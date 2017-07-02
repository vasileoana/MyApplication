package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;
import com.google.android.myapplication.DataBase.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Oana on 09-Jun-17.
 */

public class GetIngredientAnalyses extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... args) {
        String linkUsers = args[0];
        IngredientAnalysisMethods ingredientMethods= new IngredientAnalysisMethods();
        try {
            URL url = new URL(linkUsers);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONArray jsonArray = new JSONArray(line);
            JSONObject jObject = null;
            IngredientAnalysis ingredientAnalisys = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jObject = jsonArray.getJSONObject(i);
                int id = Integer.parseInt(jObject.get("id").toString());
                int idProduct = Integer.parseInt(jObject.get("idProduct").toString());
                int idIngredient = Integer.parseInt(jObject.get("IdIngredient").toString());
                ingredientAnalisys = new IngredientAnalysis(id,idProduct,idIngredient);
                ingredientMethods.insert(ingredientAnalisys);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
