package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 02-Jul-17.
 */

public class PutSyncProduct extends AsyncTask<List<Integer>, Void, Void> {
    @Override
    protected Void doInBackground(List<Integer>... products) {
        List<Integer> listSterse = new ArrayList<>();
        listSterse = products[0];
        Product prod;
        ProductMethods productMethods = new ProductMethods();
        for(Integer id: listSterse) {
            prod = new Product();
            prod = productMethods.selectProductById(id);
            String urlString = "https://teme-vasileoana22.c9users.io/Product/" + prod.getIdProduct();
            try {
                HttpPut request = new HttpPut(urlString);
                JSONStringer json = new JSONStringer()
                        .object()
                        .key("id").value(prod.getIdProduct())
                        .key("IdCategorie").value(prod.getIdCategory())
                        .key("Descriere").value(prod.getDescription())
                        .key("Functie").value(prod.getFunction())
                        .key("Marca").value(prod.getBrand())
                        .endObject();

                StringEntity entity = new StringEntity(json.toString());
                entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                request.setEntity(entity);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
