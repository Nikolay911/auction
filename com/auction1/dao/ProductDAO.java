package com.auction1.dao;


import com.auction1.models.Customer;
import com.auction1.models.Product;
import org.postgresql.util.PGbytea;
import org.postgresql.util.PGmoney;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

import static java.sql.JDBCType.NUMERIC;

@Component
public class ProductDAO {

    public static void main(String[] args) {

        //int pGmoney = 35;
        //NUMERIC;
        Product product = createProduct(47,"C:\\Warcraft 3\\testJava.png", "ХОРОШИЙ, ПАКУПАЙ", 5553535);

        Product product1 = getProduct(86);

        System.out.println(product1.toString());

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

    public static Product createProduct(int customerId, String foto, String productDescription, Number startPrice){

        Product product = new Product();

        //product.setFoto(foto);
        product.setProductDescription(productDescription);
        product.setStartPrice(startPrice);

        try {
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            Statement statement4 = connection.createStatement();

            ResultSet object_type_id = statement.executeQuery("SELECT object_type_id FROM nc_object_types " +
                    "WHERE name=\'product\'");

            if(object_type_id.next()){
                product.setId(object_type_id.getInt("object_type_id")); // это Id типа, а не объекта!
                ResultSet max_object_id = statement.executeQuery("SELECT MAX(object_id) from nc_objects"); // это Id объекта!

                if (max_object_id.next()) {
                    int OBJECT_ID = (max_object_id.getInt("max") + 1);
                    statement.executeUpdate("INSERT into nc_objects" +
                            " (object_type_id, name) VALUES" +
                            "(" + "\'" + product.getId() + "\'" + ", \'" + "product" + "\'" + "\'" + OBJECT_ID + "\'" + ")");
                    product.setId(OBJECT_ID);

                    ResultSet foto_attribute_id = statement1.executeQuery("SELECT attribute_id from nc_attributes where name = \'foto\'");
                    ResultSet productDescription_attribute_id = statement2.executeQuery("SELECT attribute_id from nc_attributes where name = \'productDescription\'");
                    ResultSet startPrice_attribute_id = statement3.executeQuery("SELECT attribute_id from nc_attributes where name = \'StartPrice\'");

                    ResultSet reference_to_customer_who_add_product = statement4.executeQuery("SELECT attribute_id from nc_attributes where name = \'reference_to_customer_who_add_product\'");

                    if(foto_attribute_id.next() & productDescription_attribute_id.next() & startPrice_attribute_id.next() & reference_to_customer_who_add_product.next()){

                        File image = new File(foto);
                        InputStream fis = new FileInputStream(image);
                        int ilen=(int) image.length();

                        statement2.executeUpdate("INSERT into nc_params (attribute_id, object_id, string_value) VALUES" +
                                "(" + "\'" + productDescription_attribute_id.getInt("attribute_id") + "\'" + ", \'"
                                + product.getId() + "\'" + ", \'" + productDescription + "\' )");


                        //statement2.executeUpdate("INSERT into nc_params (attribute_id, object_id, bytea_value) VALUES" +
                          //      "(" + "\'" + foto_attribute_id.getInt("attribute_id") + "\'" + ", \'"
                            //    + product.getId() + "\'" + ", \'" + productDescription + "\' )");

                        int a = foto_attribute_id.getInt("attribute_id");

                        String sql = ("INSERT into nc_params (attribute_id, object_id, bytea_value) VALUES" +
                                "(" + "\'" + a + "\'" + ", \'"
                                + product.getId() + "\'"  + ", ? )");

                        PreparedStatement stmt = connection.prepareStatement(sql);
                        stmt.setBinaryStream(1, fis, ilen);
                        stmt.execute();



                        statement3.executeUpdate("INSERT into nc_params (attribute_id, object_id, money_value) VALUES" +
                                "(" + "\'" + startPrice_attribute_id.getInt("attribute_id") + "\'" + ", \'"
                                + product.getId() + "\'" + ", \'" + product.getStartPrice() + "\' )");

                        product.setFoto(image);

                        statement.executeUpdate("INSERT into nc_references values (\'"+reference_to_customer_who_add_product.getInt("attribute_id")+"\' , \'" + product.getId()+"\', \'" + customerId + "\')");

                    }
                }
            }

        }
        catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        }

        return product;
    }







    //https://www.enterprisedb.com/edb-docs/d/jdbc-connector/user-guides/jdbc-guide/42.2.9.1/using_bytea_data_with_java.html
    public static Product getProduct(int idProduct){

        Product product = new Product();
        product.setId(idProduct);

        try {
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();


            ResultSet resultSet1 = statement1.executeQuery("select bytea_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'" + idProduct + "\') as foo2 where name = 'foto' ");
            ResultSet resultSet2 = statement2.executeQuery("select string_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'" + idProduct + "\') as foo2 where name = 'productDescription'");
            ResultSet resultSet3 = statement3.executeQuery("select money_value from (SELECT * from (SELECT * from " +
                    "nc_attributes JOIN nc_params on nc_params.attribute_id = nc_attributes.attribute_id)" +
                    " as foo where object_id = \'"+idProduct+"\') as foo2 where name = 'StartPrice'");

            if(resultSet1.next() & resultSet2.next() & resultSet3.next()){


                byte[] bytes = resultSet1.getBytes("bytea_value");
                String filename = "E:\\курсы Java\\" + idProduct;
                FileOutputStream fos = new FileOutputStream(new File(filename));
                fos.write(bytes);
                fos.close();

                //File file =(File) bytes;

                //imageByte is the byte array that is already defined
                BufferedImage image = null;
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                image = ImageIO.read(bis);
                bis.close();

                // write the image to a file
                File outputfile = new File("image.png");
                ImageIO.write(image, "png", outputfile);

                product.setFoto(outputfile);
                product.setProductDescription(resultSet2.getString("string_value"));

                product.setStartPrice((double) resultSet3.getDouble("money_value"));



            }

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


        return product;
    }

}
