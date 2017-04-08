package com.google.android.myapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AnalysesActivity extends AppCompatActivity {

    ListView lvMyAnalyses;
    ProductMethods productMethods;
    List<Product> productList;
    List<String> produsConcatenat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyses);
        lvMyAnalyses= (ListView) findViewById(R.id.lvMyAnalyses);
        productMethods=new ProductMethods();
        produsConcatenat=new ArrayList<>();
        int id=getIntent().getExtras().getInt("userId");
        productList=productMethods.selectProductsByUser(id);
        for(Product p:productList){
            produsConcatenat.add(p.getDescription()+"   "+p.getBrand());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,produsConcatenat);
        lvMyAnalyses.setAdapter(adapter);
    }
}
