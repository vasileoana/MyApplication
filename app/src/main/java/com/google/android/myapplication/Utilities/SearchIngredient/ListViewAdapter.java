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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Oana on 06-Apr-17.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    static List<Ingredient> arrayList;
    private LayoutInflater inflater;
    private List<Ingredient> filterArrayList;
    private List<Ingredient> filterByRating;
    private RatingMethods ratingMethods;//duplicate list for filtering

    public ListViewAdapter(Context context, List<Ingredient> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        ratingMethods = new RatingMethods();
        inflater = LayoutInflater.from(context);
        filterByRating = new ArrayList<>();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
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
        String rating = ratingMethods.getRating(arrayList.get(i).getIdRating());
        viewHolder.name.setText(model.getName());
        viewHolder.ingRating.setImageResource(returnRatingImage(rating));

        return view;
    }


    private class ViewHolder {
        private TextView name;
        ImageView ingRating;
    }

    private int adaptRating(int rating) {
        if (rating == 0 || rating == 1) {
            rating += 2;
        } else
        if (rating == 2) {
            rating = 1;
        } else
        if (rating == 3) {
            rating = 4;
        } else
        if (rating == 4) {
            rating = 5;
        }
        return rating;
    }

    // Filter Class to filter data
    public void filter(FilterIngredients filterIngredients, String charText, boolean isSearchWithPrefix, int rating) {
        rating = adaptRating(rating);
        if (charText == null) {
            arrayList.addAll(filterArrayList);
        } else {
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
                    boolean isRatingOk = (model.getIdRating() == rating || rating == 5);
                    //Now check the type of search filter
                    switch (filterIngredients) {
                        case NAME:
                            if (isSearchWithPrefix) {
                                //if STARTS WITH radio button is selected then it will match the exact NAME which match with search query
                                boolean isStartWith = model.getName().toLowerCase(Locale.getDefault()).startsWith(charText);
                                if (isStartWith && isRatingOk) {
                                    arrayList.add(model);
                                }
                            } else {
                                //if CONTAINS radio button is selected then it will match the NAME wherever it contains search query
                                boolean isContains = model.getName().toLowerCase(Locale.getDefault()).contains(charText);
                                if (isContains && isRatingOk) {
                                    arrayList.add(model);
                                }
                            }
                            break;
                        case RATING:
                            if (isSearchWithPrefix) {
                                boolean isStartWith = model.getName().toLowerCase(Locale.getDefault()).startsWith(charText);
                                if (isStartWith && isRatingOk)
                                    arrayList.add(model);
                            } else {
                                boolean isContains = model.getName().toLowerCase(Locale.getDefault()).contains(charText);
                                if (isContains && isRatingOk)
                                    arrayList.add(model);
                            }
                            break;
                    }

                }
            }
        }
        notifyDataSetChanged();
        filterByRating.clear();
        filterByRating.addAll(arrayList);
    }

    public void filterRatings(int rating) {
        if (!filterByRating.containsAll(arrayList))
            filterByRating.addAll(arrayList);
        arrayList.clear();
        for (int i = 0; i < filterByRating.size(); i++) {
            Ingredient ingredient = filterByRating.get(i);
            switch (rating) {
                case 0: {
                    if (ingredient.getIdRating() == 2)
                        arrayList.add(ingredient);
                    break;
                }
                case 1: {
                    if (ingredient.getIdRating() == 3)
                        arrayList.add(ingredient);
                    break;
                }
                case 2: {
                    if (ingredient.getIdRating() == 1)
                        arrayList.add(ingredient);
                    break;
                }
                case 3: {
                    if (ingredient.getIdRating() == 4)
                        arrayList.add(ingredient);
                    break;
                }
                case 4: {
                    arrayList.add(ingredient);
                }
            }
        }

        notifyDataSetChanged();
    }


    private int returnRatingImage(String rating) {
        switch (rating) {
            case "SLAB":
                return R.drawable.poor;
            case "BUN":
                return R.drawable.good;
            case "FOARTE BUN":
                return R.drawable.best;
            case "MEDIU":
                return R.drawable.average;
        }
        return R.drawable.good;
    }
}
