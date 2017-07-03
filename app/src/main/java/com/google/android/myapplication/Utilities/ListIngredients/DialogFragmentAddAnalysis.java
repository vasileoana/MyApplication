package com.google.android.myapplication.Utilities.ListIngredients;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import com.google.android.myapplication.DataBase.Methods.SyncProdusMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Model.IngredientAnalysis;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.DataBase.Model.SyncProdus;
import com.google.android.myapplication.DataBase.Rest.GetIngredientAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.GetProducts;
import com.google.android.myapplication.DataBase.Rest.PostIngredientAnalysis;
import com.google.android.myapplication.DataBase.Rest.PostProduct;
import com.google.android.myapplication.DataBase.Rest.PostProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.PutProduct;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Register.CheckInternetConnection;

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
    Product p;
    PostIngredientAnalysis postIngredientAnalysis;
    PostProductAnalyses postProductAnalyses;
    PostProduct postProd;
    SyncProdusMethods syncProdusMethods;
    TextInputLayout tilFunction, tilDescription, tilBrand;
    String date;
    int idUser;
    AnalysesActivity activitate;
    CheckInternetConnection checkInternetConnection;
    public static Context context ;
    public DialogFragmentAddAnalysis() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.list_ingredients_dialog_fragment_add_analysis, container, false);
        getDialog().setTitle("Adauga analiza!");
      context= getActivity().getApplicationContext();
        syncProdusMethods = new SyncProdusMethods();
        checkInternetConnection = new CheckInternetConnection();
        categoryMethods = new CategoryMethods();
        productMethods = new ProductMethods();
        ingredientAnalysisMethods = new IngredientAnalysisMethods();
        productAnalysisMethods = new ProductAnalysisMethods();
        ingredientAnalysis = new IngredientAnalysis();
        etBrand = (EditText) rootView.findViewById(R.id.etBrand);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        etFunction = (EditText) rootView.findViewById(R.id.etFunction);
        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        tilFunction = (TextInputLayout) rootView.findViewById(R.id.tilFunction);
        tilBrand = (TextInputLayout) rootView.findViewById(R.id.tilBrand);
        tilDescription = (TextInputLayout) rootView.findViewById(R.id.tilDescription);

        List<String> categories = categoryMethods.selectCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, categories);
        spinnerCategory.setAdapter(adapter);

        Bundle bundle = this.getArguments();
        String operatie = bundle.getString("operatie");
        if (operatie.equals("editare")) {

            int id = bundle.getInt("id");
            p = productMethods.selectProductById(id);
            etDescription.setText(p.getDescription());
            etFunction.setText(p.getFunction());
            etBrand.setText(p.getBrand());
            spinnerCategory.setSelection(p.getIdCategory()-1);

            btnOk = (Button) rootView.findViewById(R.id.btnAddAnalysis);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etDescription.getText().toString().trim().length() == 0) {
                        etDescription.requestFocus();
                        tilDescription.setError("Introduceti descrierea produsului de pe eticheta!");
                    } else if (etBrand.getText().toString().trim().length() == 0) {
                        tilDescription.setError(null);
                        etBrand.requestFocus();
                        tilBrand.setError("Introduceti marca produsului!");
                    } else if (etFunction.getText().toString().trim().length() == 0) {
                        tilBrand.setError(null);
                        etFunction.requestFocus();
                        tilFunction.setError("Introduceti functia produsului!");
                    } else {
                        tilFunction.setError("null");
                        String brand = etBrand.getText().toString();
                        String description = etDescription.getText().toString();
                        String category = spinnerCategory.getSelectedItem().toString();
                        String function = etFunction.getText().toString();
                        idCategory = categoryMethods.getIdCategory(category);
                        p.setFunction(function);
                        p.setBrand(brand);
                        p.setIdCategory(idCategory);
                        p.setDescription(description);
                        if (checkInternetConnection.isNetworkAvailable(getActivity().getApplication().getApplicationContext())) {
                            PutProduct putProduct = new PutProduct();
                            putProduct.execute(p);

                        } else {
                            syncProdusMethods.insert(new SyncProdus(p.getIdProduct(), true, false));
                        }

                        productMethods.update(p);
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Analiza actualizata!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            });
        } else if (operatie.equals("adaugare")) {
            btnOk = (Button) rootView.findViewById(R.id.btnAddAnalysis);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etDescription.getText().toString().trim().length() == 0) {
                        etDescription.requestFocus();
                        tilDescription.setError("Introduceti descrierea produsului de pe eticheta!");
                    } else if (etBrand.getText().toString().trim().length() == 0) {
                        tilDescription.setError(null);
                        etBrand.requestFocus();
                        tilBrand.setError("Introduceti marca produsului!");
                    } else if (etFunction.getText().toString().trim().length() == 0) {
                        tilBrand.setError(null);
                        etFunction.requestFocus();
                        tilFunction.setError("Introduceti functia produsului!");
                    } else {
                        if (checkInternetConnection.isNetworkAvailable(getActivity().getApplicationContext())) {
                            idUser = (int) getActivity().getIntent().getExtras().get("userId");

                            String brand = etBrand.getText().toString();
                            String description = etDescription.getText().toString();
                            String category = spinnerCategory.getSelectedItem().toString();
                            String function = etFunction.getText().toString();
                            idCategory = categoryMethods.getIdCategory(category);
                            product = new Product(description, brand, idCategory, function);
                            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());



                            postProd = (PostProduct) new PostProduct() {

                                @Override
                                protected void onPostExecute(Product product) {
                                    super.onPostExecute(product);
                                    for (Ingredient i : ListIngredientsActivity.ingredientsBD) {

                                        IngredientAnalysis ingredientAnalysis = new IngredientAnalysis(product.getIdProduct(), i.getIdIngredient());
                                        postIngredientAnalysis = (PostIngredientAnalysis) new PostIngredientAnalysis().execute(ingredientAnalysis);
                                    }
                                    ProductAnalysis productAnalysis = new ProductAnalysis(product.getIdProduct(), idUser, date);
                                    postProductAnalyses = (PostProductAnalyses) new PostProductAnalyses() {
                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);
                                            Toast.makeText(getActivity().getApplicationContext(), "Analiza inserata cu succes!", Toast.LENGTH_SHORT).show();
                                            if(PostProduct.dialog != null && PostProduct.dialog.isShowing()){
                                                PostProduct.dialog.dismiss();
                                            }

                                            dismiss();
                                        }
                                    }.execute(productAnalysis);


                                }
                            }.execute(product);

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Pentru a finaliza operatiunea este necesara conexiunea la retea!", Toast.LENGTH_SHORT).show();
                        }
                    }
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}