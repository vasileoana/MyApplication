package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.myapplication.R;

public class MainActivity extends AppCompatActivity {

    TextView tvRegister,tvLogin,tvApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            public void onClick(View v) {Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                startActivity(intent);

            }
        });

    }

}
