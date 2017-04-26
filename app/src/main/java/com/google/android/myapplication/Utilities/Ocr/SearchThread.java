package com.google.android.myapplication.Utilities.Ocr;

import android.os.AsyncTask;

import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 26-Apr-17.
 */

public class SearchThread extends AsyncTask<List<String>, Void, Void> {
    IngredientMethods ingredientMethods = new IngredientMethods();
    List<String> ingredientNameList = new ArrayList<>();
    List<Ingredient> ingredientList;
    List<Ingredient> ingredienteReturnate;
    List<String> ingredients;


    @Override
    protected Void doInBackground(List<String>... params) {
      ingredients = params[0];
        ListIngredientsActivity.ingredientsBD=new ArrayList<>();
        ingredientList = ingredientMethods.select();
        for (Ingredient i : ingredientList) {
            ingredientNameList.add(i.getName());
        }
        algoritmCautare();
        return null;
    }


    public void algoritmCautare() {

        for (int i = 0; i < ingredients.size(); i++) {
            String ing = ingredients.get(i);
            List<Ingredient> ingList = cautareCuvantBD(ing);
            int j = 0;
            if (ingList.size() > 1 && ingredientMethods.selectIngredient(ing) != null) {
                ListIngredientsActivity.ingredientsBD.add(ingredientMethods.selectIngredient(ing));
            } else {
                while (j < 4 && ingList.size() > 1 && i < ingredients.size() - 1) {
                    int poz = i + 1;
                    ing = ing + " " + ingredients.get(poz);
                    ingList = cautareCuvantBD(ing);
                    if (poz < ingredients.size()) {
                        j++;
                    } else {
                        j = 4;
                    }
                }
            }
        }
    }

    public List<Ingredient> cautareCuvantBD(String ingredient) {
        ingredienteReturnate = ingredientMethods.selectIngredients(ingredient);
        if (ingredienteReturnate.size() == 0) {
            recursivLevenstein(ingredient, 0.6);
        } else if (ingredienteReturnate.size() == 1) {
            if (!ListIngredientsActivity.ingredientsBD.contains(ingredienteReturnate.get(0)))
                ListIngredientsActivity.ingredientsBD.add(ingredienteReturnate.get(0));
        }
        return ingredienteReturnate;

    }


    public void recursivLevenstein(String ing, double fuzzy) {
        if (fuzzy > 0.4 && fuzzy <= 1) {
            //merge greu randu urm
            List<String> levenshtein = LevenshteinDistanceSearch.Search(ing, ingredientNameList, fuzzy);
            if (levenshtein.size() == 1) {
                String numeIng = levenshtein.get(0);
                if (!ListIngredientsActivity.ingredientsBD.contains((ingredientMethods.selectIngredient(numeIng))))
                    ListIngredientsActivity.ingredientsBD.add(ingredientMethods.selectIngredient(numeIng));
            } else if (levenshtein.size() < 1) {
                recursivLevenstein(ing, fuzzy - 0.1);
            } else if (levenshtein.size() > 1) {
                recursivLevenstein(ing, fuzzy + 0.1);
            }

        }
    }


}
