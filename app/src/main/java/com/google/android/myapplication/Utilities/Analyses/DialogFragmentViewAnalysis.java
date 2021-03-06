package com.google.android.myapplication.Utilities.Analyses;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.myapplication.Activities.AnalysesActivity;
import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListIngredients.*;
import com.google.android.myapplication.Utilities.SearchIngredient.DialogFragmentViewIngredient;
import com.google.android.myapplication.Utilities.SearchProduct.ProductsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Oana on 08-Apr-17.
 */

public class DialogFragmentViewAnalysis extends DialogFragment {

    ListView lv_analyses_ingredients;
    TextView tvAnalysysCategoryName,tvAnalysysProductFunction,tvAnalysysProductDescription,tvAnalysysBrandName,tvAnalysysDate;
    ProductMethods productMethods;
    List<Product> productList;
    public static List<Ingredient> ingredients;
    CategoryMethods categoryMethods;
    IngredientMethods ingredientMethods;
    ProductAnalysisMethods productAnalysisMethods;
    Button btnOK;
    Product p;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.analyses_dialog_fragment, container, false);
        getDialog().setTitle("Vizualizeaza analiza!");
        ingredientMethods=new IngredientMethods();
        productMethods=new ProductMethods();
        productAnalysisMethods=new ProductAnalysisMethods();
        ingredients=new ArrayList<>();
        int id=getActivity().getIntent().getExtras().getInt("userId");
        String clasa= (String) getArguments().get("clasa");

        if(clasa.equals(AnalysesActivity.class.getSimpleName()))
        {
            productList=productMethods.selectProductsByUser(id);

        }
        else {
            productList= ProductsFragment.arrayList;

        }

        categoryMethods=new CategoryMethods();
        lv_analyses_ingredients= (ListView) rootView.findViewById(R.id.lv_analyses_ingredients);
        tvAnalysysCategoryName= (TextView) rootView.findViewById(R.id.tvAnalysysCategoryName);
        tvAnalysysProductFunction= (TextView) rootView.findViewById(R.id.tvAnalysysProductFunction);
        tvAnalysysProductDescription=(TextView) rootView.findViewById(R.id.tvAnalysysProductDescription);
        tvAnalysysBrandName=(TextView) rootView.findViewById(R.id.tvAnalysysBrandName);
        tvAnalysysDate=(TextView) rootView.findViewById(R.id.tvAnalysysDate);
        btnOK= (Button) rootView.findViewById(R.id.btnOK);

        int position= (int) getArguments().get("position");
         p=productList.get(position);
        ingredients=ingredientMethods.selectIngredients(p.getIdProduct());
        tvAnalysysCategoryName.setText(categoryMethods.getCategoryName(p.getIdCategory()));
        tvAnalysysBrandName.setText(p.getBrand());
        tvAnalysysProductFunction.setText(p.getFunction());
        tvAnalysysProductDescription.setText(p.getDescription());
        int id2 = p.getIdProduct();
        String date =productAnalysisMethods.getDate(id2);
        tvAnalysysDate.setText(date);

        com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter adapter = new com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter(getActivity().getApplicationContext(), R.layout.search_ingredients_adapter, ingredients);

           lv_analyses_ingredients.setAdapter(adapter);

        lv_analyses_ingredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentViewIngredient dialogFragmentViewIngredient = new DialogFragmentViewIngredient();
                Bundle bundle=new Bundle();
                bundle.putString("from",DialogFragmentViewAnalysis.class.getSimpleName());
                bundle.putInt("poz",i);
                dialogFragmentViewIngredient.setArguments(bundle);
                dialogFragmentViewIngredient.show(fragmentManager,"ViewIngredient");

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return rootView;
    }
}
