package com.auction1.models;

import java.sql.Timestamp;

public class AuctionModel {

    private int id;
    public int customer_who_created_auction;
    public Timestamp auction_start_date;
    public Timestamp auction_completion_date;
    public String application_status;

    public AuctionModel(){

    }

    public AuctionModel(int id, int customer_who_created_auction, Timestamp auction_start_date, Timestamp auction_completion_date, String application_status) {
        this.id = id;
        this.customer_who_created_auction = customer_who_created_auction;
        this.auction_start_date = auction_start_date;
        this.auction_completion_date = auction_completion_date;
        this.application_status = application_status;
    }

    public int getCustomer_who_created_auction() {
        return customer_who_created_auction;
    }

    public void setCustomer_who_created_auction(int customer_who_created_auction) {
        this.customer_who_created_auction = customer_who_created_auction;
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getAuction_start_date() {
        return auction_start_date;
    }

    public void setAuction_start_date(Timestamp auction_start_date) {
        this.auction_start_date = auction_start_date;
    }

    public Timestamp getAuction_completion_date() {
        return auction_completion_date;
    }

    public void setAuction_completion_date(Timestamp auction_completion_date) {
        this.auction_completion_date = auction_completion_date;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    @Override
    public String toString() {
        return "AuctionModel{" +
                "id=" + id +
                ", customer_who_created_auction=" + customer_who_created_auction +
                ", auction_start_date=" + auction_start_date +
                ", auction_completion_date=" + auction_completion_date +
                ", application_status='" + application_status + '\'' +
                '}';
    }
}
