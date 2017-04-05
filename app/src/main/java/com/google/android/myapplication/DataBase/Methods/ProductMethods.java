package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class ProductMethods {

    public static String create()
    {
        return "CREATE TABLE "+ Product.TABLE+ " ( "+ Product.label_idProduct+ " INTEGER PRIMARY KEY, "+ Product.label_idCategory+ " INTEGER, "+ Product.label_function+ " TEXT, +" +
               Product.label_description+ " TEXT, "+ Product.label_brand + " TEXT);";

    }


    public int insert(Product product)
    { int code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Product.label_idCategory,product.getIdCategory());
            values.put(Product.label_description,product.getDescription());
            values.put(Product.label_function,product.getFunction());
            values.put(Product.label_brand,product.getBrand());

            // Inserting Row
            code = (int) db.insert(Product.TABLE, null, values);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return code;
    }


    public List<Product> select() {
        List<Product> products = new ArrayList<>();
        Product product;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Product.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct))));
                product.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idCategory))));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.label_description)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                products.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }


}
