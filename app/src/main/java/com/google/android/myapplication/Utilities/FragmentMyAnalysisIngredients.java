package com.google.android.myapplication.Utilities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.myapplication.R;

/**
 * Created by Oana on 07-Apr-17.
 */

public class FragmentMyAnalysisIngredients extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myanalysis_ingredients, null);

      //  ListViewIngredientsAdapter adapter=new ListViewIngredientsAdapter(getActivity().getApplicationContext(),R.layout.search_analyses_adapter,)
       return view;
    }
}
