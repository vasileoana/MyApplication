package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class IngredientAnalysisMethods {

    public static String create()
    {
        return "CREATE TABLE "+ IngredientAnalysis.TABLE+ " ( "+ IngredientAnalysis.label_idAnalysis+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ IngredientAnalysis.label_idIngredient+ " INTEGER, "
                + IngredientAnalysis.label_idProduct+" INTEGER);";

    }


    public int insert(IngredientAnalysis ingredientAnalysis)
    {
        int code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(IngredientAnalysis.label_idIngredient, ingredientAnalysis.getIdIngredient());
            values.put(IngredientAnalysis.label_idProduct, ingredientAnalysis.getIdProduct());

            // Inserting Row
            code = (int) db.insert(IngredientAnalysis.TABLE, null, values);
            DatabaseManager.getInstance().closeDatabase();


        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return code;
    }


    public List<IngredientAnalysis> select() {
        List<IngredientAnalysis> analysises = new ArrayList<>();
        IngredientAnalysis ingredientAnalysis;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + IngredientAnalysis.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ingredientAnalysis = new IngredientAnalysis();
                ingredientAnalysis.setIdIngredient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IngredientAnalysis.label_idIngredient))));
                ingredientAnalysis.setIdAnalysis(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IngredientAnalysis.label_idAnalysis))));
                ingredientAnalysis.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IngredientAnalysis.label_idProduct))));

                analysises.add(ingredientAnalysis);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return analysises;
    }



}
