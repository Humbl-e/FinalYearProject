package com.realfit1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private CardView heartRate,stepCounter,calorieCounter,sleepTracker,weightTracker,waterTracker;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("DashBoard");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        heartRate = view.findViewById(R.id.heart_rate);
        stepCounter = view.findViewById(R.id.step_counter);
        weightTracker = view.findViewById(R.id.weight_tracker);
        waterTracker = view.findViewById(R.id.water_tracker);
        calorieCounter = view.findViewById(R.id.calorie_counter);
        sleepTracker = view.findViewById(R.id.sleep_tracker);
        weightTracker.setOnClickListener(this);
        heartRate.setOnClickListener(this);
        stepCounter.setOnClickListener(this);
        waterTracker.setOnClickListener(this);
        calorieCounter.setOnClickListener(this);
        sleepTracker.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.step_counter : i= new Intent(getActivity(),StepCountActivity.class);
                startActivity(i);
                break;
            case R.id.heart_rate : i= new Intent(getActivity(),HeartRateActivity.class);
                startActivity(i);
                break;
            case R.id.weight_tracker : i= new Intent(getActivity(),WeightLogActivity.class);
                startActivity(i);
                break;
            case R.id.water_tracker : i= new Intent(getActivity(),WaterActivity.class);
                startActivity(i);
                break;
            case R.id.calorie_counter : i= new Intent(getActivity(),CalorieCountActivity.class);
                startActivity(i);
                break;
            case R.id.sleep_tracker : i= new Intent(getActivity(),SleepTrackActivity.class);
                startActivity(i);
                break;
            default:
                break;

        }

    }
}
