package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.DataBase.Rest.GetIngredientAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProducts;
import com.google.android.myapplication.DataBase.Rest.PostUser;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Register.CheckInternetConnection;
import com.google.android.myapplication.DataBase.Files.ReadCategories;
import com.google.android.myapplication.DataBase.Files.ReadIngredients;
import com.google.android.myapplication.DataBase.Files.ReadRatings;

import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout tilUsername, tilPass, tilPass2, tilEmail;
    EditText etUsername, etPass, etPass2, etEmail;
    String username, pass, email;
    User user;
    IngredientAnalysisMethods ingredientAnalysisMethods = new IngredientAnalysisMethods();
    ProductAnalysisMethods productAnalysisMethods = new ProductAnalysisMethods();
    ProductMethods productMethods = new ProductMethods();
    UserMethods userMethods;
    IngredientMethods ingredientMethod;
    RatingMethods ratingMethods;
    CategoryMethods categoryMethods;
    ReadRatings readRatings;
    ReadIngredients readIngredients;
    ReadCategories readCategories;
    CheckInternetConnection checkInternetConnection;
    GetProducts getProducts = new GetProducts();
    GetIngredientAnalyses getIngredientAnalyses = new GetIngredientAnalyses();
    GetProductAnalyses getProductAnalyses = new GetProductAnalyses();
    PostUser postUser;
    User user_rezultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userMethods = new UserMethods();
        ingredientMethod = new IngredientMethods();
        ratingMethods = new RatingMethods();
        categoryMethods = new CategoryMethods();
        readIngredients = new ReadIngredients();
        readRatings = new ReadRatings();
        readCategories = new ReadCategories();
        checkInternetConnection = new CheckInternetConnection();
        tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        tilPass = (TextInputLayout) findViewById(R.id.tilPassword);
        tilPass2 = (TextInputLayout) findViewById(R.id.tilPassword2);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);
        etPass2 = (EditText) findViewById(R.id.etPassword2);
        etEmail = (EditText) findViewById(R.id.etEmail);

        Button btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().trim().length() == 0) {
                    etUsername.requestFocus();
                    tilUsername.setError("Numele de utilizator este necesar!");
                } else if (etPass.getText().toString().length() < 6) {
                    tilUsername.setError(null);
                    etPass.requestFocus();
                    tilPass.setError("Alegeti o parola de minim 6 caractere!");
                } else if (etPass2.getText().toString().compareTo(etPass.getText().toString()) != 0) {
                    tilPass.setError(null);
                    etPass2.requestFocus();
                    tilPass2.setError("Parola trebuie sa fie identica cu cea de mai sus!");
                } else if (etEmail.getText().toString().trim().length() <= 2 || !etEmail.getText().toString().contains("@")) {
                    tilPass2.setError(null);
                    etEmail.requestFocus();
                    tilEmail.setError("Scrieti o adresa de email valida!");
                } else {

                    tilEmail.setError(null);
                    username = etUsername.getText().toString().trim();
                    pass = etPass.getText().toString().trim();
                    email = etEmail.getText().toString().trim();
                    user = new User(username, pass, email);

                    if (checkInternetConnection.isNetworkAvailable(getApplicationContext())) {
                        URL url = null;
                        try {
                            url = new URL("https://teme-vasileoana22.c9users.io/Users/" + username + "/" + pass + "/" + email);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        postUser = (PostUser) new PostUser() {
                            @Override
                            protected void onPostExecute(User user) {
                                user_rezultat = user;
                                if (user_rezultat != null) {
                                    AssetManager assetManager = getAssets();
                                    if (categoryMethods.select().size() == 0) {
                                        readIngredients.execute(getAssets());
                                        readRatings.readRatings(assetManager, ratingMethods);
                                        readCategories.readCategories(assetManager, categoryMethods);
                                    }
                                    productMethods.delete();
                                    productAnalysisMethods.delete();
                                    ingredientAnalysisMethods.delete();
                                    getProductAnalyses.execute("https://teme-vasileoana22.c9users.io/ProductAnalyses");
                                    getIngredientAnalyses.execute("https://teme-vasileoana22.c9users.io/IngredientAnalyses");
                                    getProducts.execute("https://teme-vasileoana22.c9users.io/products");

                                    Toast.makeText(RegisterActivity.this, "Inregistrare realizata cu succes!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Username sau email deja existente!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute(user);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Pentru a finaliza inregistrarea este necesara o conexiune la Internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


}
