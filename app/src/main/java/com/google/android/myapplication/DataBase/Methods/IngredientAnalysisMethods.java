package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class IngredientAnalysisMethods {

    public static String create()
    {
        return "CREATE TABLE IF NOT EXISTS"+ IngredientAnalysis.TABLE+ " ( "+ IngredientAnalysis.label_idAnalysis+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ IngredientAnalysis.label_idIngredient+ " INTEGER, "
                + IngredientAnalysis.label_idProduct+" INTEGER);";

    }


    public long insert(IngredientAnalysis ingredientAnalysis)
    {
        long code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(IngredientAnalysis.label_idIngredient, ingredientAnalysis.getIdIngredient());
            values.put(IngredientAnalysis.label_idProduct, ingredientAnalysis.getIdProduct());

            // Inserting Row
            code= db.insertWithOnConflict(IngredientAnalysis.TABLE, null, values,SQLiteDatabase.CONFLICT_IGNORE);
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
