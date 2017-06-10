package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oana on 09-Jun-17.
 */

public class Post extends AsyncTask<URL, Void, Void> {

    @Override
    protected Void doInBackground(URL... urls) {
        URL url = urls[0];

        try {
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            InputStream input = con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}