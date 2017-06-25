package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;

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

public class PostProduct extends AsyncTask<URL, Void, Product> {


    @Override
    protected Product doInBackground(URL... urls) {
        Product product = null;
        URL url = urls[0];
        ProductMethods productMethods = new ProductMethods();

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONObject jObject = new JSONObject(line);
            int id = jObject.getInt("id");
            int idCateg = jObject.getInt("IdCategory");
            String description = jObject.getString("Description");
            String function = jObject.getString("IFunction");
            String brand = jObject.getString("Brand");
            product = new Product(id, description, brand, idCateg, function);
            productMethods.insert(product);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return product;
    }
}