package com.google.android.myapplication.DataBase.Files;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Oana on 24-Apr-17.
 */

public class ReadIngredients extends AsyncTask< AssetManager, Void, Void> {


                @Override
                protected Void doInBackground(AssetManager... params) {
                    AssetManager assetManager=params[0];
                    InputStream inputStream;
                    InputStreamReader inputStreamReader;
                    BufferedReader reader;
                    IngredientMethods ingredientMethod=new IngredientMethods();
                    try {
                        inputStream=assetManager.open("ingredient.csv");
                        inputStreamReader=new InputStreamReader(inputStream);
                        reader=new BufferedReader(inputStreamReader);
                        String line;
                        Ingredient ingredient=new Ingredient();
                        while((line = reader.readLine()) != null) {
                            String[] entry = line.split(",");
                            String name = entry[0];
                            int idRating = Integer.parseInt(entry[1]);
                            String desc;
                            ingredient.setIdRating(idRating);
                            ingredient.setName(name);
                            if (entry.length == 3) {
                                desc = entry[2];
                                ingredient.setDescription(desc);
                            }
                            else {
                                ingredient.setDescription(null);
                            }
                            ingredientMethod.insert(ingredient);
                        }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
