package com.auction1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer{

    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private java.sql.Date dateOfBirth;
    private int ReferenceToLocation;
    private String login;
    private String password;
    //@JsonIgnore

}
