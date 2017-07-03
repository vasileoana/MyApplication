package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Oana on 03-Jul-17.
 */

public class IsServerOpen extends AsyncTask<Void,Void,String> {
    @Override
    protected String doInBackground(Void... voids) {
        HttpGet request = new HttpGet("https://teme-vasileoana22.c9users.io/products");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(response.getStatusLine().getStatusCode()/100);
    }
}
