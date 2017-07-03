package com.google.android.myapplication.Utilities.Ocr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;

import java.util.List;

/**
 * Created by Oana on 03-Jul-17.
 */

public class OcrOnThread extends AsyncTask<Void, Void, Void> {
    public static ProgressDialog dialog = new ProgressDialog(ListIngredientsActivity.context);

    IngredientMethods ingredientMethods = new IngredientMethods();
    @Override
    protected void onPreExecute() {
        //set message of the dialog
        dialog.setMessage("Asteapta...");
        //show dialog
        if(!((Activity) ListIngredientsActivity.context).isFinishing())
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        algoritmCautare();
        return null;
    }



    public void algoritmCautare() {
//ingredients reprezinta vectorul preluat cu split din ceea ce a reusit sa transforme OCR-ul
        for (int i = 0; i < ListIngredientsActivity.ingredients.size(); i++) {
            //preluam pe rand cate un element
            String ing = ListIngredientsActivity.ingredients.get(i);
            //il cautam in baza de date asa cum este sau il cautam aplicand alg levenstein
            List<Ingredient> ingList = cautareCuvantBD(ing);
            int j = 0;
            Ingredient ingr = ingredientMethods.selectIngredient(ing);
            if (ingList.size() > 1 && ingr != null && ingr.getIdRating() != 0) {
                //vezi cazul glycerin si glycerine, in bd fiinc cu LIKE '%glycerin%'  mi se returneaza ambele
                ListIngredientsActivity.ingredientsBD.add(ingr);
            } else {
                while (j < 3 && ingList.size() > 1 && i < ListIngredientsActivity.ingredients.size() - 1) {
                    //ex am alcool si mi apar mai multe rezultate. incerc sa le combin cu stringurile vecine
                    int poz = i + 1;
                    ing = ing + " " + ListIngredientsActivity.ingredients.get(poz);
                    //il caut iarasi in bd dar de data asta concatenat cu cuv alaturat
                    ingList = cautareCuvantBD(ing);
                    if (poz < ListIngredientsActivity.ingredients.size() - 1) {
                        j++;
                    } else {
                        j = 3;
                    }
                }
            }
        }
    }

    public List<Ingredient> cautareCuvantBD(String ingredient) {
        //cauta toate ingredientele care contin cuvantul in ele, ex: avem mai multe ingrediente care au alcool in ele
        ListIngredientsActivity.ingredienteReturnate = ingredientMethods.selectIngredients(ingredient);
        if (ListIngredientsActivity.ingredienteReturnate.size() == 0) {
            //inseamna ca in bd nu se gaseste niciun rezultat si se incearca a se corecta inputul
            recursivLevenstein(ingredient, 0.7);
        } else if (ListIngredientsActivity.ingredienteReturnate.size() == 1) {
            //daca bd-ul a returnat 1 element o sa presupunem ca e fix cel cautat
            Ingredient ingr = ListIngredientsActivity.ingredienteReturnate.get(0);
            if (!ListIngredientsActivity.ingredientsBD.contains(ListIngredientsActivity.ingredienteReturnate.get(0)) && ingr != null && ingr.getIdRating() != 0) {
                ListIngredientsActivity.ingredientsBD.add(ingr);
            }
        }
        return ListIngredientsActivity.ingredienteReturnate;

    }


    public void recursivLevenstein(String ing, double fuzzy) {
        //un fuzzy mare inseamna ca dorim cu cat mai putine modficari inputul sa se potriveasca cu un anumit cuv din dictionar
        //un fuzzy mic mareste sansele de a returna mai multe ingrediente care se scriu asemanator, adica au cateva caractere in comun

        if (fuzzy >= 0.5 && fuzzy <= 1) {
            //merge greu randu urm
            List<String> levenshtein = LevenshteinDistanceSearch.Search(ing, ListIngredientsActivity.levenshteinList, fuzzy);
            if (levenshtein.size() == 1) {
                //daca a returnat doar un rezultat inseamna ca a gasit doar un ing asemanator
                String numeIng = levenshtein.get(0);
                //daca exista deja in lista de ingrediente pe care o vom baga in adapter la listview, nu il mai introducem iar
                Ingredient ingr = ingredientMethods.selectIngredient(numeIng);
                if (!ListIngredientsActivity.ingredientsBD.contains((ingredientMethods.selectIngredient(numeIng))) && ingr != null && ingr.getIdRating() != 0)
                    ListIngredientsActivity.ingredientsBD.add(ingr);
            } /*else if (levenshtein.size() == 0) {
                //daca s-au returnat 0 rezultate incercam sa micsoram fuzzines, in ideea in care poate ocr-ul a incurcat mai multe caractere
                recursivLevenstein(ing, fuzzy - 0.1);
            } */ else if (levenshtein.size() > 1) {
                //daca se returneaza mai mult de  1 rez marim fuzziness ul, pana se returneaza doar 1 rezultat care este cel potrivit noua
                ListIngredientsActivity.levenshteinList = levenshtein;
                recursivLevenstein(ing, fuzzy + 0.1);
            }

        }
    }
}
