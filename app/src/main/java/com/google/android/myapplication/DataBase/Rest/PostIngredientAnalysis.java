package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oana on 25-Jun-17.
 */

public class PostIngredientAnalysis extends AsyncTask<URL, Void, IngredientAnalysis> {


    @Override
    protected IngredientAnalysis doInBackground(URL... urls) {
        IngredientAnalysis ingredientAnalysis = null;
        URL url = urls[0];
        IngredientAnalysisMethods ingredientAnalysisMethods = new IngredientAnalysisMethods();

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONObject jObject = new JSONObject(line);
            int id = jObject.getInt("id");
            int idProduct = jObject.getInt("idProduct");
            int idIngredient = jObject.getInt("IdIngredient");
            ingredientAnalysis = new IngredientAnalysis(id, idProduct, idIngredient);
            ingredientAnalysisMethods.insert(ingredientAnalysis);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredientAnalysis;
    }
}