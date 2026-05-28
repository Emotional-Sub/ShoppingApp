package com.example.shoppingapp.model;

import java.util.List;

public class Order {
    public long id;
    public String orderNo;
    public String username;
    public double totalPrice;
    public String status;  // pending, shipped, completed
    public String createTime;
    public List<OrderItem> items;
}