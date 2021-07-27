package com.auction1.dao;


import com.auction1.jdbcconf.SQLrequests;
import com.auction1.models.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

import static com.auction1.consts.AttributesConsts.*;
import static com.auction1.consts.TypeConsts.PRODUCT;

@Component
public class ProductDAO {

    public SQLrequests sqLrequests;

    public ProductDAO(SQLrequests sqLrequests) {
        this.sqLrequests = sqLrequests;
    }


    public Product createProduct(int customerId, JsonNode body) throws SQLException {

        Connection connection = this.sqLrequests.getConnection();

        ObjectMapper objectMapper = new ObjectMapper();

        Product product = objectMapper.convertValue(body, Product.class);

        product.setProductDescription(product.getProductDescription());
        product.setStartPrice(product.getStartPrice());

        Savepoint savepointOne = null;
        try {
            savepointOne = connection.setSavepoint("SavepointOne");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            Statement statement = connection.createStatement();


            product.setId(PRODUCT);
            ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects");

            if (max_object_id.next()) {
                int OBJECT_ID = (max_object_id.getInt("max") + 1);

                statement.executeUpdate("INSERT into nc_objects (object_id, object_type_id, name) VALUES ( default, \'"
                        + product.getId() + "\' , \'" + "product" + "\'" + "\'" + OBJECT_ID + "\'" + ")");

                ResultSet findId = statement.executeQuery("SELECT object_id from nc_objects where object_type_id = \'"
                        + product.getId() + "\' and name =  \'" + "product" + "\'" + "\'" + OBJECT_ID + "\'");

                if (findId.next()) {
                    product.setId(findId.getInt("object_id"));
                }

                File image = new File(String.valueOf(product.getFoto()));
                InputStream fis = new FileInputStream(image);
                int ilen = (int) image.length();

                String SQLbytea = ("INSERT into nc_params (attribute_id, object_id, bytea_value) VALUES (?, ?, ? )");
                String SQLstring = ("INSERT into nc_params (attribute_id, object_id, string_value) VALUES (?, ?, ? )");
                String SQLmoney = ("INSERT into nc_params (attribute_id, object_id, money_value) VALUES (?, ?, ? )");
                String SQLint = ("INSERT into nc_references  VALUES (?, ?, ? )");

                PreparedStatement stmt = connection.prepareStatement(SQLbytea);
                stmt.setInt(1, FOTO);
                stmt.setInt(2, product.getId());
                stmt.setBinaryStream(3, fis, ilen);

                stmt.execute();

                stmt = connection.prepareStatement(SQLstring);

                stmt.setInt(1, PRODUCT_DESCRIPTION);
                stmt.setInt(2, product.getId());
                stmt.setString(3, product.getProductDescription());

                stmt.execute();

                stmt = connection.prepareStatement(SQLmoney);

                stmt.setInt(1, START_PRICE);
                stmt.setInt(2, product.getId());
                stmt.setInt(3, (Integer) product.getStartPrice());

                stmt.execute();

                stmt = connection.prepareStatement(SQLint);

                stmt.setInt(1, REFERENCE_TO_CUSTOMER_WHO_ADD_PRODUCT);
                stmt.setInt(2, product.getId());
                stmt.setInt(3, customerId);

                stmt.execute();

                product.setFoto(image);
                connection.commit();
            }
        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback(savepointOne);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return product;
    }


    public Product getProduct(int idProduct) throws SQLException {

        Connection connection = this.sqLrequests.getConnection();

        Product product = new Product();
        product.setId(idProduct);

        try {

            String SQLbytea = "select bytea_value from nc_params WHERE object_id = ? and attribute_id = ?";
            String SQLstring = "select string_value from nc_params WHERE object_id = ? and attribute_id = ?";
            String SQLmoney = "select money_value from nc_params WHERE object_id = ? and attribute_id = ?";

            PreparedStatement statement1 = connection.prepareStatement(SQLbytea);
            PreparedStatement statement2 = connection.prepareStatement(SQLstring);
            PreparedStatement statement3 = connection.prepareStatement(SQLmoney);

            statement1.setInt(1, idProduct);
            statement1.setInt(2, FOTO);

            statement2.setInt(1, idProduct);
            statement2.setInt(2, PRODUCT_DESCRIPTION);

            statement3.setInt(1, idProduct);
            statement3.setInt(2, START_PRICE);

            ResultSet resultSet1 = statement1.executeQuery();
            ResultSet resultSet2 = statement2.executeQuery();
            ResultSet resultSet3 = statement3.executeQuery();

            if (resultSet1.next() & resultSet2.next() & resultSet3.next()) {

                byte[] bytes = resultSet1.getBytes("bytea_value");
                String filename = "C:\\Program Files\\курсы Java\\foto_rest\\" + idProduct + ".png";
                FileOutputStream fos = new FileOutputStream(new File(filename));
                fos.write(bytes);
                fos.close();

                BufferedImage image = null;
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                image = ImageIO.read(bis);
                bis.close();

                File outputfile = new File("image.png");
                ImageIO.write(image, "png", outputfile);

                product.setFoto(outputfile);
                product.setProductDescription(resultSet2.getString("string_value"));

                product.setStartPrice((Number) resultSet3.getDouble("money_value"));
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return product;
    }
}