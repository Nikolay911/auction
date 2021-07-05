package com.auction1.dao;

import com.auction1.models.Customer;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerDAO {

    public static void main(String[] args) {

      //  Customer customer;
     //   Date date = new Date(121, 12, 17);
        //System.out.println(createCustomer("SURNAME222", "NAME", "PATRONYMIC",
                //date, 1, "LOGIN", "PASSWORD"));

       // customer = getCustomer(25);
       // System.out.println(customer.toString());


        List customers = getAllCustomers();

        int size = customers.size();

        for (int i = 0; i < size; i++){
            System.out.println(customers.get(i).toString());
        }
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

    public Customer createCustomer(String surname, String name, String patronymic, Date dateOfBirth, int ReferenceToLocation, String login, String password) {
        Customer customer = new Customer();

        customer.setSurname(surname);
        customer.setName(name);
        customer.setPatronymic(patronymic);
        customer.setDateOfBirth(dateOfBirth);
        customer.setReference_To_Location(ReferenceToLocation);
        customer.setLogin(login);
        customer.setPassword(password);


        try {
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            Statement statement4 = connection.createStatement();
            Statement statement5 = connection.createStatement();
            Statement statement6 = connection.createStatement();
            Statement statement7 = connection.createStatement();

            ResultSet object_type_id = statement.executeQuery("SELECT object_type_id FROM nc_object_types " +
                    "WHERE name=\'customer\'");

            if (object_type_id.next()) {
                customer.setId(object_type_id.getInt("object_type_id")); // это Id типа, а не объекта!
                ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!
                if (max_object_id.next()) {
                    int OBJECT_ID = (max_object_id.getInt("max") + 1);
                    statement.executeUpdate("INSERT into nc_objects" +
                            " (object_type_id, name) VALUES" +
                            "(" + "\'" + customer.getId() + "\'" + ", \'" + "customer" + "\'" + "\'" + OBJECT_ID + "\'" + ")");
                    customer.setId(OBJECT_ID);
                    ResultSet surname_attribute_id = statement1.executeQuery("SELECT attribute_id from nc_attributes where name = \'surname\'");
                    ResultSet name_attribute_id = statement2.executeQuery("SELECT attribute_id from nc_attributes where name = \'name\'");
                    ResultSet patronymic_attribute_id = statement3.executeQuery("SELECT attribute_id from nc_attributes where name = \'patronymic\'");
                    ResultSet dateOfBirth_attribute_id = statement4.executeQuery("SELECT attribute_id from nc_attributes where name = \'dateOfBirth\'");
                    ResultSet ReferenceToLocation_attribute_id = statement5.executeQuery("SELECT attribute_id from nc_attributes where name = \'ReferenceToLocation\'");
                    ResultSet login_attribute_id = statement6.executeQuery("SELECT attribute_id from nc_attributes where name = \'login\'");
                    ResultSet password_attribute_id = statement7.executeQuery("SELECT attribute_id from nc_attributes where name = \'password\'");

                    if (surname_attribute_id.next() & name_attribute_id.next() & patronymic_attribute_id.next() & dateOfBirth_attribute_id.next()
                            & ReferenceToLocation_attribute_id.next() & login_attribute_id.next() & password_attribute_id.next()) {

                        statement1.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + surname_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + surname + "\'" + ")");
                        statement2.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + name_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + name + "\'" + ")");
                        statement3.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + patronymic_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + patronymic + "\'" + ")");
                        statement4.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, date_value) VALUES" +
                                "(" + "\'" + dateOfBirth_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + dateOfBirth + "\'" + ")");
                        statement5.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, int_value) VALUES" +
                                "(" + "\'" + ReferenceToLocation_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + ReferenceToLocation + "\'" + ")");
                        statement6.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + login_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + login + "\'" + ")");
                        statement7.executeUpdate("INSERT into nc_params" +
                                " (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + password_attribute_id.getInt("attribute_id") + "\'" + ", \'" + customer.getId() + "\'" + ",\'" + password + "\'" + ")");


                        return customer;
                    }
                }
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer;
    }




    public static Customer getCustomer(int id) {

        Customer customer = new Customer();
        customer.setId(id);

        try {
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            Statement statement4 = connection.createStatement();
            Statement statement5 = connection.createStatement();
            Statement statement6 = connection.createStatement();
            Statement statement7 = connection.createStatement();

            ResultSet resultSet1 = statement1.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'" + id + "\') as foo2 where name = 'surname' ");
            ResultSet resultSet2 = statement2.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'" + id + "\') as foo2 where name = 'name'");
            ResultSet resultSet3 = statement3.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'patronymic'");
            ResultSet resultSet4 = statement4.executeQuery("select date_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'dateOfBirth'");
            ResultSet resultSet5 = statement5.executeQuery("select int_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'ReferenceToLocation'");
            ResultSet resultSet6 = statement6.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'login'");
            ResultSet resultSet7 = statement7.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+id+"\') as foo2 where name = 'password'");

            if(resultSet1.next() & resultSet2.next() & resultSet3.next() & resultSet4.next() & resultSet5.next() & resultSet6.next() & resultSet7.next()){
                customer.setSurname(resultSet1.getString("string_value"));
                customer.setName(resultSet2.getString("string_value"));
                customer.setPatronymic(resultSet3.getString("string_value"));
                customer.setDateOfBirth(resultSet4.getDate("date_value"));
                customer.setReference_To_Location(resultSet5.getInt("int_value"));
                customer.setLogin(resultSet6.getString("string_value"));
                customer.setPassword(resultSet7.getString("string_value"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer;
    }







    public static List<Customer> getAllCustomers(){

        List<Customer> customers = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();

            ResultSet object_type_id = statement.executeQuery("SELECT object_type_id FROM nc_object_types " +
                    "WHERE name=\'customer\'");

            if (object_type_id.next()) {

                int type_id = object_type_id.getInt("object_type_id");


                ResultSet allCustomers = statement1.executeQuery("SELECT Count(*) from nc_objects WHERE object_type_id = \'"+type_id+"\'");

                if(allCustomers.next()) {
                    int[] count = new int[allCustomers.getInt("count")];

                    ResultSet customersId = statement1.executeQuery("SELECT object_id from nc_objects where object_type_id = \'"+type_id+"\'");

                    int i = 0;
                    while(customersId.next()){
                        count[i] = customersId.getInt("object_id");
                        i++;
                    }

                    for(int j = 0; j < count.length; j++){
                        customers.add(getCustomer(count[j]));
                    }
                    return customers;
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customers;
    }
}
