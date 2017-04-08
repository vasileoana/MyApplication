package com.google.android.myapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;
import com.google.android.myapplication.Utilities.Analyses.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AnalysesActivity extends AppCompatActivity {

    ListView lvMyAnalyses;
    ProductMethods productMethods;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyses);
        lvMyAnalyses= (ListView) findViewById(R.id.lvMyAnalyses);
        productMethods=new ProductMethods();
        int id=getIntent().getExtras().getInt("userId");
        productList=productMethods.selectProductsByUser(id);

        ListViewAdapter adapter=new ListViewAdapter(getApplicationContext(),R.layout.analyses_adapter,productList);
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
    }
}
