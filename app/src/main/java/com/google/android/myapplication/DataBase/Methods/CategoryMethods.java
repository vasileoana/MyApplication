package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.Category;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class CategoryMethods {


    public static String create()
    {
        return "CREATE TABLE "+ Category.TABLE+ " ( "+ Category.label_idCategory+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+Category.label_categoryName+ " TEXT UNIQUE "+Category.label_idParent+ " INTEGER);";

    }


    public int insert(Category category)
    {
        int code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Category.label_categoryName, category.getCategoryName());

            // Inserting Row
            code = (int) db.insert(Category.TABLE, null, values);
            DatabaseManager.getInstance().closeDatabase();


        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return code;
    }


    public List<Category> select() {
        List<Category> categories = new ArrayList<>();
        Category category;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Category.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                category = new Category();
                category.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Category.label_idCategory))));
                category.setCategoryName(cursor.getString(cursor.getColumnIndex(Category.label_categoryName)));
                category.setIdParent(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Category.label_idParent))));
                categories.add(category);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return categories;
    }



}
