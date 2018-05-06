package com.realfit1;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    private ListView listView;
    private Progress progress;
    SharedPreferences pedometerpref;
    SharedPreferences calorieprefs;
    SharedPreferences sleepprefs;
    ValueEventListener mListener;
    DatabaseReference mDatabaseRef;
    ArrayAdapter<String> adapter;
    ArrayList<String> progressList;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Daily Progress");

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        listView = view.findViewById(R.id.lv_progress);
        pedometerpref = getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        calorieprefs = getActivity().getSharedPreferences("counterValue", Context.MODE_PRIVATE);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("User").child("Progress");
        progress = new Progress();
        progressList = new ArrayList<>();

        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,progressList);


        getValues();
        mDatabaseRef.setValue(progress);
        mListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    progressList.clear();
                //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Progress mProgress = dataSnapshot.getValue(Progress.class);

                    progressList.add("Date: " + mProgress.getDate().toString());
                    progressList.add("Steps: " + mProgress.getSteps().toString());
                    progressList.add("Calories Burned: " + mProgress.getCalories().toString());


                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void getValues() {
        //retrieve values
        Float dailysteps = pedometerpref.getFloat("stepCount",0);

        int caloriesConsumed = calorieprefs.getInt("counterValue", 0);


        String totalCals = String.valueOf(caloriesConsumed);
        String dailySteps = String.valueOf(dailysteps);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        //save to progress object
        progress.setDate(strDate);
        progress.setSteps(dailySteps);
        progress.setCalories(totalCals);

    }

}

