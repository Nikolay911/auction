package com.auction1.dao;

import java.sql.*;

import com.auction1.jdbcconf.ConfigProperties;
import com.auction1.models.AuctionModel;
import com.auction1.usefunction.SQLrequests;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Component;

import static com.auction1.consts.AtrConsts.*;
import static com.auction1.consts.TypeConsts.AUCTION;

@Component
public class AuctionDAO {

    public SQLrequests sqLrequests;

    public AuctionDAO(SQLrequests sqLrequests){
        this.sqLrequests = sqLrequests;
    }

    public AuctionModel createAuction(int idCustomer, Timestamp completeTime, int idProduct) throws SQLException {

        Connection connection = this.sqLrequests.connectionToDB();

        AuctionModel auction = new AuctionModel();

        auction.setCustomer_who_created_auction(idCustomer);
        auction.setAuction_completion_date(completeTime);

        Savepoint savepointOne = null;
        try {
            savepointOne = connection.setSavepoint("SavepointOne");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            Statement statement  = connection.createStatement();

            String SQLcheckUserProductExist = "SELECT object_id from nc_references where attribute_id =? and reference_id = ?";
            PreparedStatement statement1 = connection.prepareStatement(SQLcheckUserProductExist);

            statement1.setInt(1, REFERENCE_TO_CUSTOMER_WHO_ADD_PRODUCT);
            statement1.setInt(2, idCustomer);

            ResultSet checkUserProductExist = statement1.executeQuery();
            while(checkUserProductExist.next()){
               if( checkUserProductExist.getInt("object_id") == idProduct){

                   auction.setId(AUCTION); // это Id типа, а не объекта!
                   ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!
                   if (max_object_id.next()) {
                       int OBJECT_ID = (max_object_id.getInt("max") + 1);
                       statement.executeUpdate("INSERT into nc_objects (object_id, object_type_id, name) VALUES ( default, \'" + auction.getId() + "\' , \'" + "auction" + "\'" + "\'" + OBJECT_ID + "\'" + ")");

                       ResultSet findId = statement.executeQuery("SELECT object_id from nc_objects where object_type_id = \'" + auction.getId() + "\' and name =  \'" + "auction" + "\'" + "\'" + OBJECT_ID + "\'");
                       if(findId.next()){
                           auction.setId(findId.getInt("object_id"));
                       }
                       String status = "InProgress";
                       Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                       auction.setAuction_start_date(currentTime);
                       auction.setApplication_status(status);

                       String SQLstring = "INSERT into nc_params (attribute_id, object_id, string_value) VALUES (?, ?, ?)";
                       String SQLtimestamp = "INSERT into nc_params (attribute_id, object_id, timestamp_value) VALUES (?, ?, ?)";
                       String SQLint = "INSERT into nc_references values (?, ?, ?)";

                       PreparedStatement statement2 = connection.prepareStatement(SQLstring);

                       statement2.setInt(1, APPLICATION_STATUS);
                       statement2.setInt(2, auction.getId());
                       statement2.setString(3, status);

                       statement2.executeUpdate();

                       statement2 = connection.prepareStatement(SQLtimestamp);

                       statement2.setInt(1, AUCTION_START_DATE);
                       statement2.setInt(2, auction.getId());
                       statement2.setTimestamp(3, currentTime);

                       statement2.addBatch();

                       statement2.setInt(1, AUCTION_COMPLETION_DATE);
                       statement2.setInt(2, auction.getId());
                       statement2.setTimestamp(3, completeTime);

                       statement2.addBatch();

                       statement2.executeBatch();

                       statement2 = connection.prepareStatement(SQLint);

                       statement2.setInt(1, REFERENCE_TO_PRODUCT_WHO_PARTICIPATES_IN_AUCTION);
                       statement2.setInt(2, auction.getId());
                       statement2.setInt(3, idProduct);

                       statement2.addBatch();

                       statement2.setInt(1, REFERENCE_TO_CUSTOMER_WHO_CREATED_AUCTION);
                       statement2.setInt(2, auction.getId());
                       statement2.setInt(3, idCustomer);

                       statement2.addBatch();

                       statement2.executeBatch();

                       connection.commit();
                   }
               }
            }

        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback(savepointOne);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return auction;
    }


    public String auctionStatus(int idAuction, String status, Connection connection){
        try {
            String SQL = "UPDATE nc_params Set string_value = ? where object_id = ? and string_value = ?";
            PreparedStatement statement2 = connection.prepareStatement(SQL);

            statement2.setString(1, status);
            statement2.setInt(2, idAuction);
            statement2.setString(1, "InProgress");

            statement2.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return "Now, auction status with ID " + idAuction + " is " + status;
    }


    public boolean auctionIsExist(int idAuction , Connection connection){

        try {
            String SQL = "SELECT object_id FROM nc_objects where object_id = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, idAuction);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return false;
    }


    public boolean auctionIsInProgress(int idAuction, Connection connection){

        try {
            String SQL = "SELECT string_value FROM nc_params where object_id = ? and string_value = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, idAuction);
            statement.setString(2, "InProgress");

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return false;
    }


    public boolean auctionIsCompleted(int idAuction, Connection connection){

        try {
            String SQL = "SELECT timestamp_value from nc_params WHERE object_id = ? AND attribute_id = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setInt(1, idAuction);
            statement.setInt(2, AUCTION_COMPLETION_DATE);

            ResultSet resultSet2 = statement.executeQuery();

            if(resultSet2.next()){
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                Timestamp endTime = resultSet2.getTimestamp("timestamp_value");

                if(endTime.getTime() - currentTime.getTime() <= 0){
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
