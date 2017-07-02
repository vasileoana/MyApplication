package com.google.android.myapplication.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Methods.SyncProdusMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.SyncProdus;
import com.google.android.myapplication.DataBase.Rest.DeleteIngredientAnalysis;
import com.google.android.myapplication.DataBase.Rest.DeleteProduct;
import com.google.android.myapplication.DataBase.Rest.DeleteProductAnalyses;
import com.google.android.myapplication.DataBase.Rest.PutProduct;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;
import com.google.android.myapplication.Utilities.Analyses.ListViewAdapter;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;
import com.google.android.myapplication.Utilities.Register.CheckInternetConnection;

import java.util.List;


public class AnalysesActivity extends AppCompatActivity {

    ListView lvMyAnalyses;
    ProductMethods productMethods;
    List<Product> productList;
    ProductAnalysisMethods productAnalysisMethods;
    ListViewAdapter adapter;
    CheckInternetConnection checkInternetConnection;
    int id;
    Product p;
    SyncProdusMethods syncProdusMethods;
    DeleteProduct deleteProduct;
    DeleteIngredientAnalysis deleteIngredientAnalysis;
    DeleteProductAnalyses deleteProductAnalyses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyses);
        checkInternetConnection = new CheckInternetConnection();
        lvMyAnalyses = (ListView) findViewById(R.id.lvMyAnalyses);
        productMethods = new ProductMethods();

        syncProdusMethods = new SyncProdusMethods();
        id = getIntent().getExtras().getInt("userId");
        productAnalysisMethods = new ProductAnalysisMethods();
        productList = productMethods.selectProductsByUser(id);
        adapter = new ListViewAdapter(getApplicationContext(), R.layout.analyses_adapter, productList);
        lvMyAnalyses.setAdapter(adapter);
        lvMyAnalyses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentViewAnalysis dialogFragment = new DialogFragmentViewAnalysis();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("clasa", AnalysesActivity.class.getSimpleName());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fragmentManager, "Analysis Details");
            }
        });

        registerForContextMenu(lvMyAnalyses);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.analyses_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_edit:
                editAnalyses(info.position);
                return true;
            case R.id.menu_delete:
                deleteAnalyses(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteAnalyses(int position) {
        p = productList.get(position);
        productAnalysisMethods.delete(p.getIdProduct());
        productList = productMethods.selectProductsByUser(id);
        ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), R.layout.analyses_adapter, productList);
        lvMyAnalyses.setAdapter(adapter);
        if (checkInternetConnection.isNetworkAvailable(getApplicationContext())) {
            deleteProduct = (DeleteProduct) new DeleteProduct().execute(p.getIdProduct());
            deleteProductAnalyses = (DeleteProductAnalyses) new DeleteProductAnalyses().execute(p.getIdProduct());
            deleteIngredientAnalysis = (DeleteIngredientAnalysis) new DeleteIngredientAnalysis().execute(p.getIdProduct());

        } else {
            syncProdusMethods.insert(new SyncProdus(p.getIdProduct(), false, true));
        }
        Toast.makeText(AnalysesActivity.this, "Stergere realizata cu succes!", Toast.LENGTH_SHORT).show();
    }




private void editAnalyses(int position){

        Product p=productList.get(position);
        android.app.FragmentManager fragmentManager=getFragmentManager();
        DialogFragmentAddAnalysis dialogFragment=new DialogFragmentAddAnalysis(){
@Override
public void onDismiss(DialogInterface dialog){
        productList=productMethods.selectProductsByUser(id);
        adapter=new ListViewAdapter(getApplicationContext(),R.layout.analyses_adapter,productList);
        lvMyAnalyses.setAdapter(adapter);
        }
        };
        Bundle bundle=new Bundle();
        bundle.putString("operatie","editare");
        bundle.putInt("id",p.getIdProduct());
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment,1);
        dialogFragment.show(fragmentManager,"Analysis");


        }


        }
