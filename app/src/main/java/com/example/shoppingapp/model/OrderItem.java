package com.example.shoppingapp.model;

import java.io.Serializable;  // 添加导入

public class OrderItem implements Serializable {  // 添加 implements Serializable
    public long id;
    public long orderId;
    public int productId;
    public String productName;
    public double price;
    public int quantity;
    public int imageRes;
}