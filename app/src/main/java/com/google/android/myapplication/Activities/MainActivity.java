package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Files.ReadCategories;
import com.google.android.myapplication.DataBase.Files.ReadIngredients;
import com.google.android.myapplication.DataBase.Files.ReadRatings;
import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Category;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView tvRegister,tvLogin,tvApp;
    List<Category> categoryList;
    CategoryMethods categoryMethods;
    RatingMethods ratingMethods;
    ReadIngredients readIngredients;
    ReadCategories readCategories;
    ReadRatings readRatings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readCategories = new ReadCategories();
        readIngredients = new ReadIngredients();
        readRatings = new ReadRatings();
        ratingMethods = new RatingMethods();
        categoryMethods = new CategoryMethods();
        categoryList = new ArrayList<>();
        categoryList = categoryMethods.select();
        tvRegister= (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvLogin= (TextView) findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);

                startActivity(intent);
            }
        });

        tvApp= (TextView) findViewById(R.id.tvApp);
        tvApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Intent intent=new Intent(getApplicationContext(),OcrActivity.class);
               intent.putExtra("tipUtilizator","anonim");
                if (categoryList.size() == 0) {
                    AssetManager assetManager = getAssets();
                    readIngredients.execute(getAssets());
                    readRatings.readRatings(assetManager, ratingMethods);
                    readCategories.readCategories(assetManager, categoryMethods);
                }
                startActivity(intent);

            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
