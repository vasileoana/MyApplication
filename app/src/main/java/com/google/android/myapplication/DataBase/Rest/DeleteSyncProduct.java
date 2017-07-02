package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 02-Jul-17.
 */

public class DeleteSyncProduct extends AsyncTask<List<Integer>, Void, Void> {
    @Override
    protected Void doInBackground(List<Integer>... ints) {
        List<Integer> listaProduseSterse = new ArrayList();
        listaProduseSterse = ints[0];
        for ( Integer id : listaProduseSterse){
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


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        return null;
    }

}
