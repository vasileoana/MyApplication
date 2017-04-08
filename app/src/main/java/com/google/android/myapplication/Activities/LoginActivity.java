package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.UserMethods;
import com.google.android.myapplication.DataBase.Model.User;
import com.google.android.myapplication.R;

public class LoginActivity extends AppCompatActivity {

     UserMethods userMethods;
     String username, password;
     User user;
     EditText etUsernameLogin, etPasswordLogin;
     TextInputLayout tilUsernameLogin, tilPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user=new User();
        userMethods=new UserMethods();

        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        etUsernameLogin = (EditText) findViewById(R.id.etUsernameLogin);
        tilUsernameLogin = (TextInputLayout) findViewById(R.id.tilUserLogin);
        tilPasswordLogin = (TextInputLayout) findViewById(R.id.tilPassLogin);


        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsernameLogin.getText().toString().trim().length() <= 0) {
                    etUsernameLogin.requestFocus();
                    tilUsernameLogin.setError("Plase write your username!");
                } else if (etPasswordLogin.getText().toString().length() < 6) {
                    tilUsernameLogin.setError(null);
                    etPasswordLogin.requestFocus();
                    tilPasswordLogin.setError("Please type your password!");
                } else {
                    username = etUsernameLogin.getText().toString();
                    password = etPasswordLogin.getText().toString();

                     user = userMethods.selectUser(username, password);
                    if (user == null) {
                        Toast.makeText(LoginActivity.this, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                       intent.putExtra("userId",user.getIdUser());
                        intent.putExtra("tipUtilizator","logat");
                        startActivity(intent);
                    }
                }

            }
        });
    }
}
