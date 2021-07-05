package com.auction1.dao;

import com.auction1.models.Customer;
import com.auction1.models.Location;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class LocationDAO {


     public static void main(String[] args) {

      //Location location = createLocation("Дзержинск", "Циолковского", "39", 213, "60-60-29");




     //Location location1 = getLocation(54);
       //  System.out.println(location1.toString());


         System.out.println(addLocationToCustomer(50,"Дзержинск124", "Циолковского142", "39142", 213124, "60-60-29412"));

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

    public static Location createLocation(String city, String street, String home, int apartment, String postcode) {
        Location location = new Location();

        location.setCity(city);
        location.setStreet(street);
        location.setHome(home);
        location.setApartment(apartment);
        location.setPostCode(postcode);

        try {
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            Statement statement4 = connection.createStatement();
            Statement statement5 = connection.createStatement();

            ResultSet object_type_id = statement.executeQuery("SELECT object_type_id FROM nc_object_types " +
                    "WHERE name=\'location\'");

            if (object_type_id.next()) {
                location.setId(object_type_id.getInt("object_type_id")); // это Id типа, а не объекта!
                ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!

                if (max_object_id.next()) {
                    int OBJECT_ID = (max_object_id.getInt("max") + 1);
                    statement.executeUpdate("INSERT into nc_objects" +
                            " (object_type_id, name) VALUES" +
                            "(" + "\'" + location.getId() + "\'" + ", \'" + "location" + "\'" + "\'" + OBJECT_ID + "\'" + ")");
                    location.setId(OBJECT_ID);
                    ResultSet city_attribute_id = statement1.executeQuery("SELECT attribute_id from nc_attributes where name = \'City\'");
                    ResultSet street_attribute_id = statement2.executeQuery("SELECT attribute_id from nc_attributes where name = \'Street\'");
                    ResultSet home_attribute_id = statement3.executeQuery("SELECT attribute_id from nc_attributes where name = \'Home\'");
                    ResultSet apartment_attribute_id = statement4.executeQuery("SELECT attribute_id from nc_attributes where name = \'apartment\'");
                    ResultSet postcode_attribute_id = statement5.executeQuery("SELECT attribute_id from nc_attributes where name = \'PostCode\'");

                    if (city_attribute_id.next() & street_attribute_id.next() & home_attribute_id.next() & apartment_attribute_id.next()
                            & postcode_attribute_id.next()) {

                        statement1.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + city_attribute_id.getInt("attribute_id") + "\'" + ", \'" + location.getId() + "\'" + ",\'" + city + "\'" + ")");
                        statement2.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + street_attribute_id.getInt("attribute_id") + "\'" + ", \'" + location.getId() + "\'" + ",\'" + street + "\'" + ")");
                        statement3.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + home_attribute_id.getInt("attribute_id") + "\'" + ", \'" + location.getId() + "\'" + ",\'" + home + "\'" + ")");
                        statement4.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, int_value) VALUES" +
                                "(" + "\'" + apartment_attribute_id.getInt("attribute_id") + "\'" + ", \'" + location.getId() + "\'" + ",\'" + apartment + "\'" + ")");
                        statement5.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + postcode_attribute_id.getInt("attribute_id") + "\'" + ", \'" + location.getId() + "\'" + ",\'" + postcode + "\'" + ")");

                        return location;
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return location;
    }







    public static Location getLocation(int id) {

        Location location = new Location();
        location.setId(id);

        try {
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            Statement statement4 = connection.createStatement();
            Statement statement5 = connection.createStatement();


            ResultSet resultSet1 = statement1.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'" + id + "\') as foo2 where name = 'City' ");
            ResultSet resultSet2 = statement2.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'" + id + "\') as foo2 where name = 'Street'");
            ResultSet resultSet3 = statement3.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'Home'");
            ResultSet resultSet4 = statement4.executeQuery("select int_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'apartment'");
            ResultSet resultSet5 = statement5.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'PostCode'");


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
        return location;
    }




    public static boolean addLocationToCustomer(int idObjectCustomer, String city, String street, String home, int apartment, String postcode){

        Location location = createLocation(city, street, home, apartment, postcode);
        int idObjectLocation = location.getId();

        try {
            Statement statement1 = connection.createStatement();

            ResultSet resultSet = statement1.executeQuery("SELECT attribute_id from nc_attributes where name = 'ReferenceToLocation'");

            if(resultSet.next()){
                int ReferenceToLocation = resultSet.getInt("attribute_id");

                statement1.executeUpdate("INSERT into nc_references values (\'"+ReferenceToLocation+"\' , \'" + idObjectCustomer+"\', \'" + idObjectLocation + "\')");

                return true;
            }




        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


}
