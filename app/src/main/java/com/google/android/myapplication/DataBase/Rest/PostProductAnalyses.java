package com.google.android.myapplication.DataBase.Rest;

import android.os.AsyncTask;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;

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

public class PostProductAnalyses extends AsyncTask<ProductAnalysis, Void, Void> {


    @Override
    protected Void doInBackground(ProductAnalysis... productAnalysises) {
        ProductAnalysis productAnalysis = productAnalysises[0];
        ProductAnalysis productAnalysisNou;
        ProductAnalysisMethods productMethods = new ProductAnalysisMethods();

        try {

            String url = "https://teme-vasileoana22.c9users.io/ProductAnalyses/";

            HttpPost request = new HttpPost(url);
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("idProduct").value(productAnalysis.getIdProduct())
                    .key("IdUser").value(productAnalysis.getIdUser())
                    .key("Data").value(productAnalysis.getDate())
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
            int idProduct = jObject.getInt("idProduct");
            int idUser = jObject.getInt("IdUser");
            String date = jObject.getString("Data");
            productAnalysisNou = new ProductAnalysis(id, idProduct, idUser, date);
            productMethods.insert(productAnalysisNou);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}