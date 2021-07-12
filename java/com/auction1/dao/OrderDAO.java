package com.auction1.dao;

import com.auction1.models.Customer;
import com.auction1.models.Order;
import com.auction1.usefunction.SQLrequests;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;

import static com.auction1.consts.AtrConsts.*;
import static com.auction1.consts.TypeConsts.ORDER;
import static com.auction1.dao.AuctionDAO.*;

@Component
public class OrderDAO {

    public CustomerDAO customerDAO;
    public SQLrequests sqLrequests;
    public AuctionDAO auctionDAO;

    public OrderDAO(CustomerDAO customerDAO, SQLrequests sqLrequests, AuctionDAO auctionDAO){
        this.customerDAO = customerDAO;
        this.sqLrequests = sqLrequests;
        this.auctionDAO = auctionDAO;
    }


    
    public String bid(int idCustomer, int idAuction, double customerPrice) throws SQLException {

        Connection connection = this.sqLrequests.connectionToDB();

        Order order = new Order();
        if(auctionDAO.auctionIsExist(idAuction, connection) & !auctionDAO.auctionIsCompleted(idAuction, connection)
                & auctionDAO.auctionIsInProgress(idAuction, connection)){

            order.setCustomerPrice(customerPrice);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            order.setBidDate(currentTime);

            Savepoint savepointOne = null;
            try {
                savepointOne = connection.setSavepoint("SavepointOne");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                Statement statement = connection.createStatement();

                order.setId(ORDER);
                ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects");
                if (max_object_id.next()) {
                    int OBJECT_ID = (max_object_id.getInt("max") + 1);
                    statement.executeUpdate("INSERT into nc_objects (object_id, object_type_id, name) VALUES ( default, \'" + order.getId() + "\' , \'" + "order" + "\'" + "\'" + OBJECT_ID + "\'" + ")");

                    ResultSet findId = statement.executeQuery("SELECT object_id from nc_objects where object_type_id = \'" + order.getId() + "\' and name =  \'" + "order" + "\'" + "\'" + OBJECT_ID + "\'");
                    if(findId.next()){
                        order.setId(findId.getInt("object_id"));
                    }

                    String SQLtimestamp = "INSERT INTO nc_params (attribute_id, object_id, timestamp_value) VALUES (?, ?, ?)";
                    String SQLmoney = "INSERT INTO nc_params (attribute_id, object_id, money_value) VALUES (?, ?, ?)";
                    String SQLref = "INSERT INTO nc_references (attribute_id, object_id, reference_id) VALUES (?, ?, ?)";

                    PreparedStatement statement1 = connection.prepareStatement(SQLtimestamp);

                    statement1.setInt(1, BID_DATE);
                    statement1.setInt(2, order.getId());
                    statement1.setTimestamp(3, order.getBidDate());

                    statement1.executeUpdate();

                    statement1 = connection.prepareStatement(SQLmoney);

                    statement1.setInt(1, CUSTOMER_PRICE);
                    statement1.setInt(2, order.getId());
                    statement1.setBigDecimal(3, BigDecimal.valueOf(order.getCustomerPrice()));

                    statement1.executeUpdate();

                    statement1 = connection.prepareStatement(SQLref);

                    statement1.setInt(1, REFERENCE_TO_AUCTION);
                    statement1.setInt(2, order.getId());
                    statement1.setInt(3, idAuction);

                    statement1.addBatch();

                    statement1.setInt(1, REFERENCE_TO_CUSTOMER_WHO_PARTICIPATES_IN_AUCTION);
                    statement1.setInt(2, order.getId());
                    statement1.setInt(3, idCustomer);

                    statement1.addBatch();

                    statement1.executeBatch();

                    System.out.println("Order is successfuly created");
                    connection.commit();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                try {
                    connection.rollback(savepointOne);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(auctionDAO.auctionIsCompleted(idAuction, connection)){
            auctionDAO.auctionStatus( idAuction, "Completed", connection);
            //System.out.println("ВЫВЕСТИ ПОБЕДИТЕЛЯ АУКЦИОНА!!!(ВЕРНУТЬ)");

            Statement statement = null;
            try {
                statement = connection.createStatement();

                ResultSet resultSetVinner = statement.executeQuery("SELECT reference_id FROM (SELECT * FROM (SELECT DISTINCT ON (\"money_value\") * FROM (SELECT * FROM (SELECT tew.object_id, tew.attribute_id, reference_id, money_value FROM\n" +
                        "    (SELECT * FROM (SELECT * FROM nc_objects NATURAL JOIN nc_references)\n" +
                        "        as rew where object_type_id=\'" + ORDER + "\' " +
                        "          and attribute_id=\'" + REFERENCE_TO_CUSTOMER_WHO_PARTICIPATES_IN_AUCTION + "\')\n" +
                        "            as tew JOIN nc_params on tew.object_id = nc_params.object_id)\n" +
                        "                as mef WHERE money_value notnull)\n" +
                        "                    as pef ORDER BY \"money_value\" DESC)\n" +
                        "                        as pog LIMIT 1 OFFSET 0) as kew;");

                if(resultSetVinner.next()){
                    int vinnerId = resultSetVinner.getInt("reference_id");

                    Customer vinner = customerDAO.getCustomer(vinnerId);
                    connection.close();
                    return "Аукцион завершен! Победитель: " + vinner.toString();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        else {
            connection.close();
            return "АУКЦИОН ОТМЕНЕН ИЛИ НЕ СУЩЕСТВУЕТ!";
        }
        connection.close();
        return "Заявка на участие создана" + order.toString();
    }

}
