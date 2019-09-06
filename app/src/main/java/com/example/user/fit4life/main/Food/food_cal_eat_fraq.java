package com.example.user.fit4life.main.Food;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.example.user.fit4life.R;

public class food_cal_eat_fraq extends Fragment {
    private AutoCompleteTextView autocomplete_product;
    private EditText Edit_merk;
    private EditText cal_100;
    private EditText amount_gram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.cal_eat_fraq_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Food Eaten");
        // initializing all input fields
        autocomplete_product = view.findViewById(R.id.productautocomplete);
        Edit_merk = view.findViewById(R.id.merk_et);
        cal_100 = view.findViewById(R.id.cal_100gr);
        amount_gram = view.findViewById(R.id.amount_gram);
    }

}
