package com.google.android.myapplication.DataBase;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Model.Rating;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.Utilities.App;

/**
 * Created by Oana on 27-Feb-17.
 */

public class DBHelper  extends SQLiteOpenHelper{


    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "PRODUCT_ANALYSIS.db";
    private static final String TAG = DBHelper.class.getSimpleName();



    public DBHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserMethods.create());
        db.execSQL(IngredientMethods.create());
        db.execSQL(RatingMethods.create());
        db.execSQL(IngredientAnalysisMethods.create());
        db.execSQL(CategoryMethods.create());
        db.execSQL(ProductMethods.create());
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Ingredient.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Rating.TABLE);
        onCreate(db);

    }
}
