package com.realfit1;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SleepTrackActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;

    private EditText sleepingTime;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause, mButtonReset;
    private CountDownTimer mCountDownTimer;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_track);
        prefs = getSharedPreferences("Sleep", MODE_PRIVATE);
        editor = prefs.edit();
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        sleepingTime = findViewById(R.id.et_sleep);
        mButtonStartPause = findViewById(R.id.start_pause_Btn);
        mButtonReset = findViewById(R.id.resetBtn);


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    setTimerValues();
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }
    private long setTimerValues() {
        int time = 0;
        if (!sleepingTime.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            time = Integer.parseInt(sleepingTime.getText().toString().trim());
            editor.putInt("sleepTime",time);
            editor.apply();
        } else {
            // toast message to fill edit text
            Toast.makeText(getApplicationContext(),"Enter a time to start tracking", Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        mStartTime = time *3600000;
        mTimeLeftInMillis = time * 3600000;
        return mTimeLeftInMillis;
    }
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        String hms = String.format(Locale.getDefault(),"%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis),
                    TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mTimeLeftInMillis)),
                    TimeUnit.MILLISECONDS.toSeconds(mTimeLeftInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMillis)));
//        //int hours = (int) (mTimeLeftInMillis/1000) % 24;
//        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
//        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

       // String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d",minutes, seconds);

        mTextViewCountDown.setText(hms);
    }
    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
            sleepingTime.setEnabled(false);
        } else {
            mButtonStartPause.setText("Start");
            sleepingTime.setEnabled(true);
            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTime) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("Sleep", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("Sleep", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", setTimerValues());
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        int time = prefs.getInt("sleepingTime",0);
        updateCountDownText();
        updateButtons();
        sleepingTime.setText(String.valueOf(time));

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

}