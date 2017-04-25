package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.Category;
import com.google.android.myapplication.DataBase.Model.Rating;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Register.ReadIngredients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegisterActivity extends AppCompatActivity {

     TextInputLayout tilUsername, tilPass, tilPass2, tilEmail;
     EditText etUsername, etPass, etPass2, etEmail;
     String username, pass, email;
     User user;
     UserMethods userMethods;
     IngredientMethods ingredientMethod;
     RatingMethods ratingMethods;
     CategoryMethods categoryMethods;

    ReadIngredients readIngredients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userMethods=new UserMethods();
        ingredientMethod=new IngredientMethods();
        ratingMethods=new RatingMethods();
        categoryMethods=new CategoryMethods();
        readIngredients=new ReadIngredients();
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
                if (etUsername.getText().toString().trim().length() <= 0) {
                    etUsername.requestFocus();
                    tilUsername.setError("An username is necessary!");
                } else if (etPass.getText().toString().length() < 6) {
                    tilUsername.setError(null);
                    etPass.requestFocus();
                    tilPass.setError("Please type a password with 6 characters!");
                } else if (etPass2.getText().toString().compareTo(etPass.getText().toString()) != 0) {
                    tilPass.setError(null);
                    etPass2.requestFocus();
                    tilPass2.setError("The password must be the same in both fields!");
                } else if (etEmail.getText().toString().trim().length() <= 2 || !etEmail.getText().toString().contains("@")) {
                    tilPass2.setError(null);
                    etEmail.requestFocus();
                    tilEmail.setError("Type a valid email!");
                } else {

                    tilEmail.setError(null);
                    username=etUsername.getText().toString().trim();
                    pass=etPass.getText().toString().trim();
                    email=etEmail.getText().toString().trim();
                    user = new User(username,pass,email);

                   if( userMethods.insert(user)>0) {
                       readIngredients.execute(getAssets());
                       readRatings();
                       readCategories();
                       Toast.makeText(RegisterActivity.this, "You have successfully registered!", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                       startActivity(intent);
                   }
                   else {
                       Toast.makeText(RegisterActivity.this, "Username sau email deja existente! Introduceti atle date!", Toast.LENGTH_SHORT).show();
                   }
                   }
            }
        });


    }


    public void readRatings(){
        AssetManager assetManager=getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;

        try {
            inputStream=assetManager.open("ratings.csv");
            inputStreamReader=new InputStreamReader(inputStream);
            reader=new BufferedReader(inputStreamReader);
            String line;
            Rating rating=new Rating();
            while((line = reader.readLine()) != null) {
                String[] entry = line.split(",");
                String ratingName = entry[0];
                int idRating = Integer.parseInt(entry[1]);
                rating.setRating(ratingName);
                rating.setIdRating(idRating);
                ratingMethods.insert(rating);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCategories(){
        AssetManager assetManager=getAssets();
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;

        try {
            inputStream=assetManager.open("categories.csv");
            inputStreamReader=new InputStreamReader(inputStream);
            reader=new BufferedReader(inputStreamReader);
            String line;
            Category category=new Category();
            while((line = reader.readLine()) != null) {
                String[] entry = line.split(",");
                String categoryName = entry[1];
                int idCategory = Integer.parseInt(entry[0]);
                if(entry.length>2)
                {
                    int idParent=Integer.parseInt(entry[2]);
                    category.setIdParent(idParent);
                }
                category.setIdCategory(idCategory);
                category.setCategoryName(categoryName);
                categoryMethods.insert(category);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
