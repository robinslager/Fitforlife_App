package com.example.user.fit4life.main.Food;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.user.fit4life.Functions.functions;
import com.example.user.fit4life.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class food_fraq extends Fragment {
    public NavigationView navigationView;
    public MenuItem item;
    private Context context;
    private Activity activity;
    private functions fn;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fraq_food, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Body Info");
        drawChart(view);

        Button cal_eaten = view.findViewById(R.id.callories_eaten);
        Button cal_burnt = view.findViewById(R.id.callories_burnt);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        activity = getActivity();
        Menu menuoption = navigationView.getMenu();
        item = menuoption.findItem(R.id.nav_call_eat);
        fn = new functions();





        cal_eaten.setOnClickListener(caleaten_btn);
//        this.mHost.mActivity.mWindow.mContentParent.mParent.mChildren.1.mChildren.mChildren.0.mChildren.2.menu;



    }

    private void drawChart(View view) {
        // get floats of DB
        Float cal_eaten_res = 8f;
        Float cal_burnt_res = 15f;
        // for radius.
        Float cal_toatal = cal_eaten_res + cal_burnt_res;

        PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(cal_eaten_res, "Eaten", 0));
        yvalues.add(new PieEntry(cal_burnt_res, "burnt", 1));
        PieDataSet dataSet = new PieDataSet(yvalues, "string");
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(cal_toatal);
        pieChart.setHoleRadius(cal_toatal);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
    }

    private View.OnClickListener caleaten_btn = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            context.
        fn.displaySelectedScreen(item.getItemId(), getFragmentManager(), activity);

        }
    };
}