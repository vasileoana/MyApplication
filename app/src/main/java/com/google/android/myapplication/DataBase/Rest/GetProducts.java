package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
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

public class GetProducts extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... args) {
        String link = args[0];
        ProductMethods productMethods= new ProductMethods();
        try {
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONArray jsonArray = new JSONArray(line);
            JSONObject jObject = null;
            Product product = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jObject = jsonArray.getJSONObject(i);
                int idCategory = Integer.parseInt(jObject.get("IdCategorie").toString());
                int idProduct = Integer.parseInt(jObject.get("id").toString());
                String description = jObject.get("Descriere").toString();
                String function = jObject.get("Functie").toString();
                String brand = jObject.get("Marca").toString();

                product = new Product(idProduct,description,brand,idCategory,function);
                productMethods.insert(product);
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
