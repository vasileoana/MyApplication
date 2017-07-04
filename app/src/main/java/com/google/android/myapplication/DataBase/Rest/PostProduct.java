package com.google.android.myapplication.DataBase.Rest;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;

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

public class PostProduct extends AsyncTask<Product, Void, Product> {

    public static ProgressDialog dialog = new ProgressDialog(ListIngredientsActivity.context);


    @Override
    protected void onPreExecute() {
        //set message of the dialog
        dialog.setMessage("Asteapta...");
        //show dialog
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Product doInBackground(Product... products) {

        Product productNou = null;
        Product product = products[0];
        ProductMethods productMethods = new ProductMethods();

        String url = "https://teme-vasileoana22.c9users.io/Product/";
        try {
            HttpPost request = new HttpPost(url);
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("Descriere").value(product.getDescription())
                    .key("Functie").value(product.getFunction())
                    .key("Marca").value(product.getBrand())
                    .key("IdCategorie").value(product.getIdCategory())
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

            JSONObject jObject =  new JSONObject(line);
            int id = jObject.getInt("id");
            productNou = new Product(id, product.getDescription(), product.getBrand(), product.getIdCategory(),product.getFunction());
            productMethods.insert(productNou);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productNou;
    }

    @Override
    protected void onPostExecute(Product product) {
        super.onPostExecute(product);
    }
}