package com.google.android.myapplication.Activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;
import com.google.android.myapplication.Utilities.Analyses.ListViewAdapter;
import com.google.android.myapplication.Utilities.ListIngredients.DialogFragmentAddAnalysis;

import java.util.List;


public class AnalysesActivity extends AppCompatActivity {

    ListView lvMyAnalyses;
    ProductMethods productMethods;
    List<Product> productList;
    ProductAnalysisMethods productAnalysisMethods;
    ListViewAdapter adapter;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyses);
        lvMyAnalyses= (ListView) findViewById(R.id.lvMyAnalyses);
        productMethods=new ProductMethods();
        id=getIntent().getExtras().getInt("userId");
        productAnalysisMethods=new ProductAnalysisMethods();
        productList=productMethods.selectProductsByUser(id);
        adapter=new ListViewAdapter(getApplicationContext(),R.layout.analyses_adapter,productList);
        lvMyAnalyses.setAdapter(adapter);
        lvMyAnalyses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                DialogFragmentViewAnalysis dialogFragment = new DialogFragmentViewAnalysis();
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
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
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
        Product p=productList.get(position);
        productMethods.delete(p.getIdProduct());
        productAnalysisMethods.delete(p.getIdProduct());
        productList=productMethods.selectProductsByUser(id);
        ListViewAdapter adapter=new ListViewAdapter(getApplicationContext(),R.layout.analyses_adapter,productList);
        lvMyAnalyses.setAdapter(adapter);
    }

    private void editAnalyses(int position) {
        Product p=productList.get(position);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        DialogFragmentAddAnalysis dialogFragment = new DialogFragmentAddAnalysis();
        Bundle bundle=new Bundle();
        bundle.putString("operatie","editare");
        bundle.putInt("id",p.getIdProduct());
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment,1);

        dialogFragment.show(fragmentManager, "Analysis");

    }

}
