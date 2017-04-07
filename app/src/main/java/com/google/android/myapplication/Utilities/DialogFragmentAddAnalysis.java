package com.google.android.myapplication.Utilities;

import android.app.DialogFragment;
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

import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Category;
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
    Button btnOk = null;
    EditText etBrand = null;
    EditText etDescription = null;
    Spinner spinnerCategoriy = null;
    Product product=null;
    ProductAnalysis productAnalysis=null;
    CategoryMethods categoryMethods=null;
    ProductMethods productMethods=null;
    ProductAnalysisMethods productAnalysisMethods=null;
    int idCategory=0;
    List<String> ingredients;

    public DialogFragmentAddAnalysis() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_fragment_add_analysis, container, false);
        getDialog().setTitle("Add analysis!");
        categoryMethods=new CategoryMethods();
        productMethods=new ProductMethods();
        productAnalysisMethods=new ProductAnalysisMethods();
        etBrand = (EditText) rootView.findViewById(R.id.etBrand);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        spinnerCategoriy = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        List<String> categories= categoryMethods.selectCategories();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,categories);
        spinnerCategoriy.setAdapter(adapter);

        btnOk = (Button) rootView.findViewById(R.id.btnAddAnalysis);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo validari

                String brand=etBrand.getText().toString();
                String description=etDescription.getText().toString();
                String category=spinnerCategoriy.getSelectedItem().toString();
                idCategory=categoryMethods.getIdCategory(category);
                product=new Product(description,brand,idCategory);
                productMethods.insert(product);
                int idProdus=productMethods.getIdProduct(product);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                int idUser= (int) getActivity().getIntent().getExtras().get("userId");
                productAnalysis=new ProductAnalysis(idProdus,idUser,date);
                productAnalysisMethods.insert(productAnalysis);
                Toast.makeText(getActivity().getApplicationContext(), "Analiza inserata cu succes!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return rootView;
    }
}
