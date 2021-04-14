package com.lappenfashion.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.lappenfashion.data.model.ResponseMainLocalCart;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String json) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.JASONSTRING, json);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public void insertCart(ResponseMainLocalCart cart) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CART_IMAGE, cart.getCartImage());
        contentValue.put(DatabaseHelper.CART_QTY, cart.getCartQty());
        contentValue.put(DatabaseHelper.CART_PRODUCT_ID, cart.getProductId());
        contentValue.put(DatabaseHelper.CART_TITLE, cart.getCartTitle());
        contentValue.put(DatabaseHelper.CART_AMOUNT, cart.getCartAmount());
        database.insert(DatabaseHelper.TABLE_CART, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.JASONSTRING};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public ArrayList<ResponseMainLocalCart> fetchCart() {
        ArrayList<ResponseMainLocalCart> cartList = new ArrayList<>();
        String[] columns = new String[] { DatabaseHelper.CART_ID, DatabaseHelper.CART_IMAGE, DatabaseHelper.CART_PRODUCT_ID, DatabaseHelper.CART_QTY,DatabaseHelper.CART_TITLE,DatabaseHelper.CART_AMOUNT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_CART, columns, null, null, null, null, null);
        if (cursor != null) {
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                do {
                    ResponseMainLocalCart cart = new ResponseMainLocalCart(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CART_ID)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.CART_IMAGE)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.CART_PRODUCT_ID)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.CART_QTY)),
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.CART_TITLE)), cursor.getString(cursor.getColumnIndex(DatabaseHelper.CART_AMOUNT)));

                    cartList.add(cart);
                } while (cursor.moveToNext());
            }
        }
        return cartList;
    }

    public int update(long _id, String name, String desc, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.JASONSTRING, name);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public int updateCart(int cartId, int qty) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CART_QTY, String.valueOf(qty));
        int i = database.update(DatabaseHelper.TABLE_CART, contentValues, DatabaseHelper.CART_ID + " = " + cartId, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public void deleteCart(long _id) {
        database.delete(DatabaseHelper.TABLE_CART, DatabaseHelper.CART_ID + "=" + _id, null);
    }

}