package com.google.android.myapplication.Utilities.SearchProduct;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.CategoryMethods;
import com.google.android.myapplication.DataBase.Methods.ProductAnalysisMethods;
import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.DataBase.Model.ProductAnalysis;
import com.google.android.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Created by Oana on 20-Apr-17.
 */

public class ListViewAdapter extends BaseAdapter {


    private Context context;
    private List<Product> arrayList;
    private LayoutInflater inflater;
    private List<Product> filterArrayList;
    private List<Product> ratingArrayList;
    private List<Product> categoriesArrayList;
    private CategoryMethods categoryMethods = new CategoryMethods();
    private ProductAnalysisMethods productAnalysisMethods = new ProductAnalysisMethods();
    private ProductMethods productMethods = new ProductMethods();

    public ListViewAdapter(Context context, List<Product> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
        categoryMethods = new CategoryMethods();
        this.filterArrayList = new ArrayList<>();//initiate filter list
        this.filterArrayList.addAll(arrayList);//add all items of array list to filter list
        this.ratingArrayList = new ArrayList<>();
        this.ratingArrayList.addAll(arrayList);
        this.categoriesArrayList = new ArrayList<>();
        this.categoriesArrayList.addAll(arrayList);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.analyses_adapter, viewGroup, false);

            viewHolder.category = (TextView) view.findViewById(R.id.tvAnalysysCategoryName);
            viewHolder.brand = (TextView) view.findViewById(R.id.tvAnalysysBrandName);
            viewHolder.functie = (TextView) view.findViewById(R.id.tvAnalysysProductFunction);
            viewHolder.data = (TextView) view.findViewById(R.id.tvAnalysysDate);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        Product model = arrayList.get(position);
        viewHolder.category.setText(categoryMethods.getCategoryName(model.getIdCategory()));
        viewHolder.brand.setText(model.getBrand());
        viewHolder.functie.setText(model.getFunction());
        viewHolder.data.setText(productAnalysisMethods.getDate(model.getIdProduct()));
        return view;
    }

    private class ViewHolder {
        private TextView category;
        private TextView brand;
        private TextView functie;
        private TextView data;
    }


    public void filter(FilterProducts filterProducts, String charText) {

        //If Filter type is NAME and EMAIL then only do lowercase, else in case of NUMBER no need to do lowercase because of number format
        if (filterProducts == FilterProducts.TEXT || filterProducts == FilterProducts.SORTARE || filterProducts == FilterProducts.CATEGORIE)
            charText = charText.toLowerCase(Locale.getDefault());

        arrayList.clear();//Clear the main ArrayList
        //If search query is null or length is 0 then add all filterList items back to arrayList
        if (charText.length() == 0) {
            arrayList.addAll(filterArrayList);
        } else {
            for (Product model : filterArrayList) {
                switch (filterProducts) {
                    case TEXT: {

                        if (model.getBrand().toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getDescription().toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getFunction().toLowerCase(Locale.getDefault()).contains(charText))
                            arrayList.add(model);
                        break;
                    }
                    case SORTARE: {
                        if (categoryMethods.getCategoryName(model.getIdCategory()).toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getBrand().toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getDescription().toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getFunction().toLowerCase(Locale.getDefault()).contains(charText))
                            arrayList.add(model);
                        break;
                    }
                    case CATEGORIE: {
                        if (categoryMethods.getCategoryName(model.getIdCategory()).toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getBrand().toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getDescription().toLowerCase(Locale.getDefault()).contains(charText) ||
                                model.getFunction().toLowerCase(Locale.getDefault()).contains(charText))
                            arrayList.add(model);
                        break;
                    }
                }

            }
        }
        notifyDataSetChanged();
        categoriesArrayList.clear();
        ratingArrayList.clear();
        categoriesArrayList.addAll(arrayList);
        ratingArrayList.addAll(arrayList);


    }

    public void filterCategories(String categorie) {
        if (!categorie.equals("All")) {
            arrayList.clear();
            for (Product p : categoriesArrayList) {
                if (categoryMethods.getCategoryName(p.getIdCategory()).equals(categorie)) {
                    arrayList.add(p);
                }
            }
        }
        notifyDataSetChanged();

    }

    public void sortAsc() throws ParseException {

        HashMap<Integer, Date> map = new HashMap<>();
        for (Product p : arrayList) {
            String dateString = productAnalysisMethods.getDate(p.getIdProduct());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (dateString != null) {
                Date date = format.parse(dateString);
                map.put(p.getIdProduct(), date);
            }
        }
        arrayList.clear();
        Object[] a = map.entrySet().toArray();

        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Integer, Date>) o2).getValue()
                        .compareTo(((Map.Entry<Integer, Date>) o1).getValue());
            }
        });
        int id;
        Product p;
        for (Object e : a) {
            id = ((Map.Entry<Integer, Date>) e).getKey();
            p = productMethods.selectProductById(id);
            arrayList.add(p);

        }

        notifyDataSetChanged();

    }

    public void sortDesc() throws ParseException {
        HashMap<Integer, Date> map = new HashMap<>();
        for (Product p : arrayList) {
            String dateString = productAnalysisMethods.getDate(p.getIdProduct());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(dateString);
            map.put(p.getIdProduct(), date);
        }
        arrayList.clear();
        Object[] a = map.entrySet().toArray();

        Arrays.sort(a, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Map.Entry<Integer, Date>) o2).getValue()
                        .compareTo(((Map.Entry<Integer, Date>) o1).getValue());
            }
        });
        int id;
        Product p;
        for (Object e : a) {
            id = ((Map.Entry<Integer, Date>) e).getKey();
            p = productMethods.selectProductById(id);
            arrayList.add(p);

        }

        notifyDataSetChanged();
    }


}
