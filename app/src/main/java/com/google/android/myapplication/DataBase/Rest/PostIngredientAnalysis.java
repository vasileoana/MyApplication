package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Oana on 25-Jun-17.
 */

public class PostIngredientAnalysis extends AsyncTask<IngredientAnalysis, Void, Void> {


    @Override
    protected Void doInBackground(IngredientAnalysis... ingredientAnalysises) {
        IngredientAnalysis ingredientAnalysis = null;
        IngredientAnalysisMethods ingredientAnalysisMethods = new IngredientAnalysisMethods();
        IngredientAnalysis ingAn = ingredientAnalysises[0];
        String url = "https://teme-vasileoana22.c9users.io/IngredientAnalyses/";
        try {
            HttpPost request = new HttpPost(url);
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("idProdus").value(ingAn.getIdProduct())
                    .key("IdIngredient").value(ingAn.getIdIngredient())
                    .endObject();

            StringEntity entity = new StringEntity(json.toString());
            entity.setContentType("application/json;charset=UTF-8");//text/plain;charset=UTF-8
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            request.setEntity(entity);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            InputStream input = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();

            JSONObject jObject = new JSONObject(line);
            int id = jObject.getInt("id");
            ingredientAnalysis = new IngredientAnalysis(id, ingAn.getIdProduct(), ingAn.getIdIngredient());
            ingredientAnalysisMethods.insert(ingredientAnalysis);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}