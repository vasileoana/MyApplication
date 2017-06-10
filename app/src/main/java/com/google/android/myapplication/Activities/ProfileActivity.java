package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextInputLayout tilUsername, tilPass, tilPass2, tilEmail;
    EditText etUsername, etPass, etPass2, etEmail;
    String username, pass, email;
    User user;
    UserMethods userMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userMethods = new UserMethods();

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
                    username = etUsername.getText().toString().trim();
                    pass = etPass.getText().toString().trim();
                    email = etEmail.getText().toString().trim();
                    user = new User(username, pass, email);

                    if (userMethods.update(user) > 0) {
                        Toast.makeText(ProfileActivity.this, "Datele au fost modificate cu succes!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Username sau email deja existente! Introduceti alte date!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

