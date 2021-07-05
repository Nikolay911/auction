package com.auction1.dao;

import com.auction1.models.Customer;
import com.auction1.models.Order;
import org.springframework.stereotype.Component;

import java.sql.*;

import static com.auction1.dao.AuctionDAO.*;
import static com.auction1.dao.CustomerDAO.getCustomer;

@Component
public class OrderDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/auction_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "22hp993761";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String bid(int idCustomer, int idAuction, double customerPrice){
        Order order = new Order();
        if(auctionIsExist(idAuction) & !auctionIsCompleted(idAuction) & auctionIsInProgress(idAuction)){

            order.setCustomerPrice(customerPrice);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            order.setBidDate(currentTime);

            try {
                Statement statement = connection.createStatement();
                Statement statement1 = connection.createStatement();
                Statement statement2 = connection.createStatement();
                Statement statement3 = connection.createStatement();
                Statement statement4 = connection.createStatement();
                Statement statement5 = connection.createStatement();
                Statement statement6 = connection.createStatement();

                ResultSet object_type_id = statement.executeQuery("SELECT object_type_id FROM nc_object_types " +
                        "WHERE name=\'order\'");
                if(object_type_id.next()){
                    order.setId(object_type_id.getInt("object_type_id"));
                    ResultSet max_object_id = statement2.executeQuery("SELECT MAX(object_id) from nc_objects");
                    if (max_object_id.next()) {
                        int OBJECT_ID = (max_object_id.getInt("max") + 1);
                        statement2.executeUpdate("INSERT into nc_objects" +
                                " (object_type_id, name) VALUES" +
                                "(" + "\'" + order.getId() + "\'" + ", \'" + "order" + "\'" + "\'" + OBJECT_ID + "\'" + ")");
                        order.setId(OBJECT_ID);

                        ResultSet resultSet  = statement3.executeQuery("SELECT attribute_id from nc_attributes where name = \'bid_date\'");
                        ResultSet resultSet1 = statement4.executeQuery("SELECT attribute_id from nc_attributes where name = \'customer_price\'");
                        ResultSet resultSet2 = statement5.executeQuery("SELECT attribute_id from nc_attributes where name = \'reference_to_auction\'");
                        ResultSet resultSet3 = statement6.executeQuery("SELECT attribute_id from nc_attributes where name = \'reference_to_customer_who_participates_in_auction\'");

                        if(resultSet.next() & resultSet1.next() & resultSet2.next() & resultSet3.next()){
                            int bd = resultSet.getInt("attribute_id");
                            int cp = resultSet1.getInt("attribute_id");
                            int rta = resultSet2.getInt("attribute_id");
                            int rtc = resultSet3.getInt("attribute_id");

                            statement3.executeUpdate("INSERT INTO nc_params (attribute_id, object_id, timestamp_value)" +
                                    " VALUES (\'"+bd+"\', \'"+ order.getId() +"\', \'"+ order.getBidDate() + "\' )");

                            statement4.executeUpdate("INSERT INTO nc_params (attribute_id, object_id, money_value)" +
                                    " VALUES (\'"+cp+"\', \'"+ order.getId() +"\', \'"+ order.getCustomerPrice() + "\' )");

                            statement5.executeUpdate("INSERT INTO nc_references (attribute_id, object_id, reference_id)" +
                                    " VALUES (\'"+rta+"\', \'"+ order.getId() +"\', \'"+ idAuction + "\' )");

                            statement6.executeUpdate("INSERT INTO nc_references (attribute_id, object_id, reference_id)" +
                                    " VALUES (\'"+rtc+"\', \'"+ order.getId() +"\', \'"+ idCustomer + "\' )");

                            System.out.println("Order is successfuly created");

                        }

                    }
                }



            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        else if(auctionIsCompleted(idAuction)){
            auctionStatus( idAuction, "Completed");
            //System.out.println("ВЫВЕСТИ ПОБЕДИТЕЛЯ АУКЦИОНА!!!(ВЕРНУТЬ)");

            Statement statement = null;
            try {
                statement = connection.createStatement();

                ResultSet resultSetVinner = statement.executeQuery("SELECT reference_id FROM (SELECT * FROM (SELECT DISTINCT ON (\"money_value\") * FROM (SELECT * FROM (SELECT tew.object_id, tew.attribute_id, reference_id, money_value FROM\n" +
                        "    (SELECT * FROM (SELECT * FROM nc_objects NATURAL JOIN nc_references)\n" +
                        "        as rew where object_type_id=5 and attribute_id=18)\n" +
                        "            as tew JOIN nc_params on tew.object_id = nc_params.object_id)\n" +
                        "                as mef WHERE money_value notnull)\n" +
                        "                    as pef ORDER BY \"money_value\" DESC)\n" +
                        "                        as pog LIMIT 1 OFFSET 0) as kew;");
                if(resultSetVinner.next()){
                    int vinnerId = resultSetVinner.getInt("reference_id");

                    Customer vinner = getCustomer(vinnerId);

                    return "Аукцион завершен! Победитель: " + vinner.toString();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        else {
            //System.out.println("АУКЦИОН ОТМЕНЕН ИЛИ НЕ СУЩЕСТВУЕТ!");
            return "АУКЦИОН ОТМЕНЕН ИЛИ НЕ СУЩЕСТВУЕТ!";
        }
        return "Заявка на участие создана" + order.toString();
    }

}
