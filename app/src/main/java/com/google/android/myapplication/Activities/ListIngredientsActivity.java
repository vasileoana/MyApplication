package com.google.android.myapplication.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.DataBase.Rest.IsServerOpen;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddIng;
import com.google.android.myapplication.Utilities.ListIngredients.ListViewAdapter;
import com.google.android.myapplication.Utilities.Ocr.LevenshteinDistanceSearch;
import com.google.android.myapplication.Utilities.Ocr.OcrOnThread;
import com.google.android.myapplication.Utilities.SearchIngredient.DialogFragmentViewIngredient;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListIngredientsActivity extends AppCompatActivity {
    public static Context context;
    public static List<Ingredient> ingredientsBD;
    IsServerOpen isServerOpen;
    OcrOnThread ocrOnThread;
    public static ArrayList<String> ingredients;
    IngredientMethods ingredientMethods;
    ListView lv;
    List<String> bdIng;
    ImageButton btnAddIng;
    ImageButton btnSaveAnalysis;
    ProductMethods productMethods;
    ListViewAdapter adapter;
    public static List<Ingredient> ingredienteReturnate;
    public static List<Ingredient> ingredientList;
    public static List<String> ingredientNameList;
    public static List<String> levenshteinList;
    String tip_utilizator;
    int idUser = -1;
    TextView addIng, removeIng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ingredients);
        context = ListIngredientsActivity.this;
        btnSaveAnalysis = (ImageButton) findViewById(R.id.btnSave);
        isServerOpen = new IsServerOpen();
        btnAddIng = (ImageButton) findViewById(R.id.btnAddIng);
        tip_utilizator = getIntent().getExtras().getString("tipUtilizator");
        addIng = (TextView) findViewById(R.id.textView1);
        removeIng = (TextView) findViewById(R.id.textView2);
        if (tip_utilizator.equals("logat")) {
            idUser = (int) getIntent().getExtras().get("userId");
        } else {
            btnSaveAnalysis.setVisibility(View.GONE);
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

        //  algoritmCautare();
        ocrOnThread = (OcrOnThread) new OcrOnThread() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (OcrOnThread.dialog != null && OcrOnThread.dialog.isShowing()) {
                    OcrOnThread.dialog.dismiss();
                }
                adapter = new ListViewAdapter(getApplicationContext(), R.layout.search_ingredients_adapter, ingredientsBD);
                lv.setAdapter(adapter);
            }
        }.execute();


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
                //doar daca e serverul deschis merge salvata
                isServerOpen = (IsServerOpen) new IsServerOpen(){
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (!s.equals("5")) {
                            FragmentManager fragmentManager = getFragmentManager();
                            DialogFragmentAddAnalysis dialogFragment = new DialogFragmentAddAnalysis();
                            Bundle bundle = new Bundle();
                            bundle.putInt("userId", idUser);
                            bundle.putString("operatie", "adaugare");
                            dialogFragment.setArguments(bundle);
                            dialogFragment.show(fragmentManager, "IngredientsFragment Manager");
                        }
                        else {
                            Toast.makeText(ListIngredientsActivity.this, "Server inchis. Incercati mai tarziu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
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



    @Override
    public void onBackPressed() {
        Intent intent;
        if (idUser != -1) {
            intent = new Intent(getApplicationContext(), NavigationActivity.class);
            intent.putExtra("userId", idUser);
            intent.putExtra("tipUtilizator", "logat");
            if (OcrOnThread.dialog != null && OcrOnThread.dialog.isShowing()) {
                OcrOnThread.dialog.dismiss();
            }
            startActivity(intent);

        } else {
            if (OcrOnThread.dialog != null && OcrOnThread.dialog.isShowing() ) {
                OcrOnThread.dialog.dismiss();
            }

            intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("tipUtilizator", "anonim");
            startActivity(intent);

        }
    }
}
