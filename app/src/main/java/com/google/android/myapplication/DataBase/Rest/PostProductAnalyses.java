package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;

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

public class PostProductAnalyses extends AsyncTask<URL, Void, ProductAnalysis> {


    @Override
    protected ProductAnalysis doInBackground(URL... urls) {
        ProductAnalysis product = null;
        URL url = urls[0];
        ProductAnalysisMethods productMethods = new ProductAnalysisMethods();

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONObject jObject = new JSONObject(line);
            int id = jObject.getInt("id");
            int idProduct= jObject.getInt("IdUser");
            int idUser= jObject.getInt("idProduct");
            String date = jObject.getString("Data");
            product = new ProductAnalysis(id, idProduct, idUser, date);
            productMethods.insert(product);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return product;
    }
}
