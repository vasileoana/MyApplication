package com.google.android.myapplication.Activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListViewFragmentIngredients;
import com.google.android.myapplication.Utilities.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AnalysesActivity extends AppCompatActivity {

    ListView lvMyAnalyses;
    ProductMethods productMethods=null;
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
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,produsConcatenat);
        lvMyAnalyses.setAdapter(adapter);
    }
}
