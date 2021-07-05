package com.auction1.dao;

import java.sql.*;

import com.auction1.models.AuctionModel;
import org.springframework.stereotype.Component;

@Component
public class AuctionDAO {

    public static void main(String[] args){

        Timestamp timeEnd = new Timestamp(121,6,4,16,50,0,0);

        AuctionModel auctionModel = createAuction(47,timeEnd,86);

        System.out.println(auctionModel.toString());

    }

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





    public static AuctionModel createAuction(int idCustomer, Timestamp completeTime, int idProduct) {

        AuctionModel auction = new AuctionModel();

        auction.setCustomer_who_created_auction(idCustomer);
        auction.setAuction_completion_date(completeTime);

        try {
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            Statement statement4 = connection.createStatement();
            Statement statement5 = connection.createStatement();

            ResultSet object_type_id = statement.executeQuery("SELECT object_type_id FROM nc_object_types " +
                    "WHERE name=\'auction\'");

            ResultSet checkUserProductExist = statement5.executeQuery("SELECT object_id from nc_references where attribute_id = 11 and reference_id = \'" + idCustomer + "\'");
            while(checkUserProductExist.next()){
               if( checkUserProductExist.getInt("object_id") == idProduct){
                   if (object_type_id.next()) {
                       auction.setId(object_type_id.getInt("object_type_id")); // это Id типа, а не объекта!
                       ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!
                       if (max_object_id.next()) {
                           int OBJECT_ID = (max_object_id.getInt("max") + 1);
                           statement.executeUpdate("INSERT into nc_objects" +
                                   " (object_type_id, name) VALUES" +
                                   "(" + "\'" + auction.getId() + "\'" + ", \'" + "auction" + "\'" + "\'" + OBJECT_ID + "\'" + ")");
                           auction.setId(OBJECT_ID);

                           ResultSet resultSet1 = statement1.executeQuery("SELECT attribute_id from nc_attributes where name = \'application_status\'");
                           ResultSet resultSet2 = statement2.executeQuery("SELECT attribute_id from nc_attributes where name = \'auction_start_date\'");
                           ResultSet resultSet3 = statement3.executeQuery("SELECT attribute_id from nc_attributes where name = \'auction_completion_date\'");

                           ResultSet resultSet4 = statement4.executeQuery("SELECT attribute_id from nc_attributes where name = \'reference_to_product_who_participates_in_auction\'");
                           //ResultSet resultSet5 = statement.executeQuery("SELECT attribute_id from nc_attributes where name = \'reference_to_customer_who_participates_in_auction\'");

                           if(resultSet1.next()&resultSet2.next()&resultSet3.next()&resultSet4.next()){
                               String status = "InProgress";
                               Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                               auction.setAuction_start_date(currentTime);
                               auction.setApplication_status(status);

                               statement.executeUpdate("INSERT into nc_params (attribute_id, object_id, string_value) VALUES" +
                                       "(" + "\'" + resultSet1.getInt("attribute_id") + "\'" + ", \'"
                                       + auction.getId() + "\'" + ", \'" + status + "\' )");
                               statement.executeUpdate("INSERT into nc_params (attribute_id, object_id, timestamp_value) VALUES" +
                                       "(" + "\'" + resultSet2.getInt("attribute_id") + "\'" + ", \'"
                                       + auction.getId() + "\'" + ", \'" + currentTime + "\' )");
                               statement.executeUpdate("INSERT into nc_params (attribute_id, object_id, timestamp_value) VALUES" +
                                       "(" + "\'" + resultSet3.getInt("attribute_id") + "\'" + ", \'"
                                       + auction.getId() + "\'" + ", \'" + completeTime + "\' )");

                               statement.executeUpdate("INSERT into nc_references values (\'"+resultSet4.getInt("attribute_id")+"\' , \'" + auction.getId()+"\', \'" + idProduct + "\')");
                               statement.executeUpdate("INSERT into nc_references values (\'"+17+"\' , \'" + auction.getId()+"\', \'" + idCustomer + "\')");

                           }
                       }
                   }
               }
            }

        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return auction;
    }


    public static String auctionStatus(int idAuction, String status){


        try {
            //Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();

            //ResultSet resultSet = statement1.executeQuery("SELECT object_id from nc_references where attribute_id = 17 and reference_id = \'" + idCustomer + "\'");

            //while (resultSet.next()){
                //if(resultSet.getInt("object_id") == idAuction){

                    statement2.executeUpdate("UPDATE nc_params Set string_value = \'"+status+"\' where object_id = \'" + idAuction +"\' and string_value = 'InProgress'");

               // }
            //}

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return "Now, auction status with ID " + idAuction + " is " + status;
    }


    public static boolean auctionIsExist(int idAuction){

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT object_id FROM nc_objects where object_id = \'" + idAuction + "\' ");
            if (resultSet.next()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return false;
    }


    public static boolean auctionIsInProgress(int idAuction){

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT string_value FROM nc_params where object_id = \'" + idAuction +"\' and string_value = 'InProgress'");
            if(resultSet.next()){
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return false;
    }


    public static boolean auctionIsCompleted(int idAuction){

        try {
            //Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();


            //ResultSet resultSet1 = statement1.executeQuery("SELECT timestamp_value from nc_params WHERE object_id = \'" + idAuction + "\' AND attribute_id = 23"); // 23 это id атрибута(доделать , чтобы определялся автоматически)
            ResultSet resultSet2 = statement2.executeQuery("SELECT timestamp_value from nc_params WHERE object_id = \'" + idAuction + "\' AND attribute_id = 24");

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
