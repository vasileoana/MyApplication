package com.google.android.myapplication.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListViewFragmentIngredients;
import com.google.android.myapplication.Utilities.ListViewIngredientsAdapter;
import com.google.android.myapplication.Utilities.ViewPagerAdapter;

import java.util.List;

public class SearchIngredientActivity extends AppCompatActivity {

    private List<Ingredient> ingredients;
    private IngredientMethods ingredientMethods;
    private ListView lwSearchIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ingredient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment fragment = new ListViewFragmentIngredients();
        adapter.addFrag(fragment, "ListView");

        viewPager.setAdapter(adapter);
    }
}

