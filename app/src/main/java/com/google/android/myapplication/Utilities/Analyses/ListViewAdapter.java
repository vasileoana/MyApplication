package com.google.android.myapplication.Utilities.Analyses;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 08-Apr-17.
 */

public class ListViewAdapter extends ArrayAdapter<Product> {

    private List<Product> products = new ArrayList<>();
    private Context context;
    private int layoutResourceId;
    CategoryMethods categoryMethods;


    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> products) {
        super(context, resource, products);
        this.context = context;
        this.products = products;
        this.layoutResourceId = resource;
        categoryMethods=new CategoryMethods();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutResourceId, null);

        TextView tvAnalysysCategoryName = (TextView) view.findViewById(R.id.tvAnalysysCategoryName);
        TextView tvAnalysysProductFunction = (TextView) view.findViewById(R.id.tvAnalysysProductFunction);
        TextView tvAnalysysBrandName = (TextView) view.findViewById(R.id.tvAnalysysBrandName);
        Product p=products.get(position);
        tvAnalysysBrandName.setText(p.getBrand());
        tvAnalysysProductFunction.setText(p.getFunction());
        tvAnalysysCategoryName.setText(categoryMethods.getCategoryName(p.getIdCategory()));
        return view;

    }
}
