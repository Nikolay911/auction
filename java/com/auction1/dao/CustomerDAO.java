package com.auction1.dao;

import com.auction1.information_models.CustomerInf;
import com.auction1.jdbcconf.SQLrequests;
import com.auction1.models.Customer;
import com.auction1.models.Location;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.auction1.consts.AttributesConsts.*;
import static com.auction1.consts.SqlRequestsConsts.MAX_OBJECT_ID;
import static com.auction1.consts.TypeConsts.CUSTOMER;


@Component
public class CustomerDAO {

    private final SQLrequests sqLrequests;
    private final LocationDAO locationDAO;

    public CustomerDAO(SQLrequests sqLrequests, LocationDAO locationDAO) {
        this.sqLrequests = sqLrequests;
        this.locationDAO = locationDAO;
    }


    public Customer createCustomer(JsonNode body) throws SQLException {
        Connection connection = this.sqLrequests.getConnection();

        ObjectMapper objectMapper = new ObjectMapper();

        Customer customer = objectMapper.convertValue(body, Customer.class);

        Savepoint savepointOne = null;
        try {
            savepointOne = connection.setSavepoint("SavepointOne");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            Statement statement = connection.createStatement();


            customer.setId(CUSTOMER);
            ResultSet max_object_id = statement.executeQuery(MAX_OBJECT_ID);
            if (max_object_id.next()) {
                int OBJECT_ID = (max_object_id.getInt("max") + 1);

                statement.executeUpdate("INSERT into nc_objects (object_id, object_type_id, name) VALUES ( default, \'" +
                        customer.getId() + "\' , \'" + "customer" + "\'" + "\'" + OBJECT_ID + "\'" + ")");

                ResultSet findId = statement.executeQuery("SELECT object_id from nc_objects where object_type_id = \'" +
                        customer.getId() + "\' and name =  \'" + "customer" + "\'" + "\'" + OBJECT_ID + "\'");

                if (findId.next()) {
                    customer.setId(findId.getInt("object_id"));
                }

                String SQLsting = "INSERT into nc_params(attribute_id, object_id, string_value) VALUES (?,?,?)";
                String SQLdate = "INSERT into nc_params" +
                        " (attribute_id, object_id, date_value) VALUES (?,?,?)";
                String SQLint = "INSERT into nc_params" +
                        " (attribute_id, object_id, int_value) VALUES (?,?,?)";

                PreparedStatement statement1 = connection.prepareStatement(SQLsting); //STRING-------------------------

                statement1.setInt(1, SURNAME);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, customer.getSurname());

                statement1.addBatch();

                statement1.setInt(1, NAME);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, customer.getName());

                statement1.addBatch();

                statement1.setInt(1, PATRONYMIC);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, customer.getPatronymic());

                statement1.addBatch();

                statement1.setInt(1, LOGIN);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, customer.getLogin());

                statement1.addBatch();

                statement1.setInt(1, PASSWORD);
                statement1.setInt(2, customer.getId());
                statement1.setString(3, customer.getPassword());

                statement1.addBatch();

                statement1.executeBatch();

                statement1 = connection.prepareStatement(SQLdate); //DATE-----------------------------------------------

                statement1.setInt(1, DATE_OF_BIRTH);
                statement1.setInt(2, customer.getId());
                statement1.setDate(3, customer.getDateOfBirth());

                statement1.executeUpdate();

                statement1 = connection.prepareStatement(SQLint); //INT-------------------------------------------------

                statement1.setInt(1, REFERENCE_TO_LOCATION);
                statement1.setInt(2, customer.getId());
                statement1.setInt(3, customer.getReferenceToLocation());

                statement1.executeUpdate();

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
        connection.close();
        return customer;
    }

    public Customer getCustomer(int id) throws SQLException {

        Connection connection = this.sqLrequests.getConnection();

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

            if (resultSet1.next() & resultSet2.next() & resultSet3.next() & resultSet4.next() & resultSet5.next() & resultSet6.next() & resultSet7.next()) {
                customer.setSurname(resultSet1.getString("string_value"));
                customer.setName(resultSet2.getString("string_value"));
                customer.setPatronymic(resultSet3.getString("string_value"));
                customer.setDateOfBirth(resultSet4.getDate("date_value"));
                customer.setReferenceToLocation(resultSet5.getInt("int_value"));
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

        Connection connection = this.sqLrequests.getConnection();

        List<Customer> customers = new ArrayList<>();

        try {
            Statement statement1 = connection.createStatement();

            ResultSet allCustomers = statement1.executeQuery(
                    "SELECT Count(*) from nc_objects WHERE object_type_id = \'" + CUSTOMER + "\'");

            if (allCustomers.next()) {
                int[] count = new int[allCustomers.getInt("count")];

                ResultSet customersId = statement1.executeQuery(
                        "SELECT object_id from nc_objects where object_type_id = \'" + CUSTOMER + "\'");

                int i = 0;
                while (customersId.next()) {
                    count[i] = customersId.getInt("object_id");
                    i++;
                }

                for (int j = 0; j < count.length; j++) {
                    customers.add(getCustomer(count[j]));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return customers;
    }

    public List<CustomerInf> getCustomerInf() throws SQLException {

        Customer customer = new Customer();
        List<Location> location = new ArrayList<>();

        List<CustomerInf> customerInf = new ArrayList<>();

        List<Customer> allCustomers = getAllCustomers();
        int size = allCustomers.size();

        for (int i = 0; i < size; i++) {
            customerInf.add(new CustomerInf(allCustomers.get(i), locationDAO.getAllLocationToCustomer(allCustomers.get(i).getId())));
        }
        return customerInf;
    }
}
