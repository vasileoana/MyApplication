package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 05-Apr-17.
 */

public class ProductAnalysisMethods {


    public static String create()
    {
        return "CREATE TABLE IF NOT EXISTS "+ ProductAnalysis.TABLE+ " ( "+ ProductAnalysis.label_idProductAnalysis+ " INTEGER PRIMARY KEY, "+ProductAnalysis.label_idProduct+ " INTEGER, "+ProductAnalysis.label_idUser+ " INTEGER, "+ ProductAnalysis.label_date+ " TEXT);";

    }

    public void delete(int idProduct) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(ProductAnalysis.TABLE,"IdProduct=?",new String[]{(String.valueOf(idProduct))});
        DatabaseManager.getInstance().closeDatabase();
    }

    public long insert(ProductAnalysis productAnalysis)
    {
        long code=0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(ProductAnalysis.label_idProductAnalysis, productAnalysis.getIdProductAnalysis());
            values.put(ProductAnalysis.label_date, productAnalysis.getDate());
            values.put(ProductAnalysis.label_idProduct, productAnalysis.getIdProduct());
           values.put(ProductAnalysis.label_idUser, productAnalysis.getIdUser());
            code= db.insertWithOnConflict(ProductAnalysis.TABLE, null, values,SQLiteDatabase.CONFLICT_IGNORE);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return code;
    }

    public List<ProductAnalysis> select() {
        List<ProductAnalysis> products = new ArrayList<>();
        ProductAnalysis product;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + ProductAnalysis.TABLE+";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new ProductAnalysis();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProductAnalysis.label_idProduct))));
                product.setDate(cursor.getString(cursor.getColumnIndex(ProductAnalysis.label_date)));
                product.setIdProductAnalysis(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProductAnalysis.label_idProductAnalysis))));
                product.setIdUser(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ProductAnalysis.label_idUser))));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }

    public String getDate(int id) {
        String date=null;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + ProductAnalysis.TABLE+" WHERE "+ProductAnalysis.label_idProduct+"="+id+";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

               date=cursor.getString(cursor.getColumnIndex(ProductAnalysis.label_date));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return date;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(ProductAnalysis.TABLE, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
