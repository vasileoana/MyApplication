package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.Rating;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class RatingMethods {

    public static String create()
    {
        return "CREATE TABLE IF NOT EXISTS "+ Rating.TABLE+ " ( "+ Rating.label_idRating+ " INTEGER PRIMARY KEY, "+Rating.label_rating+ " TEXT UNIQUE);";

    }


    public long insert(Rating rating)
    { long code=0;
       try {

           SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
           ContentValues values = new ContentValues();
           values.put(Rating.label_idRating, rating.getIdRating());
           values.put(Rating.label_rating, rating.getRating());


           // Inserting Row
           code= db.insertWithOnConflict(Rating.TABLE, null, values,SQLiteDatabase.CONFLICT_IGNORE);           DatabaseManager.getInstance().closeDatabase();
       }
       catch(Exception e){
           System.out.println(e.toString());
       }
        return code;
    }


    public List<Rating> select() {
        List<Rating> ratings = new ArrayList<>();
        Rating rating;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Rating.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                rating = new Rating();
                rating.setIdRating(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rating.label_idRating))));
                rating.setRating(cursor.getString(cursor.getColumnIndex(Rating.label_rating)));
                ratings.add(rating);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return ratings;
    }


    public String getRating(int idIngredientRating) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT "+ Rating.label_rating +" FROM " + Rating.TABLE+" WHERE "+Rating.label_idRating + "="+idIngredientRating;
        String rating=null;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            rating= cursor.getString(cursor.getColumnIndex(Rating.label_rating));
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return rating;
    }

    public int getId(String nume) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT "+ Rating.label_idRating +" FROM " + Rating.TABLE+" WHERE "+Rating.label_rating + " LIKE '"+nume+"'";
        int id=0;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            id= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rating.label_idRating)));
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return id;
    }

     public static int returnRatingImage(String rating) {
        switch (rating) {
            case "SLAB":
                return R.drawable.poor;
            case "BUN":
                return R.drawable.good;
            case "FOARTE BUN":
                return R.drawable.best;
            case "MEDIU":
                return R.drawable.average;
        }
        return R.drawable.good;
    }

}
