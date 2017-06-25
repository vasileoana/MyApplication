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
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.Category;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.DataBase.Rest.GetIngredientAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProducts;
import com.google.android.myapplication.DataBase.Rest.GetUser;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Register.CheckInternetConnection;
import com.google.android.myapplication.Utilities.Register.ReadCategories;
import com.google.android.myapplication.Utilities.Register.ReadIngredients;
import com.google.android.myapplication.Utilities.Register.ReadRatings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    UserMethods userMethods;
    String username, password;
    User user;
    EditText etUsernameLogin, etPasswordLogin;
    TextInputLayout tilUsernameLogin, tilPasswordLogin;
    CategoryMethods categoryMethods;
    RatingMethods ratingMethods;
    List<Category> categoryList;
    ReadRatings readRatings;
    ReadIngredients readIngredients;
    ReadCategories readCategories;
    CheckInternetConnection checkInternetConnection;
    GetUser getUser;
    GetProducts getProducts = new GetProducts();
    GetIngredientAnalyses getIngredientAnalyses = new GetIngredientAnalyses();
    GetProductAnalyses getProductAnalyses = new GetProductAnalyses();
    User user_gasit = null;
    ProductMethods productMethods;
    IngredientAnalysisMethods ingredientAnalysisMethods;
    ProductAnalysisMethods productAnalysisMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        categoryMethods = new CategoryMethods();
        productMethods = new ProductMethods();
        ingredientAnalysisMethods = new IngredientAnalysisMethods();
        productAnalysisMethods = new ProductAnalysisMethods();
        checkInternetConnection = new CheckInternetConnection();
        categoryList = new ArrayList<>();
        categoryList = categoryMethods.select();
        user = new User();
        userMethods = new UserMethods();
        ratingMethods = new RatingMethods();
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        etUsernameLogin = (EditText) findViewById(R.id.etUsernameLogin);
        tilUsernameLogin = (TextInputLayout) findViewById(R.id.tilUserLogin);
        tilPasswordLogin = (TextInputLayout) findViewById(R.id.tilPassLogin);

        readIngredients = new ReadIngredients();
        readRatings = new ReadRatings();
        readCategories = new ReadCategories();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsernameLogin.getText().toString().trim().length() <= 0) {
                    etUsernameLogin.requestFocus();
                    tilUsernameLogin.setError("Scrieti numele de utilizator!");
                } else if (etPasswordLogin.getText().toString().length() < 6) {
                    tilUsernameLogin.setError(null);
                    etPasswordLogin.requestFocus();
                    tilPasswordLogin.setError("Scrieti parola dvs!");
                } else {
                    username = etUsernameLogin.getText().toString();
                    password = etPasswordLogin.getText().toString();

                    user = userMethods.selectUser(username, password);
                    if (user == null) {
                        if (!checkInternetConnection.isNetworkAvailable(getApplicationContext())) {
                            Toast.makeText(LoginActivity.this, "Daca aveti deja cont creat, activati internetul pentru a prelua datele!", Toast.LENGTH_SHORT).show();

                        } else {
                            URL url = null;
                            try {
                                url = new URL("https://teme-vasileoana22.c9users.io/users/" + username + "/" + password);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            getUser = (GetUser) new GetUser() {
                                @Override
                                protected void onPostExecute(User user) {
                                    user_gasit = user;
                                }
                            }.execute(url);

                            if (user_gasit != null) {
                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                intent.putExtra("userId", user_gasit.getIdUser());
                                intent.putExtra("tipUtilizator", "logat");
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Nume de utilizator sau parola incorecte!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (categoryList.size() == 0) {
                            AssetManager assetManager = getAssets();
                            readIngredients.execute(getAssets());
                            readRatings.readRatings(assetManager, ratingMethods);
                            readCategories.readCategories(assetManager, categoryMethods);
                        }

                        if (checkInternetConnection.isNetworkAvailable(getApplicationContext())) {
                            //sincronizare bd cu tabele de pe server
                            productMethods.delete();
                            productAnalysisMethods.delete();
                            ingredientAnalysisMethods.delete();
                            getProductAnalyses.execute("https://teme-vasileoana22.c9users.io/ProductAnalyses");
                            getIngredientAnalyses.execute("https://teme-vasileoana22.c9users.io/IngredientAnalyses");
                            getProducts.execute("https://teme-vasileoana22.c9users.io/products");
                        }
                        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                        intent.putExtra("userId", user.getIdUser());
                        intent.putExtra("tipUtilizator", "logat");
                        startActivity(intent);
                    }
                }

            }
        });
    }
}
