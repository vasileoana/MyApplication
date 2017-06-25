package com.google.android.myapplication.Utilities.Register;

import android.content.res.AssetManager;

import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Model.Category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Oana on 25-Jun-17.
 */

public class ReadCategories {

    public void readCategories(AssetManager assetManager, CategoryMethods categoryMethods){
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;

        try {
            inputStream=assetManager.open("categories.csv");
            inputStreamReader=new InputStreamReader(inputStream);
            reader=new BufferedReader(inputStreamReader);
            String line;
            Category category=new Category();
            while((line = reader.readLine()) != null) {
                String[] entry = line.split(",");
                String categoryName = entry[1];
                int idCategory = Integer.parseInt(entry[0]);
                category.setIdCategory(idCategory);
                category.setCategoryName(categoryName);
                categoryMethods.insert(category);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
