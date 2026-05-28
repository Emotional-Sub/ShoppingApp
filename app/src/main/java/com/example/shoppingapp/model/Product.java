package com.example.shoppingapp.model;

public class Product {
    public int id;
    public String name;
    public double price;
    public String description;
    public int imageRes;

    public Product(int id, String name, double price, String description, int imageRes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageRes = imageRes;
    }
}