package com.auction1.usefunction;

import com.auction1.jdbcconf.ConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.postgresql.shaded.com.ongres.scram.common.ScramAttributes.USERNAME;

@Component
public class SQLrequests {

    public ConfigProperties configProperties;

    public SQLrequests(ConfigProperties param)
    {
        configProperties = param;

    }

    public Connection connectionToDB(){
          Connection connection = null;

        try {
            connection = DriverManager.getConnection(this.configProperties.getURL(), this.configProperties.getUSERNAME(), this.configProperties.getPASSWORD());
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }
}
