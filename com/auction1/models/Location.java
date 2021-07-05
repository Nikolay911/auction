package com.auction1.models;

public class Location {

    private int id;
    private String city;
    private String street;
    private String home;
    private int apartment;
    private String postCode;

    public Location(){

    }

    public Location(int id, String city, String street, String home, int apartment, String postCode) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.home = home;
        this.apartment = apartment;
        this.postCode = postCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public int getApartment() {
        return apartment;
    }

    public void setApartment(int apartament) {
        this.apartment = apartament;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", home='" + home + '\'' +
                ", apartment=" + apartment +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}
