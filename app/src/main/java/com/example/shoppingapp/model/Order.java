package com.example.shoppingapp.model;

import java.io.Serializable;  // 添加导入
import java.util.List;

public class Order implements Serializable {  // 添加 implements Serializable
    public long id;
    public String orderNo;
    public String username;
    public double totalPrice;
    public String status;  // pending, shipped, completed
    public String createTime;
    public List<OrderItem> items;
}