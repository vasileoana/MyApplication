package com.google.android.myapplication.Utilities.SearchIngredient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.IngredientMethods;
import com.google.android.myapplication.DataBase.Model.Ingredient;
import com.google.android.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oana on 06-Apr-17.
 */

public class IngredientsFragment extends android.support.v4.app.Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, ListView.OnItemClickListener {

    private CharSequence charSeq;
    private Context context;
    private ListViewAdapter adapter;
    List<Ingredient> arrayList;
    private RadioGroup searchViaRadioGroup, filterNameByRadioGroup, filterRatingByRadioGroup;
    private EditText searchEditText;
    private List<String> ingredientsName = new ArrayList<>();

    private TextView searchViaLabel, filterByLabel;
    private IngredientMethods ingredientMethods = new IngredientMethods();
    private int rating = 4;
    ListView listView;
    /*  Filter Type to identify the type of Filter  */
    private FilterIngredients filterIngredients;
    /*  boolean variable for Filtering */
    private boolean isSearchWithPrefix = false;

    public IngredientsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_ingredients_list_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView(view);
        findViews(view);
        implementEvents();
    }


    //Bind all Views
    private void findViews(View view) {
        filterIngredients = FilterIngredients.NAME;
        searchViaRadioGroup = (RadioGroup) view.findViewById(R.id.search_via_radio_group);
        filterNameByRadioGroup = (RadioGroup) view.findViewById(R.id.filter_name_radio_group);
        filterRatingByRadioGroup = (RadioGroup) view.findViewById(R.id.filter_rating_radio_group);
        searchEditText = (EditText) view.findViewById(R.id.search_text);
        searchViaLabel = (TextView) view.findViewById(R.id.search_via_label);
        filterByLabel = (TextView) view.findViewById(R.id.filter_by_label);

    }


    private void loadListView(View view) {
        listView = (ListView) view.findViewById(R.id.lv_search_ingredient);
        arrayList = ingredientMethods.select();
        for (Ingredient ingredient : arrayList) {
            ingredientsName.add(ingredient.getName());
        }
        adapter = new ListViewAdapter(context, arrayList);
        listView.setAdapter(adapter);
    }

    private void implementEvents() {
        filterNameByRadioGroup.setOnCheckedChangeListener(this);
        filterRatingByRadioGroup.setOnCheckedChangeListener(this);
        searchViaRadioGroup.setOnCheckedChangeListener(this);
        searchViaLabel.setOnClickListener(this);
        filterByLabel.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        searchEditText.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //On text changed in Edit text start filtering the list
                adapter.filter(filterIngredients, charSequence.toString(), isSearchWithPrefix, rating);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //On text changed in Edit text start filtering the list
                charSeq = charSequence;
                adapter.filter(filterIngredients, charSequence.toString(), isSearchWithPrefix, rating);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    boolean cautareNume = true; //to do

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int pos = radioGroup.indexOfChild(radioGroup.findViewById(checkedId));//get the checked position of radio button
        switch (radioGroup.getId()) {
            case R.id.search_via_radio_group:
                switch (pos) {
                    case 0:
                        filterIngredients = FilterIngredients.NAME;
                        cautareNume = true;
                        filterNameByRadioGroup.setVisibility(View.VISIBLE);
                        filterRatingByRadioGroup.setVisibility(View.GONE);
                        //  adapter.filter(filterIngredients, charSeq.toString(), isSearchWithPrefix, rating);

                        break;

                    case 1:
                        filterIngredients = FilterIngredients.RATING;
                        cautareNume = false;
                        filterNameByRadioGroup.setVisibility(View.GONE);
                        filterRatingByRadioGroup.setVisibility(View.VISIBLE);
                        //  adapter.filterRatings(4);
                        break;
                }
                break;
            case R.id.filter_name_radio_group:
                switch (pos) {
                    case 0:
                        isSearchWithPrefix = false;//Set boolean value to false
                        adapter.filter(filterIngredients, String.valueOf(searchEditText.getText()), false, rating);
                        break;
                    case 1:
                        isSearchWithPrefix = true;//Set boolean value to true
                        adapter.filter(filterIngredients, String.valueOf(searchEditText.getText()), true, rating);
                        break;
                }
                break;
            case R.id.filter_rating_radio_group:
                switch (pos) {
                    case 0:
                        rating = 0;
                        adapter.filterRatings(0);
                        break;
                    case 1:
                        rating = 1;
                        adapter.filterRatings(1);
                        break;
                    case 2:
                        rating = 2;
                        adapter.filterRatings(2);
                        break;
                    case 3:
                        rating = 3;
                        adapter.filterRatings(rating);
                        break;
                    case 4:
                        rating = 4;
                        adapter.filterRatings(4);
                        break;
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_via_label:
                //show hide the radio group
                if (searchViaRadioGroup.isShown()) {
                    searchViaLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_dropdown, 0);
                    searchViaRadioGroup.setVisibility(View.GONE);
                    filterNameByRadioGroup.setVisibility(View.GONE);
                    filterRatingByRadioGroup.setVisibility(View.GONE);
                } else {
                    searchViaLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_dropdown, 0);
                    searchViaRadioGroup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.filter_by_label:

                if (filterNameByRadioGroup.isShown() || filterRatingByRadioGroup.isShown()) {
                    filterByLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_dropdown, 0);
                    filterNameByRadioGroup.setVisibility(View.GONE);
                    filterRatingByRadioGroup.setVisibility(View.GONE);
                } else {
                    filterByLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_dropdown, 0);
                    if (cautareNume)
                        filterNameByRadioGroup.setVisibility(View.VISIBLE);
                    else
                        filterRatingByRadioGroup.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        {
            android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
            DialogFragmentViewIngredient dialogFragment = new DialogFragmentViewIngredient();
            Bundle bundle = new Bundle();
            bundle.putInt("poz", position);
            bundle.putString("from", this.getClass().getSimpleName());
            dialogFragment.setArguments(bundle);
            dialogFragment.show(fragmentManager, "Ingredient Details");

        }

    }


}
