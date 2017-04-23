package com.google.android.myapplication.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;
import com.google.android.myapplication.Utilities.Analyses.ListViewAdapter;
import com.google.android.myapplication.Utilities.SearchIngredient.ViewPagerAdapter;
import com.google.android.myapplication.Utilities.SearchProduct.ProductsFragment;

import java.util.List;

public class SearchProductActivity extends AppCompatActivity {

    ListView lvSearchProduct;
    ProductMethods productMethods;
    List<Product> productList;
    ProductAnalysisMethods productAnalysisMethods;
    ListViewAdapter adapter;
    int id;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        lvSearchProduct = (ListView) findViewById(R.id.lvSearchProducts);
        productMethods = new ProductMethods();
        productAnalysisMethods = new ProductAnalysisMethods();
        id = getIntent().getExtras().getInt("userId");
        productList = productMethods.selectAllProductsExceptUsers(id);
        adapter = new ListViewAdapter(getApplicationContext(), R.layout.analyses_adapter, productList);
        lvSearchProduct.setAdapter(adapter);

        lvSearchProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentViewAnalysis dialogFragment = new DialogFragmentViewAnalysis();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "Analysis Details");
            }
        });
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        android.support.v4.app.Fragment fragment = new ProductsFragment();
        adapter.addFrag(fragment);
        viewPager.setAdapter(adapter);
    }
}
