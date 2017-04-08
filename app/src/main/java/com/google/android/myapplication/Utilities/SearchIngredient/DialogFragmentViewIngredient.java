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

/**
 * Created by Oana on 08-Apr-17.
 */

public class DialogFragmentViewIngredient extends DialogFragment {

    TextView tvIngredientName, tvIngredientDescription;
    ImageView ivIngredientRatingImg;
    Ingredient ingredient;
    int position;
    Button btnOk;
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
        btnOk = (Button) rootView.findViewById(R.id.btnIngredientDetails);
        ingredientMethods=new IngredientMethods();
        Bundle bundle = getArguments();
        position = bundle.getInt("poz");

        if(bundle.getString("from").equals(IngredientsFragment.class.getSimpleName()))
        {
            ingredient=ingredientMethods.select().get(position);
        }
            else if(bundle.getString("from").equals(ListIngredientsActivity.class.getSimpleName())) {
            ingredient = ListIngredientsActivity.ingredientsBD.get(position);
        }
        tvIngredientName.setText(ingredient.getName());
        String rating = ratingMethods.getRating(ingredient.getIdRating());
        int img = RatingMethods.returnRatingImage(rating);
        tvIngredientDescription.setText(rating+"\n\n"+ingredient.getDescription());
        ivIngredientRatingImg.setImageResource(img);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }


}
