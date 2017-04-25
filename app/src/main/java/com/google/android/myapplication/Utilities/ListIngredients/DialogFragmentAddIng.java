package com.google.android.myapplication.Utilities.ListIngredients;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.myapplication.R.layout.add_ingredients_dialog_fragment;

/**
 * Created by Oana on 25-Apr-17.
 */

public class DialogFragmentAddIng extends DialogFragment {

    AutoCompleteTextView AutoCompleteTextView1;
    Button btnAdaugaIngredientul, btnRenunta;
    IngredientMethods ingredientMethods;
    List<Ingredient> ingredientList;
    List<String> ingredientsName = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_ingredients_dialog_fragment, container, false);
        getDialog().setTitle("Adauga ingredient!");
        AutoCompleteTextView1 = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextView1);
        btnAdaugaIngredientul = (Button) rootView.findViewById(R.id.btnAdaugaIngredientul);
        btnRenunta = (Button) rootView.findViewById(R.id.btnRenunta);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ingredientsName);

        AutoCompleteTextView1.setAdapter(adapter);
        AutoCompleteTextView1.setThreshold(1);
        AutoCompleteTextView1.setDropDownBackgroundResource(R.color.colorPrimary);
        btnAdaugaIngredientul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ingredient ingredient = ingredientMethods.selectIngredient(AutoCompleteTextView1.getText().toString());
              boolean introdus=true;
                for (Ingredient ing : ListIngredientsActivity.ingredientsBD) {
                    if (ing.getName().equals("AutoCompleteTextView1.getText().toString()")) {
                        introdus = false;
                    }
                }
                    if(introdus){
                        ListIngredientsActivity.ingredientsBD.add(ingredient);
                        Toast.makeText(getActivity().getApplicationContext(), "Ingredient inserat cu succes", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Ingredient existent in lista sau nu se regaseste in bd.", Toast.LENGTH_SHORT).show();

                    }
                    dismiss();


            }
        });

        btnRenunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

}
