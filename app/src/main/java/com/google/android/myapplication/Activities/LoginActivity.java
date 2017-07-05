package com.google.android.myapplication.Activities;

import android.content.Context;
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
import com.google.android.myapplication.DataBase.Methods.SyncProdusMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.Category;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.DataBase.Rest.DeleteSyncProduct;
import com.google.android.myapplication.DataBase.Rest.GetIngredientAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProducts;
import com.google.android.myapplication.DataBase.Rest.GetUser;
import com.google.android.myapplication.DataBase.Rest.IsServerOpen;
import com.google.android.myapplication.DataBase.Rest.PutSyncProduct;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Register.CheckInternetConnection;
import com.google.android.myapplication.DataBase.Files.ReadCategories;
import com.google.android.myapplication.DataBase.Files.ReadIngredients;
import com.google.android.myapplication.DataBase.Files.ReadRatings;

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
    SyncProdusMethods syncProdusMethods;
    List<Integer> listaEditate;
    List<Integer> listaSterse;
    PutSyncProduct putSyncProduct;
    DeleteSyncProduct deleteSyncProduct;
    IsServerOpen isServerOpen;
    String raspunsServer;
    public static Context context;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        listaEditate = new ArrayList<>();
        context = LoginActivity.this;
        listaSterse = new ArrayList<>();
        categoryMethods = new CategoryMethods();
        syncProdusMethods = new SyncProdusMethods();
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
        if (checkInternetConnection.isNetworkAvailable(getApplicationContext())) {
            isServerOpen = (IsServerOpen) new IsServerOpen() {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    raspunsServer = s;
                }
            }.execute();
        }
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
                                        protected void onPostExecute(User userReturnat) {
                                            user_gasit = userReturnat;
                                            if (user_gasit != null) {
                                                user = user_gasit;
                                                preluareDate();
                                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                                intent.putExtra("userId", user.getIdUser());
                                                intent.putExtra("tipUtilizator", "logat");
                                                startActivity(intent);
                                            }
                                            else if(raspunsServer.equals("5")){
                                                Toast.makeText(LoginActivity.this, "Eroare de server!", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginActivity.this, "Nume de utilizator sau parola incorecte!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }.execute(url);

                                }
                            } else {
                                preluareDate();
                                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                                intent.putExtra("userId", user.getIdUser());
                                intent.putExtra("tipUtilizator", "logat");
                                startActivity(intent);
                            }
                        }
            }
        });

    }
    public  void preluareDate(){
        if (categoryList.size() == 0) {
            AssetManager assetManager = getAssets();
            readIngredients.execute(getAssets());
            readRatings.readRatings(assetManager, ratingMethods);
            readCategories.readCategories(assetManager, categoryMethods);
        }

        if (checkInternetConnection.isNetworkAvailable(getApplicationContext())) {
            listaEditate = syncProdusMethods.selectEditate();
            listaSterse = syncProdusMethods.selectSterse();
            deleteSyncProduct = (DeleteSyncProduct) new DeleteSyncProduct() {
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    putSyncProduct = (PutSyncProduct) new PutSyncProduct() {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            isServerOpen = (IsServerOpen) new IsServerOpen() {
                                @Override
                                protected void onPostExecute(String s) {
                                    if (!s.equals("5")) {
                                        productMethods.delete();
                                        productAnalysisMethods.delete();
                                        ingredientAnalysisMethods.delete();
                                        getProductAnalyses.execute("https://teme-vasileoana22.c9users.io/ProductAnalyses");
                                        getIngredientAnalyses.execute("https://teme-vasileoana22.c9users.io/IngredientAnalyses");
                                        getProducts.execute("https://teme-vasileoana22.c9users.io/products");
                                        syncProdusMethods.delete();
                                    }
                                }
                            }.execute();


                        }
                    }.execute(listaEditate);
                }
            }.execute(listaSterse);


        }
    }
}


