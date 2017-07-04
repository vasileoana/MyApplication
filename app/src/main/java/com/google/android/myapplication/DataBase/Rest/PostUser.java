package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oana on 25-Jun-17.
 */

public class PostUser extends AsyncTask<User, Void, User> {


    @Override
    protected User doInBackground(User... users) {
        User userNou=null;
        UserMethods userMethods= new UserMethods();
        User user = users[0];
        String url = "https://teme-vasileoana22.c9users.io/users/";
        try {
            HttpPost request = new HttpPost(url);
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("nume_utilizator").value(user.getUsername())
                    .key("parola").value(user.getPassword())
                    .key("email").value(user.getEmail())
                    .endObject();

            StringEntity entity = new StringEntity(json.toString());
            entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
            request.setEntity(entity);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            InputStream input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();

            if(response.getStatusLine().getStatusCode()/100==5){
                userNou = new User();
                userNou.setUsername("server-inchis");
            }
           else if(!line.equals("exista"))
            {
                JSONObject jObject =  new JSONObject(line);
                int id = jObject.getInt("id");
                String username = jObject.getString("nume_utilizator");
                String pass = jObject.getString("parola");
                String email = jObject.getString("email");
                userNou = new User(username, pass, id , email);
                userMethods.insert(userNou);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userNou;
    }
}
