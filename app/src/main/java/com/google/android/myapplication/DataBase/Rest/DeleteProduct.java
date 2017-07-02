package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;


/**
 * Created by Oana on 28-Jun-17.
 */

public class DeleteProduct extends AsyncTask<Integer, Void, Void> {
    @Override
    protected Void doInBackground(Integer... ints) {
        Integer id = ints[0];
        String urlString = "https://teme-vasileoana22.c9users.io/Product/" + id;
        try {
            //URL url = new URL(urlString);
            HttpDelete request = new HttpDelete(urlString);
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("id").value(String.valueOf(id))
                    .endObject();

            StringEntity entity = new StringEntity(json.toString());
            entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(request);
           ProductMethods productMethods = new ProductMethods();
            productMethods.delete(id);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    ;
}
