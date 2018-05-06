package com.realfit1;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import me.itangqi.waveloadingview.WaveLoadingView;

public class WaterActivity extends AppCompatActivity {
    private static double CONVERT_TO_KG = 0.453592;

    WaveLoadingView waveLoadingView;
    ImageView addWater;
    Button reset;
    TextView waterIntake;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int count=0;

    SeekBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Water Tracker");
        sp = getSharedPreferences("Profile_Settings",MODE_PRIVATE);
        editor = sp.edit();
        waveLoadingView = findViewById(R.id.wave_view);
        addWater = findViewById(R.id.im_addwater);
        waterIntake = findViewById(R.id.tv_goal_water);
        reset = findViewById(R.id.btn_reset);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);

        getDailyIntake();

        loadTrack();

        Log.i("waveLoading",String.valueOf(waveLoadingView.getProgressValue()));


        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                waveLoadingView.setProgressValue(progress);
                saveTrack();
                if(progress <50){
                    waveLoadingView.setTopTitle("");
                    waveLoadingView.setCenterTitle("");
                    waveLoadingView.setBottomTitle(String.format("%d%%",progress));


                }
                else if(progress <80){
                    waveLoadingView.setTopTitle("");
                    waveLoadingView.setCenterTitle(String.format("%d%%",progress));
                    waveLoadingView.setBottomTitle("");
                }
                else {
                    waveLoadingView.setTopTitle(String.format("%d%%",progress));
                    waveLoadingView.setCenterTitle("");
                    waveLoadingView.setBottomTitle("");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (count < 100){
                    count += (((100*250))/(getDailyIntake().intValue()));
                }

                progressBar.setProgress(count);
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(0);
                count = 0;
                saveTrack();
                waveLoadingView.setProgressValue(0);
                waveLoadingView.setTopTitle("");
                waveLoadingView.setCenterTitle("");
                waveLoadingView.setBottomTitle(String.format("%d%%",count));


            }
        });


    }
    private void saveTrack(){
        sp = getSharedPreferences("Profile_Settings", MODE_PRIVATE);
        editor.putInt("progress",progressBar.getProgress());
        editor.putInt("count",count);
        editor.commit();
        editor.apply();
    }


    private void loadTrack(){
        sp = getSharedPreferences("Profile_Settings", MODE_PRIVATE);
        int countprogress = sp.getInt("count",0);
        count = countprogress;
        int loadprogress = sp.getInt("progress",0);
        waveLoadingView.setProgressValue(loadprogress);
        if(loadprogress < 50 ){
            waveLoadingView.setTopTitle("");
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setBottomTitle(String.format("%d%%",loadprogress));
        }
        else if(loadprogress <80){
            waveLoadingView.setTopTitle("");
            waveLoadingView.setCenterTitle(String.format("%d%%",loadprogress));
            waveLoadingView.setBottomTitle("");
        }
        else {
            waveLoadingView.setTopTitle(String.format("%d%%",loadprogress));
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setBottomTitle("");
        }



    }
    private Double getDailyIntake(){
        double weight;

        if(sp.getInt("wunit",0) == 0) {
            weight = Double.parseDouble(sp.getString("weight", " "));
        }
        else {
            weight = Double.parseDouble(sp.getString("weight", " ")) * CONVERT_TO_KG;

        }

            double dailyIntake = Math.round((weight/30)*100d)/100d;
            double dailyIntakemL = dailyIntake * 1000; // convert to mL
            waterIntake.setText("Your daily water intake is " + dailyIntakemL + " mL");

        return dailyIntakemL;
    }


}
