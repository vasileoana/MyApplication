package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Model.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

/**
 * Created by Oana on 29-Jun-17.
 */

public class PutUser extends AsyncTask<User, Void, String> {
    @Override
    protected String doInBackground(User... users) {
        String line=null;
        User user = users[0];
        String urlString = "https://teme-vasileoana22.c9users.io/Users/" + user.getIdUser();
        try {
            HttpPut request = new HttpPut(urlString);
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("id").value(user.getIdUser())
                    .key("Username").value(user.getUsername())
                    .key("Password").value(user.getPassword())
                    .key("Email").value(user.getEmail())
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
             line = in.readLine();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return line;
    }
}