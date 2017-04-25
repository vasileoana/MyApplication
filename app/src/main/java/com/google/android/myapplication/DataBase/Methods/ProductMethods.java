package com.google.android.myapplication.DataBase.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.myapplication.Utilities.DataBase.DatabaseManager;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Mar-17.
 */

public class ProductMethods {

    public static String create()
    {
        return "CREATE TABLE IF NOT EXISTS "+ Product.TABLE+ " ( "+ Product.label_idProduct+ " INTEGER PRIMARY KEY, "+ Product.label_idCategory+ " INTEGER, " +
               Product.label_description+ " TEXT, "+Product.label_function+ " TEXT, "+ Product.label_brand + " TEXT);";

    }


    public long insert(Product product)
    { long code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Product.label_idCategory,product.getIdCategory());
            values.put(Product.label_description,product.getDescription());
            values.put(Product.label_brand,product.getBrand());
            values.put(Product.label_function,product.getFunction());

            // Inserting Row
            code= db.insertWithOnConflict(Product.TABLE, null, values,SQLiteDatabase.CONFLICT_IGNORE);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return code;
    }


    public int update(Product product)
    { int code=0;
        try {

            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Product.label_idCategory,product.getIdCategory());
            values.put(Product.label_description,product.getDescription());
            values.put(Product.label_brand,product.getBrand());
            values.put(Product.label_function,product.getFunction());

            // Update Row
            code = (int) db.update(Product.TABLE, values, Product.label_idProduct+"=?", new String[] {String.valueOf(product.getIdProduct())} );
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
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));
                products.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }


    public int getIdProduct(Product product) {
        int id=0;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT "+ Product.label_idProduct+" FROM " + Product.TABLE+ " WHERE "+Product.label_idCategory+"="+product.getIdCategory()+" AND "+Product.label_brand+"='"+product.getBrand()+ "' AND "+Product.label_description+"='"+product.getDescription()+"' AND "+Product.label_function+"='"+product.getFunction()+"';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id=Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct)));

            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return id;
    }

    public void delete(int idProduct) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Product.TABLE,"IdProduct=?",new String[]{(String.valueOf(idProduct))});
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Product> selectProductsByUser(int idUser) {
        List<Product> products = new ArrayList<>();
        Product product;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Product.TABLE+ " P INNER JOIN "+ ProductAnalysis.TABLE+ " PA ON P."+Product.label_idProduct+"=PA."+ProductAnalysis.label_idProduct+" WHERE "+ProductAnalysis.label_idUser+"="+idUser+";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct))));
                product.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idCategory))));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.label_description)));
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));
                products.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }
    public Product selectProductById(int id) {

        Product product=new Product();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Product.TABLE+ " WHERE "+Product.label_idProduct+"="+id+";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct))));
                product.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idCategory))));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.label_description)));
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return product;
    }


    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        Product product;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Product.TABLE+ " P INNER JOIN "+ ProductAnalysis.TABLE+ " PA ON P."+Product.label_idProduct+"=PA."+ProductAnalysis.label_idProduct+";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct))));
                product.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idCategory))));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.label_description)));
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));
                products.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }


    public List<Product> selectAllProductsAsc() {
        List<Product> products = new ArrayList<>();
        Product product;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Product.TABLE+ " P INNER JOIN "+ ProductAnalysis.TABLE+ " PA ON P."+Product.label_idProduct+"=PA."+ProductAnalysis.label_idProduct+" ORDER BY "+ProductAnalysis.label_date+ " ASC;";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct))));
                product.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idCategory))));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.label_description)));
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));
                products.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }

    public List<Product> selectAllProductsDesc() {
        List<Product> products = new ArrayList<>();
        Product product;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT * FROM " + Product.TABLE+ " P INNER JOIN "+ ProductAnalysis.TABLE+ " PA ON P."+Product.label_idProduct+"=PA."+ProductAnalysis.label_idProduct+" ORDER BY "+ProductAnalysis.label_date+ " DESC;";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setIdProduct(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idProduct))));
                product.setIdCategory(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Product.label_idCategory))));
                product.setDescription(cursor.getString(cursor.getColumnIndex(Product.label_description)));
                product.setBrand(cursor.getString(cursor.getColumnIndex(Product.label_brand)));
                product.setFunction(cursor.getString(cursor.getColumnIndex(Product.label_function)));
                products.add(product);
            } while (cursor.moveToNext());
        }


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return products;
    }
}
