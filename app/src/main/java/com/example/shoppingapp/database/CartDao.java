package com.example.shoppingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.shoppingapp.model.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private DatabaseHelper dbHelper;

    public CartDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addToCart(int productId, String name, double price, int imageRes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CART, null,
                DatabaseHelper.COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY));
            quantity++;
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_QUANTITY, quantity);
            db.update(DatabaseHelper.TABLE_CART, values,
                    DatabaseHelper.COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_PRODUCT_ID, productId);
            values.put(DatabaseHelper.COL_PRODUCT_NAME, name);
            values.put(DatabaseHelper.COL_PRICE, price);
            values.put(DatabaseHelper.COL_QUANTITY, 1);
            values.put(DatabaseHelper.COL_IMAGE, imageRes);
            db.insert(DatabaseHelper.TABLE_CART, null, values);
        }
        cursor.close();
        db.close();
    }

    public List<CartItem> getAllCartItems() {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CART, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            CartItem item = new CartItem();
            item.id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_ID));
            item.productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_ID));
            item.name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRODUCT_NAME));
            item.price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE));
            item.quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY));
            item.imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE));
            list.add(item);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateQuantity(long cartId, int newQuantity) {
        if (newQuantity <= 0) {
            deleteCartItem(cartId);
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_QUANTITY, newQuantity);
        db.update(DatabaseHelper.TABLE_CART, values, DatabaseHelper.COL_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        db.close();
    }

    public void deleteCartItem(long cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.COL_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART, null, null);
        db.close();
    }
}