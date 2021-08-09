package com.auction1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private int ReferenceToLocation;
    @JsonIgnore
    private String login;
    @JsonIgnore
    private String password;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
