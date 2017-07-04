package com.google.android.myapplication.DataBase.Rest;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.User;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class PutProduct extends AsyncTask<Product, Void, Void>{
    @Override
    protected Void doInBackground(Product... products) {
        Product prod = products[0];
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
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(entity);
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(request);
            System.out.println(response);
            InputStream input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();

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
