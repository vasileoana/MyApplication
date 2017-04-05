package com.google.android.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ListIngredientsActivity extends AppCompatActivity {

    ArrayList<String> ingredients;
    List<Ingredient> ingredientsBD;
    IngredientMethods ingredientMethods;
    ListView lv;
    List<String> bdIng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        ingredients=getIntent().getExtras().getStringArrayList("list");
        ingredientsBD=new ArrayList<>();
        ingredientMethods=new IngredientMethods();
        lv= (ListView) findViewById(R.id.lvIng);
        bdIng=new ArrayList<>();
        for(String ing: ingredients) {
            ingredientsBD.addAll(ingredientMethods.selectIngredients(ing));
        }
            for(Ingredient i:ingredientsBD)
        {
            bdIng.add(i.getName());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,bdIng);
        lv.setAdapter(adapter);
    }
}
