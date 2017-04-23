package com.google.android.myapplication.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;
import com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter;
import com.google.android.myapplication.Utilities.SearchIngredient.DialogFragmentViewIngredient;

import java.util.ArrayList;
import java.util.List;

public class ListIngredientsActivity extends AppCompatActivity {

    public static List<Ingredient> ingredientsBD;
    ArrayList<String> ingredients;
    IngredientMethods ingredientMethods;
    ListView lv;
    List<String> bdIng;
    Button btnSaveAnalysis;
    ProductMethods productMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        btnSaveAnalysis = (Button) findViewById(R.id.btnSaveAnalysis);
        ingredients = getIntent().getExtras().getStringArrayList("list");
        ingredientsBD = new ArrayList<>();
        ingredientMethods = new IngredientMethods();
        productMethods=new ProductMethods();
        lv = (ListView) findViewById(R.id.lvIng);
        bdIng = new ArrayList<>();
        for (String ing : ingredients) {
            ingredientsBD.addAll(ingredientMethods.selectIngredients(ing));
        }
        ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), R.layout.search_ingredients_adapter, ingredientsBD);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentViewIngredient dialogFragment = new DialogFragmentViewIngredient();
                Bundle bundle = new Bundle();
                bundle.putInt("poz",position);
                bundle.putString("from",ListIngredientsActivity.class.getSimpleName());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "Ingredient Details");

            }
        });
      /*  Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        int idUser = 0;
        if (bundle != null) {
            idUser = bundle.getInt("userId");
        }
*/

        btnSaveAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentAddAnalysis dialogFragment = new DialogFragmentAddAnalysis();
                Bundle bundle=new Bundle();
                bundle.putString("operatie","adaugare");
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "IngredientsFragment Manager");

            }
        });
    }
}
