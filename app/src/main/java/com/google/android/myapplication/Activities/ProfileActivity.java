package com.google.android.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    ProductAnalysisMethods productAnalysis=null;
    List<ProductAnalysis> productAnalysisList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        productAnalysis=new ProductAnalysisMethods();
        productAnalysisList=productAnalysis.select();
        for(ProductAnalysis productAnalysis : productAnalysisList)
        {
            Toast.makeText(this, productAnalysis.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
