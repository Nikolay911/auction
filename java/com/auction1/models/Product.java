package com.auction1.models;


import java.io.File;
import java.sql.Blob;

public class Product {

    private int id;
    private File foto;
    private String productDescription;
    private Number startPrice;

    public Product(){

    }

    public Product(int id, File foto, String productDescription, Number startPrice) {
        this.id = id;
        this.foto = foto;
        this.productDescription = productDescription;
        this.startPrice = startPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFoto() {
        return foto;
    }

    public void setFoto(File foto) {
        this.foto = foto;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Number getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Number startPrice) {
        this.startPrice = startPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", foto=" + foto +
                ", productDescription='" + productDescription + '\'' +
                ", startPrice=" + startPrice +
                '}';
    }
}
