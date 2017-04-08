package com.google.android.myapplication.Utilities.ListIngredients;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Oana on 07-Apr-17.
 */

public class DialogFragmentAddAnalysis extends DialogFragment{

    Button btnOk;
    EditText etBrand, etDescription,etFunction;
    Spinner spinnerCategory;
    Product product;
    ProductMethods productMethods;
    ProductAnalysis productAnalysis;
    ProductAnalysisMethods productAnalysisMethods;
    IngredientAnalysis ingredientAnalysis;
    IngredientAnalysisMethods ingredientAnalysisMethods;
    CategoryMethods categoryMethods;
    int idCategory;

    public DialogFragmentAddAnalysis() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.list_ingredients_dialog_fragment_add_analysis, container, false);
        getDialog().setTitle("Add analysis!");
        categoryMethods=new CategoryMethods();
        productMethods=new ProductMethods();
        ingredientAnalysisMethods=new IngredientAnalysisMethods();
        productAnalysisMethods=new ProductAnalysisMethods();
        ingredientAnalysis=new IngredientAnalysis();
        etBrand = (EditText) rootView.findViewById(R.id.etBrand);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        etFunction= (EditText) rootView.findViewById(R.id.etFunction);
        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        List<String> categories= categoryMethods.selectCategories();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,categories);
        spinnerCategory.setAdapter(adapter);

        btnOk = (Button) rootView.findViewById(R.id.btnAddAnalysis);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo validari

                String brand=etBrand.getText().toString();
                String description=etDescription.getText().toString();
                String category=spinnerCategory.getSelectedItem().toString();
                String function=etFunction.getText().toString();
                idCategory=categoryMethods.getIdCategory(category);
                product=new Product(description,brand,idCategory,function);
                productMethods.insert(product);
                int idProdus=productMethods.getIdProduct(product);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int idUser= (int) getActivity().getIntent().getExtras().get("userId");
                productAnalysis=new ProductAnalysis(idProdus,idUser,date);
                productAnalysisMethods.insert(productAnalysis);

              for(Ingredient i: ListIngredientsActivity.ingredientsBD)
              {
                  ingredientAnalysis.setIdIngredient(i.getIdIngredient());
                  ingredientAnalysis.setIdProduct(idProdus);
                  ingredientAnalysisMethods.insert(ingredientAnalysis);
              }
                Toast.makeText(getActivity().getApplicationContext(), "Analiza inserata cu succes!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return rootView;
    }
}
