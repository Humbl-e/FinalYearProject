package com.realfit1;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyGoalsFragment extends Fragment implements View.OnClickListener  {

    private int DEFAULT_Step_Count = 10000;
    private Counter counterStep,counterCal;
    private Button plus,plus1,minus,minus1,save;
    private TextView goalStep,goalCal;
    SharedPreferences goals;
    SharedPreferences.Editor editor;
    private int DEFAULT_Cal_Count=2500;

    public DailyGoalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setActionBarTitle("Daily Goals");

        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_daily_goals, container, false);



        goals = getActivity().getSharedPreferences("goals", Context.MODE_PRIVATE);
        editor = goals.edit();


        plus = (Button) view.findViewById(R.id.btn_plus);
        plus1 = (Button) view.findViewById(R.id.btn_plus1);
        minus = (Button) view.findViewById(R.id.btn_minus);
        minus1 = (Button) view.findViewById(R.id.btn_minus1);
        goalStep = (TextView) view.findViewById(R.id.tv_goal_steps);
        goalCal = (TextView) view.findViewById(R.id.tv_goal_cals);
        save = (Button) view.findViewById(R.id.btn_saveCounter);


        counterStep = new Counter(DEFAULT_Step_Count);
        counterCal= new Counter(DEFAULT_Cal_Count);

        goalStep.setText(String.valueOf(goals.getInt("steps",0)));
        goalCal.setText(String.valueOf(goals.getInt("calories",0)));
        counterStep.setCount(goals.getInt("steps",0));
        counterCal.setCount(goals.getInt("calories",0));

//        Log.i("stepcountertv",goalStep.getText().toString());

        plus.setOnClickListener(this);
        plus1.setOnClickListener(this);
        minus.setOnClickListener(this);
        minus1.setOnClickListener(this);
        save.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_saveCounter:

                editor.putInt("calories",counterCal.getCount());
                editor.putInt("steps",counterStep.getCount());
                editor.apply();

                Log.i("caloriecounter",String.valueOf(counterCal.getCount()));
                Log.i("stepcounter",String.valueOf(counterStep.getCount()));

                Toast.makeText(getActivity(),"Saving...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_plus:
                counterStep.increment();
                goalStep.setText(String.valueOf(counterStep.getCount()));

                break;
            case R.id.btn_minus:
                counterStep.decrement();
                goalStep.setText(String.valueOf(counterStep.getCount()));
                break;
            case R.id.btn_plus1:
                counterCal.increment();
                goalCal.setText(String.valueOf(counterCal.getCount()));

                break;
            case R.id.btn_minus1:
                counterCal.decrement();
                goalCal.setText(String.valueOf(counterCal.getCount()));
                break;
        }

    }



}
