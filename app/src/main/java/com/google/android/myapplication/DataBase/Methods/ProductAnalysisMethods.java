package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.Category;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 05-Apr-17.
 */

public class ProductAnalysisMethods {

    public static final String label_idProductAnalysis="IdProductAnalysis";
    public static final String label_idProduct="IdProduct";
    public static final String label_idUser="IdUser";
    public static final String label_date="Date";

    public static String create()
    {
        return "CREATE TABLE "+ ProductAnalysis.TABLE+ " ( "+ ProductAnalysis.label_idProductAnalysis+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ProductAnalysis.label_idProduct+ " INTEGER "+ProductAnalysis.label_idUser+ " INTEGER "+ ProductAnalysis.label_date+ " TEXT);";

    }


    public int insert(ProductAnalysis productAnalysis)
    {
        int code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(ProductAnalysis.label_date, productAnalysis.getDate());
            values.put(ProductAnalysis.label_idProduct, productAnalysis.getIdProduct());
            values.put(ProductAnalysis.label_idUser, productAnalysis.getIdUser());
            // Inserting Row
            code = (int) db.insert(ProductAnalysis.TABLE, null, values);
            DatabaseManager.getInstance().closeDatabase();


        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return code;
    }


    /*public List<Category> select() {
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


    public int getIdCategory(String category) {
        int idCategory=0;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Category.TABLE+ " WHERE "+Category.label_categoryName+ "="+category;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                idCategory=Integer.parseInt(cursor.getString(cursor.getColumnIndex(Category.label_idCategory)));
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return idCategory;
    }
*/

}
