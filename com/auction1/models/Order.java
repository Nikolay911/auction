package com.auction1.models;

import java.sql.Timestamp;

public class Order {

    private int id;
    private double customerPrice;
    private Timestamp bidDate;

    public Order(){

    }

    public Order(int id, double customerPrice, Timestamp bidDate) {
        this.id = id;
        this.customerPrice = customerPrice;
        this.bidDate = bidDate;
    }

    public double getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(double customerPrice) {
        this.customerPrice = customerPrice;
    }

    public Timestamp getBidDate() {
        return bidDate;
    }

    public void setBidDate(Timestamp bidDate) {
        this.bidDate = bidDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerPrice=" + customerPrice +
                ", bidDate=" + bidDate +
                '}';
    }
}
