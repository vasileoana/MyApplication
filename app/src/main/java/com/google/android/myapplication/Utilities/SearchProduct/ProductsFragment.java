package com.google.android.myapplication.Utilities.SearchProduct;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.myapplication.DataBase.Methods.ProductMethods;
import com.google.android.myapplication.DataBase.Model.Product;
import com.google.android.myapplication.R;
import com.google.android.myapplication.Utilities.Analyses.DialogFragmentViewAnalysis;

import java.util.List;

/**
 * Created by Oana on 20-Apr-17.
 */

public class ProductsFragment extends android.support.v4.app.Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, ListView.OnItemClickListener, Spinner.OnItemSelectedListener {


    private Context context;
    private ListView listView;
    private List<Product> arrayList;
    private ProductMethods productMethods = new ProductMethods();
    private ListViewAdapter adapter;
    private FilterProducts filterProducts;
    private EditText searchEditText;
    private TextView searchViaLabel, filterByLabel;
    private RadioGroup searchViaRadioGroup;
    private Spinner spinnerBrand, spinnerCategorie;
    private SeekBar seekBar;

    public ProductsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_product_list_view_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView(view);
        findViews(view);
        implementEvents();
    }


    private void loadListView(View view) {
        listView = (ListView) view.findViewById(R.id.lv_search_product);
        arrayList = productMethods.selectAllProducts();
        adapter = new ListViewAdapter(context, arrayList);
        listView.setAdapter(adapter);
    }


    private void findViews(View view) {
        filterProducts = FilterProducts.TEXT;
        searchViaRadioGroup = (RadioGroup) view.findViewById(R.id.search_via_radio_group);
        spinnerBrand = (Spinner) view.findViewById(R.id.spinnerBrand);
        spinnerCategorie = (Spinner) view.findViewById(R.id.spinnerCategory);
        seekBar = (SeekBar) view.findViewById(R.id.seekBarRating);
        searchEditText = (EditText) view.findViewById(R.id.search_text);
        searchViaLabel = (TextView) view.findViewById(R.id.search_via_label);
        filterByLabel = (TextView) view.findViewById(R.id.filter_by_label);
    }


    private void implementEvents() {
        spinnerBrand.setOnItemSelectedListener(this);
        spinnerCategorie.setOnItemSelectedListener(this);
        //seekBar.setOnSeekBarChangeListener(this);
        searchViaRadioGroup.setOnCheckedChangeListener(this);
        searchViaLabel.setOnClickListener(this);
        filterByLabel.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //On text changed in Edit text start filtering the list
                adapter.filter(filterProducts, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int pos = radioGroup.indexOfChild(radioGroup.findViewById(checkedId));//get the checked position of radio button
        switch (radioGroup.getId()) {
            case R.id.search_via_radio_group:
                switch (pos) {
                    case 0:
                        filterProducts = FilterProducts.TEXT;//Change filter type to Name if pos = 0
                        spinnerBrand.setVisibility(View.GONE);
                        spinnerCategorie.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        searchEditText.setVisibility(View.GONE);
                        break;

                    case 1:
                        filterProducts = FilterProducts.RATING;//Change filter type to Number if pos = 1
                        spinnerBrand.setVisibility(View.GONE);
                        spinnerCategorie.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        searchEditText.setVisibility(View.GONE);
                        break;
                    case 2:
                        filterProducts = FilterProducts.BRAND;//Change filter type to Number if pos = 1
                        spinnerBrand.setVisibility(View.GONE);
                        spinnerCategorie.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        searchEditText.setVisibility(View.GONE);
                        break;
                    case 3:
                        filterProducts = FilterProducts.CATEGORIE;//Change filter type to Number if pos = 1
                        spinnerBrand.setVisibility(View.GONE);
                        spinnerCategorie.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        searchEditText.setVisibility(View.GONE);
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
                } else {
                    searchViaLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_dropdown, 0);
                    searchViaRadioGroup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.filter_by_label:

                if (spinnerBrand.isShown() || spinnerCategorie.isShown() || searchEditText.isShown() || seekBar.isShown()) {
                    filterByLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_dropdown, 0);
                    spinnerBrand.setVisibility(View.GONE);
                    spinnerCategorie.setVisibility(View.GONE);
                    searchEditText.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                } else {
                    filterByLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_dropdown, 0);
                    switch (filterProducts) {
                        case TEXT: {
                            searchEditText.setVisibility(View.VISIBLE);
                            break;
                        }
                        case RATING: {
                            seekBar.setVisibility(View.VISIBLE);
                            break;
                        }
                        case BRAND: {
                            spinnerBrand.setVisibility(View.VISIBLE);
                            break;
                        }
                        case CATEGORIE: {
                            spinnerCategorie.setVisibility(View.VISIBLE);
                            break;
                        }

                    }
                    break;
                }

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        {
            android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
            DialogFragmentViewAnalysis dialogFragment = new DialogFragmentViewAnalysis();
            Bundle bundle = new Bundle();
            bundle.putInt("poz", position);
            bundle.putString("from", this.getClass().getSimpleName());
            dialogFragment.setArguments(bundle);
            dialogFragment.show(fragmentManager, "Analyses Details");

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}