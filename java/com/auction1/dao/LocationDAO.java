package com.auction1.dao;

import com.auction1.models.Customer;
import com.auction1.models.Location;
import com.auction1.usefunction.SQLrequests;
import org.springframework.stereotype.Component;

import java.sql.*;

import static com.auction1.consts.AtrConsts.*;
import static com.auction1.consts.TypeConsts.LOCATION;

@Component
public class LocationDAO {

    public SQLrequests sqLrequests;

    public LocationDAO(SQLrequests sqLrequests){
        this.sqLrequests = sqLrequests;
    }


    public Location createLocation(String city, String street, String home, int apartment, String postcode, Connection connection) {


        Location location = new Location();

        location.setCity(city);
        location.setStreet(street);
        location.setHome(home);
        location.setApartment(apartment);
        location.setPostCode(postcode);

        try {
            Statement statement = connection.createStatement();

            location.setId(LOCATION); // это Id типа, а не объекта!
            ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!

            if (max_object_id.next()) {
                int OBJECT_ID = (max_object_id.getInt("max") + 1);
                statement.executeUpdate("INSERT into nc_objects (object_id, object_type_id, name) VALUES ( default, \'" + location.getId() + "\' , \'" + "location" + "\'" + "\'" + OBJECT_ID + "\'" + ")");

                ResultSet findId = statement.executeQuery("SELECT object_id from nc_objects where object_type_id = \'" + location.getId() + "\' and name =  \'" + "location" + "\'" + "\'" + OBJECT_ID + "\'");
                if(findId.next()){
                    location.setId(findId.getInt("object_id"));
                }

                String SQLstring = "INSERT into nc_params" +
                        " (attribute_id, object_id, string_value) VALUES ( ?, ?, ?)";
                String SQLint = "INSERT into nc_params" +
                        " (attribute_id, object_id, int_value) VALUES ( ?, ?, ?)";

                PreparedStatement statement1 = connection.prepareStatement(SQLstring);

                statement1.setInt(1, CITY);
                statement1.setInt(2, location.getId());
                statement1.setString(3, city);

                statement1.addBatch();

                statement1.setInt(1, STREET);
                statement1.setInt(2, location.getId());
                statement1.setString(3, street);

                statement1.addBatch();

                statement1.setInt(1, HOME);
                statement1.setInt(2, location.getId());
                statement1.setString(3, home);

                statement1.addBatch();

                statement1.setInt(1, POSTCODE);
                statement1.setInt(2, location.getId());
                statement1.setString(3, postcode);

                statement1.addBatch();

                statement1.executeBatch();

                statement1 = connection.prepareStatement(SQLint);

                statement1.setInt(1, APARTMENT);
                statement1.setInt(2, location.getId());
                statement1.setInt(3, apartment);

                statement1.executeUpdate();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return location;
    }




    public Location getLocation(int id) throws SQLException {

        Connection connection = this.sqLrequests.connectionToDB();

        Location location = new Location();
        location.setId(id);

        try {

            String SQLstring = "select string_value from nc_params WHERE object_id = ? " +
                    "and attribute_id = ? ";
            String SQLint = "select int_value from nc_params WHERE object_id = ? " +
                    "and attribute_id = ? ";

            PreparedStatement statement1 = connection.prepareStatement(SQLstring);
            PreparedStatement statement2 = connection.prepareStatement(SQLstring);
            PreparedStatement statement3 = connection.prepareStatement(SQLstring);
            PreparedStatement statement4 = connection.prepareStatement(SQLint);
            PreparedStatement statement5 = connection.prepareStatement(SQLstring);

            statement1.setInt(1, id);
            statement1.setInt(2, CITY);

            statement2.setInt(1, id);
            statement2.setInt(2, STREET);

            statement3.setInt(1, id);
            statement3.setInt(2, HOME);

            statement4.setInt(1, id);
            statement4.setInt(2, APARTMENT);

            statement5.setInt(1, id);
            statement5.setInt(2, POSTCODE);

            ResultSet resultSet1 = statement1.executeQuery();
            ResultSet resultSet2 = statement2.executeQuery();
            ResultSet resultSet3 = statement3.executeQuery();
            ResultSet resultSet4 = statement4.executeQuery();
            ResultSet resultSet5 = statement5.executeQuery();

            if(resultSet1.next() & resultSet2.next() & resultSet3.next() & resultSet4.next() & resultSet5.next()){
                location.setCity(resultSet1.getString("string_value"));
                location.setStreet(resultSet2.getString("string_value"));
                location.setHome(resultSet3.getString("string_value"));
                location.setApartment(resultSet4.getInt("int_value"));
                location.setPostCode(resultSet5.getString("string_value"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return location;
    }


    public boolean addLocationToCustomer(int idObjectCustomer, String city, String street, String home, int apartment, String postcode) throws SQLException {

        Connection connection = this.sqLrequests.connectionToDB();

        Savepoint savepointOne = null;
        try {
            savepointOne = connection.setSavepoint("SavepointOne");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Location location = createLocation(city, street, home, apartment, postcode, connection);
        int idObjectLocation = location.getId();

        try {
            String SQLreq = "INSERT into nc_references values (? , ?, ?)";

            PreparedStatement statement1 = connection.prepareStatement(SQLreq);

            statement1.setInt(1, REFERENCE_TO_LOCATION);
            statement1.setInt(2, idObjectCustomer);
            statement1.setInt(3, idObjectLocation);

            statement1.executeUpdate();

                connection.commit();
                connection.close();
                return true;

        } catch (SQLException throwables) {
            try {
                connection.rollback(savepointOne);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        connection.close();
        return false;
    }
}
