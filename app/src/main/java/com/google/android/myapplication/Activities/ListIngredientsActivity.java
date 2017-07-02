package com.google.android.myapplication.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddIng;
import com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter;
import com.google.android.myapplication.Utilities.Ocr.LevenshteinDistanceSearch;
import com.google.android.myapplication.Utilities.Ocr.SearchThread;
import com.google.android.myapplication.Utilities.SearchIngredient.DialogFragmentViewIngredient;

import java.util.ArrayList;
import java.util.List;

public class ListIngredientsActivity extends AppCompatActivity {

    public static List<Ingredient> ingredientsBD;
    ArrayList<String> ingredients;
    IngredientMethods ingredientMethods;
    ListView lv;
    List<String> bdIng;
    ImageButton btnAddIng, btnRemoveIng;
    ImageButton btnSaveAnalysis;
    ProductMethods productMethods;
    ListViewAdapter adapter;
    List<Ingredient> ingredienteReturnate;
    List<Ingredient> ingredientList;
    List<String> ingredientNameList;
    List<String> levenshteinList;
    String tip_utilizator;
    int idUser;
    TextView addIng, removeIng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        btnSaveAnalysis = (ImageButton) findViewById(R.id.btnSave);
        btnAddIng = (ImageButton) findViewById(R.id.btnAddIng);
        tip_utilizator = getIntent().getExtras().getString("tipUtilizator");
        addIng = (TextView) findViewById(R.id.textView1);
        removeIng = (TextView) findViewById(R.id.textView2);
        if (tip_utilizator.equals("logat")) {
            idUser = (int) getIntent().getExtras().get("userId");
        } else {
            btnSaveAnalysis.setVisibility(View.GONE);
            btnRemoveIng.setVisibility(View.GONE);
            btnAddIng.setVisibility(View.GONE);
            addIng.setVisibility(View.GONE);
            removeIng.setVisibility(View.GONE);
        }

        ingredients = getIntent().getExtras().getStringArrayList("list");
        ingredientsBD = new ArrayList<>();
        ingredientMethods = new IngredientMethods();
        ingredientList = ingredientMethods.select();
        productMethods = new ProductMethods();
        lv = (ListView) findViewById(R.id.lvIng);
        bdIng = new ArrayList<>();
        ingredientNameList = new ArrayList<>();
        for (Ingredient i : ingredientList) {
            ingredientNameList.add(i.getName());
        }

        levenshteinList = ingredientNameList;

        algoritmCautare();
        adapter = new ListViewAdapter(getApplicationContext(), R.layout.search_ingredients_adapter, ingredientsBD);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentViewIngredient dialogFragmentViewIngredient = new DialogFragmentViewIngredient();
                Bundle bundle = new Bundle();
                bundle.putString("from", ListIngredientsActivity.class.getSimpleName());
                bundle.putInt("poz", i);
                dialogFragmentViewIngredient.setArguments(bundle);
                dialogFragmentViewIngredient.show(fragmentManager, "ViewIngredient");
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ListIngredientsActivity.this);
                builder1.setMessage("Sunteti sigur ca stergeti acest ingredient?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Da",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ingredientsBD.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        });

                builder1.setNegativeButton(
                        "Nu",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            }
        });
        btnSaveAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentAddAnalysis dialogFragment = new DialogFragmentAddAnalysis();
                Bundle bundle = new Bundle();
                bundle.putInt("userId", idUser);
                bundle.putString("operatie", "adaugare");
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "IngredientsFragment Manager");

            }
        });

        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentAddIng dialogFragmentAddIng = new DialogFragmentAddIng();
                dialogFragmentAddIng.show(fragmentManager, "Adauga ingredient");
                adapter.notifyDataSetChanged();
            }
        });

    }


    public void algoritmCautare() {
//ingredients reprezinta vectorul preluat cu split din ceea ce a reusit sa transforme OCR-ul
        for (int i = 0; i < ingredients.size(); i++) {
            //preluam pe rand cate un element
            String ing = ingredients.get(i);
            //il cautam in baza de date asa cum este sau il cautam aplicand alg levenstein
            List<Ingredient> ingList = cautareCuvantBD(ing);
            int j = 0;
            Ingredient ingr = ingredientMethods.selectIngredient(ing);
            if (ingList.size() > 1 && ingr != null && ingr.getIdRating() != 0) {
                //vezi cazul glycerin si glycerine, in bd fiinc cu LIKE '%glycerin%'  mi se returneaza ambele
                ingredientsBD.add(ingr);
            } else {
                while (j < 3 && ingList.size() > 1 && i < ingredients.size() - 1) {
                    //ex am alcool si mi apar mai multe rezultate. incerc sa le combin cu stringurile vecine
                    int poz = i + 1;
                    ing = ing + " " + ingredients.get(poz);
                    //il caut iarasi in bd dar de data asta concatenat cu cuv alaturat
                    ingList = cautareCuvantBD(ing);
                    if (poz < ingredients.size() - 1) {
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
        ingredienteReturnate = ingredientMethods.selectIngredients(ingredient);
        if (ingredienteReturnate.size() == 0) {
            //inseamna ca in bd nu se gaseste niciun rezultat si se incearca a se corecta inputul
            recursivLevenstein(ingredient, 0.7);
        } else if (ingredienteReturnate.size() == 1) {
            //daca bd-ul a returnat 1 element o sa presupunem ca e fix cel cautat
            Ingredient ingr = ingredienteReturnate.get(0);
            if (!ingredientsBD.contains(ingredienteReturnate.get(0)) && ingr != null && ingr.getIdRating() != 0) {
                ingredientsBD.add(ingr);
            }
        }
        return ingredienteReturnate;

    }


    public void recursivLevenstein(String ing, double fuzzy) {
        //un fuzzy mare inseamna ca dorim cu cat mai putine modficari inputul sa se potriveasca cu un anumit cuv din dictionar
        //un fuzzy mic mareste sansele de a returna mai multe ingrediente care se scriu asemanator, adica au cateva caractere in comun

        if (fuzzy >= 0.5 && fuzzy <= 1) {
            //merge greu randu urm
            List<String> levenshtein = LevenshteinDistanceSearch.Search(ing, levenshteinList, fuzzy);
            if (levenshtein.size() == 1) {
                //daca a returnat doar un rezultat inseamna ca a gasit doar un ing asemanator
                String numeIng = levenshtein.get(0);
                //daca exista deja in lista de ingrediente pe care o vom baga in adapter la listview, nu il mai introducem iar
                Ingredient ingr = ingredientMethods.selectIngredient(numeIng);
                if (!ingredientsBD.contains((ingredientMethods.selectIngredient(numeIng))) && ingr != null && ingr.getIdRating() != 0)
                    ingredientsBD.add(ingr);
            } /*else if (levenshtein.size() == 0) {
                //daca s-au returnat 0 rezultate incercam sa micsoram fuzzines, in ideea in care poate ocr-ul a incurcat mai multe caractere
                recursivLevenstein(ing, fuzzy - 0.1);
            } */ else if (levenshtein.size() > 1) {
                //daca se returneaza mai mult de  1 rez marim fuzziness ul, pana se returneaza doar 1 rezultat care este cel potrivit noua
                levenshteinList = levenshtein;
                recursivLevenstein(ing, fuzzy + 0.1);
            }

        }
    }

}
