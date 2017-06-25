package com.google.android.myapplication.Utilities.ListIngredients;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.myapplication.Activities.AnalysesActivity;
import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.IngredientAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.DataBase.Rest.GetIngredientAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProducts;
import com.google.android.myapplication.DataBase.Rest.Post;
import com.google.android.myapplication.DataBase.Rest.PostIngredientAnalysis;
import com.google.android.myapplication.DataBase.Rest.PostProduct;
import com.google.android.myapplication.DataBase.Rest.PostProductAnalyses;
import com.google.android.myapplication.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Oana on 07-Apr-17.
 */

public class DialogFragmentAddAnalysis extends DialogFragment {

    Button btnOk;
    EditText etBrand, etDescription, etFunction;
    Spinner spinnerCategory;
    Product product;
    ProductMethods productMethods;
    ProductAnalysis productAnalysis;
    ProductAnalysisMethods productAnalysisMethods;
    IngredientAnalysis ingredientAnalysis;
    IngredientAnalysisMethods ingredientAnalysisMethods;
    CategoryMethods categoryMethods;
    int idCategory;
    URL url = null;
    Product p;
    PostIngredientAnalysis postIngredientAnalysis;
    PostProductAnalyses postProductAnalyses;
    PostProduct postProd;
    int idProdus;
    String date;
    int idUser;
    AnalysesActivity activitate;

    public DialogFragmentAddAnalysis() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.list_ingredients_dialog_fragment_add_analysis, container, false);
        getDialog().setTitle("Adauga analiza!");

        categoryMethods = new CategoryMethods();
        productMethods = new ProductMethods();
        ingredientAnalysisMethods = new IngredientAnalysisMethods();
        productAnalysisMethods = new ProductAnalysisMethods();
        ingredientAnalysis = new IngredientAnalysis();
        etBrand = (EditText) rootView.findViewById(R.id.etBrand);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        etFunction = (EditText) rootView.findViewById(R.id.etFunction);
        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        List<String> categories = categoryMethods.selectCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, categories);
        spinnerCategory.setAdapter(adapter);

        Bundle bundle = this.getArguments();

        String operatie = bundle.getString("operatie");
//todo validari
        if (operatie.equals("editare")) {
            int id = bundle.getInt("id");
            p = productMethods.selectProductById(id);
            etDescription.setText(p.getDescription());
            etFunction.setText(p.getFunction());
            etBrand.setText(p.getBrand());
            //todo pentru spinner

            btnOk = (Button) rootView.findViewById(R.id.btnAddAnalysis);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo validari

                    String brand = etBrand.getText().toString();
                    String description = etDescription.getText().toString();
                    String category = spinnerCategory.getSelectedItem().toString();
                    String function = etFunction.getText().toString();
                    idCategory = categoryMethods.getIdCategory(category);
                    p.setFunction(function);
                    p.setBrand(brand);
                    p.setIdCategory(idCategory);
                    p.setDescription(description);
                    productMethods.update(p);

                    dismiss();
                }
            });
        } else if (operatie.equals("adaugare")) {
            btnOk = (Button) rootView.findViewById(R.id.btnAddAnalysis);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    idUser = (int) getActivity().getIntent().getExtras().get("userId");

                    //todo validari

                    String brand = etBrand.getText().toString();
                    String description = etDescription.getText().toString();
                    String category = spinnerCategory.getSelectedItem().toString();
                    String function = etFunction.getText().toString();
                    idCategory = categoryMethods.getIdCategory(category);
                    product = new Product(description, brand, idCategory, function);
                    date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    String link = "https://teme-vasileoana22.c9users.io/Products/" +
                            idCategory + "/" + description.replaceAll(" ", "%20") + "/" + function.replaceAll(" ", "%20") + "/" + brand.replaceAll(" ", "%20");
                    try {
                        url = new URL(link);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    postProd = (PostProduct) new PostProduct() {
                        @Override
                        protected void onPostExecute(Product product) {
                            super.onPostExecute(product);
                            for (Ingredient i : ListIngredientsActivity.ingredientsBD) {
                                String link3 = "https://teme-vasileoana22.c9users.io/IngredientAnalyses/" +
                                        product.getIdProduct() + "/" + i.getIdIngredient();
                                try {
                                    url = new URL(link3);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                postIngredientAnalysis = (PostIngredientAnalysis) new PostIngredientAnalysis().execute(url);
                            }
                            String link2 = "https://teme-vasileoana22.c9users.io/ProductAnalyses/" +
                                    product.getIdProduct() + "/" + idUser + "/" + date;
                            try {
                                url = new URL(link2);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            postProductAnalyses = (PostProductAnalyses) new PostProductAnalyses().execute(url);
                            Toast.makeText(getActivity().getApplicationContext(), "Analiza inserata cu succes!", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }.execute(url);

                }
            });
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activitate = (AnalysesActivity) activity;
        } catch (ClassCastException e) {
        }
    }

}