package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView tvProfileUsername, tvProfileEmail;
    EditText etUsername, etPass, etPass2, etEmail;
    String username, pass, email;
    User user;
    UserMethods userMethods;
    Button btnSalvare, btnEditare;
    int idUser;
    User utilizator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        idUser = getIntent().getExtras().getInt("idUtilizator");
        userMethods = new UserMethods();
        utilizator = userMethods.selectUserById(idUser);

        tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        tilPass = (TextInputLayout) findViewById(R.id.tilPassword);
        tilPass2 = (TextInputLayout) findViewById(R.id.tilPassword2);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);

        tvProfileUsername = (TextView) findViewById(R.id.tvProfileUsername);
        tvProfileEmail = (TextView) findViewById(R.id.tvProfileEmail);
        tvProfileEmail.setText("Email: " + utilizator.getEmail());
        tvProfileUsername.setText("Utilizator: " + utilizator.getUsername());

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);
        etPass2 = (EditText) findViewById(R.id.etPassword2);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btnSalvare = (Button) findViewById(R.id.btnSalvare);
        btnEditare = (Button) findViewById(R.id.btnModificare);

        btnEditare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvProfileUsername.setVisibility(View.GONE);
                tvProfileEmail.setVisibility(View.GONE);

                tilUsername.setVisibility(View.VISIBLE);
                tilPass.setVisibility(View.VISIBLE);
                tilPass2.setVisibility(View.VISIBLE);
                tilEmail.setVisibility(View.VISIBLE);
                etUsername.setVisibility(View.VISIBLE);
                etPass.setVisibility(View.VISIBLE);
                etPass2.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                btnEditare.setVisibility(View.GONE);
                btnSalvare.setVisibility(View.VISIBLE);
                etUsername.setText(utilizator.getUsername());
                etEmail.setText(utilizator.getEmail());
            }
        });

        btnSalvare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().trim().length() <= 0) {
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

                    user = new User(username, pass, idUser, email);

                    if (userMethods.update(user) > 0) {
                        Toast.makeText(ProfileActivity.this, "Datele au fost modificate cu succes!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                        i.putExtra("tipUtilizator","logat");
                        i.putExtra("userId",idUser);

                        startActivity(i);
                    } else if (username.equals(utilizator.getUsername()) || email.equals(utilizator.getEmail())) {
                        Toast.makeText(ProfileActivity.this, "Datele au fost modificate cu succes!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                        i.putExtra("tipUtilizator","logat");
                        i.putExtra("userId",idUser);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(ProfileActivity.this, "Nume de utilizator sau email deja existente!Introduceti alte date!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}

