package com.auction1.models;

import javax.xml.crypto.Data;
import java.util.Date;

public class Customer {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private Date dateOfBirth;
    private int Reference_To_Location;
    private String login;
    private String password;

    public  Customer(){

    }

    public Customer(String name, String surname, String patronymic, Date dateOfBirth, int reference_To_Location, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        Reference_To_Location = reference_To_Location;
        this.login = login;
        this.password = password;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getReference_To_Location() {
        return Reference_To_Location;
    }

    public void setReference_To_Location(int reference_To_Location) {
        Reference_To_Location = reference_To_Location;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", Reference_To_Location=" + Reference_To_Location +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
