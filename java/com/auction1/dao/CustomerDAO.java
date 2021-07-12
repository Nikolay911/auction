package com.auction1.dao;

import com.auction1.jdbcconf.ConfigProperties;
import com.auction1.models.Customer;
import com.auction1.consts.AtrConsts;
import com.auction1.usefunction.SQLrequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.auction1.consts.AtrConsts.*;
import static com.auction1.consts.TypeConsts.CUSTOMER;


@Component
public class CustomerDAO {
    public SQLrequests sqLrequests;

    public CustomerDAO(SQLrequests sqLrequests){
        this.sqLrequests = sqLrequests;
    }

    public Customer createCustomer(String surname, String name, String patronymic, Date dateOfBirth, int ReferenceToLocation, String login, String password) throws SQLException {
        Connection connection = this.sqLrequests.connectionToDB();

        Customer customer = new Customer();

        customer.setSurname(surname);
        customer.setName(name);
        customer.setPatronymic(patronymic);
        customer.setDateOfBirth(dateOfBirth);
        customer.setReference_To_Location(ReferenceToLocation);
        customer.setLogin(login);
        customer.setPassword(password);


        Savepoint savepointOne = null;
        try {
            savepointOne = connection.setSavepoint("SavepointOne");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            Statement statement = connection.createStatement();


            customer.setId(CUSTOMER); // это Id типа, а не объекта!
            ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!
            if (max_object_id.next()) {
                int OBJECT_ID = (max_object_id.getInt("max") + 1);
                statement.executeUpdate("INSERT into nc_objects (object_id, object_type_id, name) VALUES ( default, \'" + customer.getId() + "\' , \'" + "customer" + "\'" + "\'" + OBJECT_ID + "\'" + ")");

                ResultSet findId = statement.executeQuery("SELECT object_id from nc_objects where object_type_id = \'" + customer.getId() + "\' and name =  \'" + "customer" + "\'" + "\'" + OBJECT_ID + "\'");
                if(findId.next()){
                    customer.setId(findId.getInt("object_id"));
                }

                String SQLstring =  "INSERT into nc_params" +
                        " (attribute_id, object_id, string_value) VALUES (?,?,?)";
                String SQLdate =  "INSERT into nc_params" +
                        " (attribute_id, object_id, date_value) VALUES (?,?,?)";
                String SQLint =  "INSERT into nc_params" +
                        " (attribute_id, object_id, int_value) VALUES (?,?,?)";

                PreparedStatement statement1 = connection.prepareStatement(SQLstring); //STRING-------------------------

                statement1.setInt(1, SURNAME);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, surname);

                statement1.addBatch();

                statement1.setInt(1, NAME);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, surname);

                statement1.addBatch();

                statement1.setInt(1, PATRONYMIC);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, patronymic);

                statement1.addBatch();

                statement1.setInt(1, LOGIN);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, login);

                statement1.addBatch();

                statement1.setInt(1, PASSWORD);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, password);

                statement1.addBatch();

                statement1.executeBatch();

                statement1 = connection.prepareStatement(SQLdate); //DATE-----------------------------------------------

                statement1.setInt(1, DATE_OF_BIRTH);
                statement1.setInt(2, customer.getId());
                statement1.setDate(3, dateOfBirth);

                statement1.executeUpdate();

                statement1 = connection.prepareStatement(SQLint); //INT-------------------------------------------------

                statement1.setInt(1, REFERENCE_TO_LOCATION);
                statement1.setInt(2, customer.getId());
                statement1.setInt(3, ReferenceToLocation);

                statement1.executeUpdate();

                connection.commit();
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
        return customer;
    }




    public Customer getCustomer(int id) throws SQLException {

        Connection connection = this.sqLrequests.connectionToDB();

        Customer customer = new Customer();
        customer.setId(id);

        try {

            String SQLstring = "select string_value from nc_params WHERE object_id = ? " +
                    "and attribute_id = ? ";
            String SQLint = "select int_value from nc_params WHERE object_id = ? " +
                    "and attribute_id = ? ";
            String SQLdate = "select date_value from nc_params WHERE object_id = ? " +
                    "and attribute_id = ? ";

            PreparedStatement statement1 = connection.prepareStatement(SQLstring);
            PreparedStatement statement2 = connection.prepareStatement(SQLstring);
            PreparedStatement statement3 = connection.prepareStatement(SQLstring);
            PreparedStatement statement4 = connection.prepareStatement(SQLdate);
            PreparedStatement statement5 = connection.prepareStatement(SQLint);
            PreparedStatement statement6 = connection.prepareStatement(SQLstring);
            PreparedStatement statement7 = connection.prepareStatement(SQLstring);

            statement1.setInt(1, id);
            statement1.setInt(2, SURNAME);

            statement2.setInt(1, id);
            statement2.setInt(2, NAME);

            statement3.setInt(1, id);
            statement3.setInt(2, PATRONYMIC);

            statement4.setInt(1, id);
            statement4.setInt(2, DATE_OF_BIRTH);

            statement5.setInt(1, id);
            statement5.setInt(2, REFERENCE_TO_LOCATION);

            statement6.setInt(1, id);
            statement6.setInt(2, LOGIN);

            statement7.setInt(1, id);
            statement7.setInt(2, PASSWORD);

            ResultSet resultSet1 = statement1.executeQuery();
            ResultSet resultSet2 = statement2.executeQuery();
            ResultSet resultSet3 = statement3.executeQuery();
            ResultSet resultSet4 = statement4.executeQuery();
            ResultSet resultSet5 = statement5.executeQuery();
            ResultSet resultSet6 = statement6.executeQuery();
            ResultSet resultSet7 = statement7.executeQuery();

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
        connection.close();
        return customer;
    }


    public List<Customer> getAllCustomers() throws SQLException {

        Connection connection = this.sqLrequests.connectionToDB();

        List<Customer> customers = new ArrayList<>();

        try {
            Statement statement1 = connection.createStatement();

            ResultSet allCustomers = statement1.executeQuery("SELECT Count(*) from nc_objects WHERE object_type_id = \'"+CUSTOMER+"\'");

            if(allCustomers.next()) {
                int[] count = new int[allCustomers.getInt("count")];

                ResultSet customersId = statement1.executeQuery("SELECT object_id from nc_objects where object_type_id = \'"+CUSTOMER+"\'");

                int i = 0;
                while(customersId.next()){
                    count[i] = customersId.getInt("object_id");
                    i++;
                }

                for(int j = 0; j < count.length; j++){
                    customers.add(getCustomer(count[j]));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return customers;
    }
}
