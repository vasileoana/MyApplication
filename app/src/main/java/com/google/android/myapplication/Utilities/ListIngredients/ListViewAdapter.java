package com.google.android.myapplication.Utilities.ListIngredients;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Apr-17.
 */

public class ListViewAdapter extends ArrayAdapter<Ingredient> {

    private List<Ingredient> ingredients = new ArrayList<>();
    private RatingMethods ratingMethods;
    private Context context;
    private int layoutResourceId;

    public ListViewAdapter(Context context, int layoutResourceId, List<Ingredient> ingredients) {
        super(context, layoutResourceId, ingredients);
        ratingMethods = new RatingMethods();
        this.ingredients = ingredients;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutResourceId, null);
        ImageView ingRating = (ImageView) view.findViewById(R.id.ivRatingIngredient);
        TextView ingName = (TextView) view.findViewById(R.id.twIngredient);
        String rating = ratingMethods.getRating(ingredients.get(position).getIdRating());
        ingRating.setImageResource(RatingMethods.returnRatingImage(rating));
        ingName.setText(ingredients.get(position).getName());

        return view;
    }


}
