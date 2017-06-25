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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Oana on 25-Jun-17.
 */

public class GetUser  extends AsyncTask<URL, Void, User> {


    @Override
    protected User doInBackground(URL... args) {
        User user = null;
        URL url = args[0];
        UserMethods userMethods = new UserMethods();
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream input = con.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            JSONArray jsonArray = new JSONArray(line);
            if(jsonArray.length()==1) {
                JSONObject jObject = jsonArray.getJSONObject(0);
                int id = Integer.parseInt(jObject.get("id").toString());
                String username = jObject.get("Username").toString();
                String parola = jObject.get("Password").toString();
                String email = jObject.get("Email").toString();

                user = new User(username, parola, id, email);
                userMethods.insert(user);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }


}
