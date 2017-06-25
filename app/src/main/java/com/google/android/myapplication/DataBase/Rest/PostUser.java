package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.User;

import org.json.JSONArray;
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

public class PostUser extends AsyncTask<URL, Void, User> {


    @Override
    protected User doInBackground(URL... urls) {
       User user=null;
        UserMethods userMethods= new UserMethods();
        URL url = urls[0];

        try {
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            if(!line.equals("exista")) {
                JSONObject jObject =  new JSONObject(line);
                int id = jObject.getInt("id");
                String username = jObject.getString("Username");
                String pass = jObject.getString("Password");
                String email = jObject.getString("Email");
                user = new User(username, pass, id , email);
                userMethods.insert(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
