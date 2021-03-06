package com.google.android.myapplication.Utilities.SearchIngredient;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.myapplication.Activities.ListIngredientsActivity;
import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Methods.RatingMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;
import com.google.android.myapplication.Utilities.SearchProduct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 08-Apr-17.
 */

public class DialogFragmentViewIngredient extends DialogFragment {

    TextView tvIngredientName, tvIngredientDescription;
    ImageView ivIngredientRatingImg;
    Ingredient ingredient;
    int position;
    RatingMethods ratingMethods;
    IngredientMethods ingredientMethods;

    public DialogFragmentViewIngredient() {
        ratingMethods = new RatingMethods();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.dialog_fragment_ingredient_details, container, false);
        getDialog().setTitle("Details");
        tvIngredientName = (TextView) rootView.findViewById(R.id.tvIngredientName);
        tvIngredientDescription = (TextView) rootView.findViewById(R.id.tvIngredientDescription);
        ivIngredientRatingImg = (ImageView) rootView.findViewById(R.id.ivIngredientRatingImg);
        ingredientMethods=new IngredientMethods();
        Bundle bundle = getArguments();

        position = bundle.getInt("poz");

        if(bundle.getString("from").equals(IngredientsFragment.class.getSimpleName()))
        {
            ingredient=ListViewAdapter.arrayList.get(position);
        }
            else if(bundle.getString("from").equals(ListIngredientsActivity.class.getSimpleName())) {
            ingredient = ListIngredientsActivity.ingredientsBD.get(position);
        }
        else if(bundle.getString("from").equals(DialogFragmentViewAnalysis.class.getSimpleName())){
            ingredient = DialogFragmentViewAnalysis.ingredients.get(position);
        }
        tvIngredientName.setText(ingredient.getName());
        String rating = ratingMethods.getRating(ingredient.getIdRating());
        int img = RatingMethods.returnRatingImage(rating);
        if(ingredient.getDescription() != null)
        tvIngredientDescription.setText(rating+"\n"+ingredient.getDescription());
       else
            tvIngredientDescription.setText(rating);

        ivIngredientRatingImg.setImageResource(img);
        return rootView;
    }


}
