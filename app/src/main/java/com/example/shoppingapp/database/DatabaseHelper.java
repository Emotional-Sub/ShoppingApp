package com.example.shoppingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "shopping.db";
    private static final int DB_VERSION = 2;

    // 用户表
    public static final String TABLE_USER = "users";
    public static final String COL_USER_ID = "_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // 购物车表
    public static final String TABLE_CART = "cart";
    public static final String COL_CART_ID = "_id";
    public static final String COL_PRODUCT_ID = "product_id";
    public static final String COL_PRODUCT_NAME = "product_name";
    public static final String COL_PRICE = "price";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_IMAGE = "image_res";

    // 订单主表
    public static final String TABLE_ORDERS = "orders";
    public static final String COL_ORDER_ID = "_id";
    public static final String COL_ORDER_NO = "order_no";
    public static final String COL_ORDER_USERNAME = "username";
    public static final String COL_TOTAL_PRICE = "total_price";
    public static final String COL_ORDER_STATUS = "status"; // pending, shipped, completed
    public static final String COL_CREATE_TIME = "create_time";

    // 订单商品明细表
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COL_ORDER_ITEM_ID = "_id";
    public static final String COL_ORDER_REF_ID = "order_id";
    public static final String COL_ORDER_PRODUCT_ID = "product_id";
    public static final String COL_ORDER_PRODUCT_NAME = "product_name";
    public static final String COL_ORDER_PRICE = "price";
    public static final String COL_ORDER_QUANTITY = "quantity";
    public static final String COL_ORDER_IMAGE = "image_res";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 用户表
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)");

        // 购物车表
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_ID + " INTEGER, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_QUANTITY + " INTEGER, " +
                COL_IMAGE + " INTEGER)");

        // 订单主表
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_NO + " TEXT UNIQUE, " +
                COL_ORDER_USERNAME + " TEXT, " +
                COL_TOTAL_PRICE + " REAL, " +
                COL_ORDER_STATUS + " TEXT, " +
                COL_CREATE_TIME + " TEXT)");

        // 订单明细表
        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_REF_ID + " INTEGER, " +
                COL_ORDER_PRODUCT_ID + " INTEGER, " +
                COL_ORDER_PRODUCT_NAME + " TEXT, " +
                COL_ORDER_PRICE + " REAL, " +
                COL_ORDER_QUANTITY + " INTEGER, " +
                COL_ORDER_IMAGE + " INTEGER)");

        // 插入测试用户
        db.execSQL("INSERT INTO " + TABLE_USER + " (" + COL_USERNAME + "," + COL_PASSWORD + ") VALUES ('user','123456')");
        db.execSQL("INSERT INTO " + TABLE_USER + " (" + COL_USERNAME + "," + COL_PASSWORD + ") VALUES ('admin','admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 只新增订单表，不删除原有表
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " (" +
                    COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ORDER_NO + " TEXT UNIQUE, " +
                    COL_ORDER_USERNAME + " TEXT, " +
                    COL_TOTAL_PRICE + " REAL, " +
                    COL_ORDER_STATUS + " TEXT, " +
                    COL_CREATE_TIME + " TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_ITEMS + " (" +
                    COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ORDER_REF_ID + " INTEGER, " +
                    COL_ORDER_PRODUCT_ID + " INTEGER, " +
                    COL_ORDER_PRODUCT_NAME + " TEXT, " +
                    COL_ORDER_PRICE + " REAL, " +
                    COL_ORDER_QUANTITY + " INTEGER, " +
                    COL_ORDER_IMAGE + " INTEGER)");
        }
    }
}