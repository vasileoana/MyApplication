package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;

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

public class GetProductAnalyses extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... args) {
        String linkUsers = args[0];
        ProductAnalysisMethods productMethods= new ProductAnalysisMethods();
        try {
            URL url = new URL(linkUsers);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONArray jsonArray = new JSONArray(line);
            JSONObject jObject = null;
            ProductAnalysis productAnalysis = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                jObject = jsonArray.getJSONObject(i);
                int idAnaliza = Integer.parseInt(jObject.get("id").toString());
                int idProduct = Integer.parseInt(jObject.get("idProdus").toString());
                String data = jObject.get("Data").toString();
                int idUser = Integer.parseInt(jObject.get("IdUtilizator").toString());

                productAnalysis = new ProductAnalysis(idAnaliza,idProduct,idUser,data);
                productMethods.insert(productAnalysis);
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
