package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Rating;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvRegister,tvLogin,tvApp;
    RatingMethods ratingMethods;
    List<Rating> ratingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ratingMethods=new RatingMethods();
        ratingList=ratingMethods.select();
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
               intent.putExtra("tipUtilizator","anonim");
                for(Rating r:ratingList)
                    Toast.makeText(MainActivity.this, r.toString(), Toast.LENGTH_SHORT).show();
                //startActivity(intent);

            }
        });

    }

}
