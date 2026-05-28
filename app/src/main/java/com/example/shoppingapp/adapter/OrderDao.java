package com.example.shoppingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.shoppingapp.model.CartItem;
import com.example.shoppingapp.model.Order;
import com.example.shoppingapp.model.OrderItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class OrderDao {
    private DatabaseHelper dbHelper;

    public OrderDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    private String generateOrderNo() {
        return "ORD" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date())
                + UUID.randomUUID().toString().substring(0, 4);
    }

    public void createOrderFromCart(String username, List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String orderNo = generateOrderNo();
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.price * item.quantity;
            }
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            ContentValues orderValues = new ContentValues();
            orderValues.put(DatabaseHelper.COL_ORDER_NO, orderNo);
            orderValues.put(DatabaseHelper.COL_ORDER_USERNAME, username);
            orderValues.put(DatabaseHelper.COL_TOTAL_PRICE, total);
            orderValues.put(DatabaseHelper.COL_ORDER_STATUS, "pending");
            orderValues.put(DatabaseHelper.COL_CREATE_TIME, currentTime);
            long orderId = db.insert(DatabaseHelper.TABLE_ORDERS, null, orderValues);

            if (orderId != -1) {
                for (CartItem item : cartItems) {
                    ContentValues itemValues = new ContentValues();
                    itemValues.put(DatabaseHelper.COL_ORDER_REF_ID, orderId);
                    itemValues.put(DatabaseHelper.COL_ORDER_PRODUCT_ID, item.productId);
                    itemValues.put(DatabaseHelper.COL_ORDER_PRODUCT_NAME, item.name);
                    itemValues.put(DatabaseHelper.COL_ORDER_PRICE, item.price);
                    itemValues.put(DatabaseHelper.COL_ORDER_QUANTITY, item.quantity);
                    itemValues.put(DatabaseHelper.COL_ORDER_IMAGE, item.imageRes);
                    db.insert(DatabaseHelper.TABLE_ORDER_ITEMS, null, itemValues);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COL_ORDER_USERNAME + "=?";
        String[] selectionArgs = {username};
        String orderBy = DatabaseHelper.COL_CREATE_TIME + " DESC";
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDERS, null, selection, selectionArgs, null, null, orderBy);
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID));
            order.orderNo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_NO));
            order.username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_USERNAME));
            order.totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TOTAL_PRICE));
            order.status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_STATUS));
            order.createTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATE_TIME));
            order.items = getOrderItems(order.id);
            orders.add(order);
        }
        cursor.close();
        db.close();
        return orders;
    }

    private List<OrderItem> getOrderItems(long orderId) {
        List<OrderItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DatabaseHelper.COL_ORDER_REF_ID + "=?";
        String[] selectionArgs = {String.valueOf(orderId)};
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER_ITEMS, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            OrderItem item = new OrderItem();
            item.id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEM_ID));
            item.orderId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_REF_ID));
            item.productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PRODUCT_ID));
            item.productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PRODUCT_NAME));
            item.price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PRICE));
            item.quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_QUANTITY));
            item.imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_IMAGE));
            items.add(item);
        }
        cursor.close();
        db.close();
        return items;
    }

    public void updateOrderStatus(long orderId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ORDER_STATUS, status);
        db.update(DatabaseHelper.TABLE_ORDERS, values, DatabaseHelper.COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    // 删除订单（同时删除关联的订单项）
    public void deleteOrder(long orderId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DatabaseHelper.TABLE_ORDER_ITEMS,
                    DatabaseHelper.COL_ORDER_REF_ID + "=?",
                    new String[]{String.valueOf(orderId)});
            db.delete(DatabaseHelper.TABLE_ORDERS,
                    DatabaseHelper.COL_ORDER_ID + "=?",
                    new String[]{String.valueOf(orderId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}