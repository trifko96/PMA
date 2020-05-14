package com.pma.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pma.DataMock;
import com.pma.R;
import com.pma.activities.DayPreviewTabsActivity;
import com.pma.activities.MealDetailsActivity;
import com.pma.adapters.ActivityPreviewRecyclerAdapter;
import com.pma.adapters.MealPreviewRecyclerAdapter;
import com.pma.model.Meal;
import com.pma.view_model.DayPreviewViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitiesMealsPreviewFragment extends Fragment implements MealPreviewRecyclerAdapter.MealClickListener {

    private DayPreviewViewModel viewModel;
    private TextView totalKcal;
    private TextView totalProtein;
    private TextView totalCarb;
    private TextView totalFat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activities_meals_preview, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(DayPreviewViewModel.class);

        totalKcal = rootView.findViewById(R.id.total_kcals);
        totalProtein = rootView.findViewById(R.id.total_proteins);
        totalCarb = rootView.findViewById(R.id.total_carbs);
        totalFat = rootView.findViewById(R.id.total_fats);



        //ubacivanje podataka za recyclev view aktivnosti
        final RecyclerView activitiesRecycler = rootView.findViewById(R.id.activities_recycler_view);
        activitiesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        activitiesRecycler.setHasFixedSize(true);

        ActivityPreviewRecyclerAdapter activitiesAdapter = new ActivityPreviewRecyclerAdapter();
        activitiesAdapter.setActivities(DataMock.getInstance().getActivities());
        activitiesRecycler.setAdapter(activitiesAdapter);

        //recycler za obroke
        RecyclerView mealsRecycler = rootView.findViewById(R.id.meals_recycler_view);
        mealsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mealsRecycler.setHasFixedSize(true);

        final MealPreviewRecyclerAdapter mealsAdapter = new MealPreviewRecyclerAdapter();
        mealsRecycler.setAdapter(mealsAdapter);

        final PieChart pieChart = rootView.findViewById(R.id.pie_chart);

        viewModel.getMeals().observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                mealsAdapter.setMeals((ArrayList<Meal>) meals);
                float protein = 0, carbs = 0, fats = 0, totalKcals = 0;
                for (Meal meal : meals) {
                    protein += meal.getTotalProtein();
                    carbs += meal.getTotalCarb();
                    fats += meal.getTotalFat();
                    totalKcals += meal.getTotalKcal();
                }
                DecimalFormat df = new DecimalFormat("#.##");
                totalKcal.setText(df.format(totalKcals) + " gr");
                totalProtein.setText(df.format(protein) + " gr");
                totalFat.setText(df.format(fats) + " gr");
                totalCarb.setText(df.format(carbs) + " kcal");

                addChartData(pieChart, protein, carbs, fats);
            }
        });

        mealsAdapter.setListener(this);

        return rootView;
    }

    @Override
    public void onClick(int mealId) {
        Intent intent = new Intent(getActivity(), MealDetailsActivity.class);
        startActivity(intent);
    }

    private void addChartData(PieChart pieChart, float protein, float carbs, float fats) {

        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawSliceText(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDescription(new Description());
        pieChart.setHoleRadius(10f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(protein, "proteins"));
        yValues.add(new PieEntry(carbs, "fats"));
        yValues.add(new PieEntry(fats, "carbs"));

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1OffsetPercentage(80.f);
        pieDataSet.setValueLinePart1Length(0.2f);
        pieDataSet.setValueLinePart2Length(0.4f);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#3dd149"));
        colors.add(Color.parseColor("#136bd6"));
        colors.add(Color.parseColor("#d62e1e"));
        pieDataSet.setColors(colors);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

}
