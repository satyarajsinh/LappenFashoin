package com.lappenfashion.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Dashboard";
    public static final String TABLE_CART = "Cart";

    // Table columns
    public static final String _ID = "_id";
    public static final String JASONSTRING = "jsonString";

    //Table Cart Columns
    public static final String CART_ID = "cart_id";
    public static final String CART_IMAGE = "cart_image";
    public static final String CART_TITLE = "cart_title";
    public static final String CART_SIZE_FLAG = "cart_size_flag";
    public static final String CART_QTY = "cart_qty";
    public static final String CART_AMOUNT = "cart_amount";
    public static final String CART_SIZE = "CART_SIZE";
    public static final String CART_COLOR = "CART_COLOR";
    public static final String CART_PRODUCT_ID = "cart_product_id";

    // Database Information
    static final String DB_NAME = "LappenFashion.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JASONSTRING + " TEXT);";

    private static final String CREATE_TABLE_CART = "create table " + TABLE_CART + "(" + CART_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CART_IMAGE + " TEXT,"
            +CART_TITLE+" TEXT,"
            +CART_QTY+" TEXT,"
            +CART_AMOUNT+" TEXT,"
            +CART_SIZE+" TEXT,"
            +CART_COLOR+" TEXT,"
            +CART_SIZE_FLAG+" TEXT,"
            +CART_PRODUCT_ID+" TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME+"'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_CART+"'");
        onCreate(db);
    }
}