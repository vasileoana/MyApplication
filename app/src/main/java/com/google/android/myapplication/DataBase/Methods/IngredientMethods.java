package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;
import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.Ingredient;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class IngredientMethods {


    public static String create()
    {
        return "CREATE TABLE IF NOT EXISTS "+ Ingredient.TABLE+ " ( "+ Ingredient.label_idIngredient+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+Ingredient.label_name+ " TEXT UNIQUE, "
                +Ingredient.label_description+" TEXT, "+Ingredient.label_idRating+ " INTEGER);";

    }


    public long insert(Ingredient ingredient)
    {
        long code=0;
       try {

           SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
           ContentValues values = new ContentValues();
         //  values.put(Ingredient.label_idIngredient, ingredient.getIdIngredient());
           values.put(Ingredient.label_name, ingredient.getName());
           values.put(Ingredient.label_description, ingredient.getDescription());
           values.put(Ingredient.label_idRating, ingredient.getIdRating());

           // Inserting Row
           code= db.insertWithOnConflict(Ingredient.TABLE, null, values,SQLiteDatabase.CONFLICT_IGNORE);
           DatabaseManager.getInstance().closeDatabase();


       }
       catch(Exception e) {
           System.out.println(e.toString());
    }
        return code;
    }


    public List<Ingredient> select() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Ingredient.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ingredient = new Ingredient();
                ingredient.setIdIngredient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idIngredient))));
                ingredient.setDescription(cursor.getString(cursor.getColumnIndex(Ingredient.label_description)));
                ingredient.setIdRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idRating))));
                ingredient.setName(cursor.getString(cursor.getColumnIndex(Ingredient.label_name)));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }


            cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return ingredients;
    }


    public List<Ingredient> selectIngredients(String ing) {
            List<Ingredient> ingredients = new ArrayList<>();
            Ingredient ingredient;
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery = " SELECT * FROM " + Ingredient.TABLE + " WHERE "+Ingredient.label_name + " LIKE '%" +ing+"%'";

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ingredient = new Ingredient();
                    ingredient.setIdIngredient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idIngredient))));
                    ingredient.setDescription(cursor.getString(cursor.getColumnIndex(Ingredient.label_description)));
                    ingredient.setIdRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idRating))));
                    ingredient.setName(cursor.getString(cursor.getColumnIndex(Ingredient.label_name)));
                    ingredients.add(ingredient);
                } while (cursor.moveToNext());
            }


            cursor.close();
            DatabaseManager.getInstance().closeDatabase();

            return ingredients;
    }


    public Ingredient selectIngredient(String nume) {
        Ingredient ingredient=new Ingredient();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Ingredient.TABLE + " WHERE "+Ingredient.label_name + " LIKE '" +nume+"';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ingredient.setIdIngredient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idIngredient))));
                ingredient.setDescription(cursor.getString(cursor.getColumnIndex(Ingredient.label_description)));
                ingredient.setIdRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idRating))));
                ingredient.setName(cursor.getString(cursor.getColumnIndex(Ingredient.label_name)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return ingredient;
    }

    public List<Ingredient> selectIngredients(int idProdus) {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT I.* FROM " + Ingredient.TABLE +" I INNER JOIN "+ IngredientAnalysis.TABLE+" IA ON I."+Ingredient.label_idIngredient+"=IA."+IngredientAnalysis.label_idIngredient+" WHERE IA."+IngredientAnalysis.label_idProduct+"="+idProdus+";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ingredient = new Ingredient();
                ingredient.setIdIngredient(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idIngredient))));
                ingredient.setDescription(cursor.getString(cursor.getColumnIndex(Ingredient.label_description)));
                ingredient.setIdRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Ingredient.label_idRating))));
                ingredient.setName(cursor.getString(cursor.getColumnIndex(Ingredient.label_name)));
                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return ingredients;
    }
}
