package com.google.android.myapplication.Utilities.SearchIngredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Oana on 06-Apr-17.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Ingredient> arrayList;
    private LayoutInflater inflater;
    private List<Ingredient> filterArrayList;
    private RatingMethods ratingMethods;//duplicate list for filtering

    //to do particulzarizar sa caute alfabetic
    public ListViewAdapter(Context context, List<Ingredient> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
         ratingMethods=new RatingMethods();
        inflater = LayoutInflater.from(context);

        this.filterArrayList = new ArrayList<>();//initiate filter list
        this.filterArrayList.addAll(arrayList);//add all items of array list to filter list
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView( final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean

            view = inflater.inflate(R.layout.search_ingredients_adapter, viewGroup, false);

            viewHolder.name = (TextView) view.findViewById(R.id.twIngredient);
            viewHolder.ingRating = (ImageView) view.findViewById(R.id.ivRatingIngredient);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        Ingredient model = arrayList.get(i);
        String rating=ratingMethods.getRating(arrayList.get(i).getIdRating());
        viewHolder.name.setText(model.getName());
        viewHolder.ingRating.setImageResource(returnRatingImage(rating));

        return view;
    }


    private class ViewHolder {
        private TextView name;
        ImageView ingRating;
    }

    // Filter Class to filter data
    public void filter(FilterIngredients filterIngredients, String charText, boolean isSearchWithPrefix) {

        //If Filter type is NAME and EMAIL then only do lowercase, else in case of NUMBER no need to do lowercase because of number format
        if (filterIngredients == FilterIngredients.NAME || filterIngredients == FilterIngredients.RATING)
            charText = charText.toLowerCase(Locale.getDefault());

        arrayList.clear();//Clear the main ArrayList

        //If search query is null or length is 0 then add all filterList items back to arrayList
        if (charText.length() == 0) {
            arrayList.addAll(filterArrayList);
        } else {

            //Else if search query is not null do a loop to all filterList items
            for (Ingredient model : filterArrayList) {

                //Now check the type of search filter
                switch (filterIngredients) {
                    case NAME:
                        if (isSearchWithPrefix) {
                            //if STARTS WITH radio button is selected then it will match the exact NAME which match with search query
                            if (model.getName().toLowerCase(Locale.getDefault()).startsWith(charText))
                                arrayList.add(model);
                        } else {
                            //if CONTAINS radio button is selected then it will match the NAME wherever it contains search query
                            if (model.getName().toLowerCase(Locale.getDefault()).contains(charText))
                                arrayList.add(model);
                        }

                        break;
                    case RATING:
                        if (isSearchWithPrefix) {
                            //if STARTS WITH radio button is selected then it will match the exact EMAIL which match with search query
                            if ((model.getName()).toLowerCase(Locale.getDefault()).startsWith(charText))
                                arrayList.add(model);
                        } else {
                            //if CONTAINS radio button is selected then it will match the EMAIL wherever it contains search query
                            if ((model.getName()).toLowerCase(Locale.getDefault()).contains(charText))
                                arrayList.add(model);
                        }

                        break;

                }

            }
        }
        notifyDataSetChanged();
    }

    private int returnRatingImage(String rating){
        switch (rating) {
            case "POOR":
                return R.drawable.poor;
            case "GOOD":
                return R.drawable.good;
            case "BEST":
                return R.drawable.best;
            case "AVERAGE":
                return R.drawable.average;
        }
        return R.drawable.good;
    }
}
