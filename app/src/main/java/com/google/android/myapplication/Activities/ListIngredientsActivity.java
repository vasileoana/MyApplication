package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;
import com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListIngredientsActivity extends AppCompatActivity {

    public static List<Ingredient> ingredientsBD;
    ArrayList<String> ingredients;
    IngredientMethods ingredientMethods;
    ListView lv;
    List<String> bdIng;
    Button btnSaveAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        btnSaveAnalysis = (Button) findViewById(R.id.btnSaveAnalysis);
        ingredients = getIntent().getExtras().getStringArrayList("list");
        ingredientsBD = new ArrayList<>();
        ingredientMethods = new IngredientMethods();
        lv = (ListView) findViewById(R.id.lvIng);
        bdIng = new ArrayList<>();
        for (String ing : ingredients) {
            ingredientsBD.addAll(ingredientMethods.selectIngredients(ing));
        }
        ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), R.layout.search_ingredients_adapter, ingredientsBD);
        lv.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int idUser = 0;
        if (bundle != null) {
            idUser = bundle.getInt("userId");
        }

        btnSaveAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentAddAnalysis dialogFragment = new DialogFragmentAddAnalysis();
                dialogFragment.show(fragmentManager, "IngredientsFragment Manager");
            }
        });
    }
}
