package com.realfit1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalorieCountActivity extends AppCompatActivity implements View.OnClickListener {
    private int counterValue = 0;
    private CardView inputNewValue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button saveBtn;
    private DatabaseReference mDatabaseRef;
    AlertDialog.Builder dialog;
    private EditText changeInput;
    private TextView calorieCount;
    LinearLayout AlertDialogLayout;
    LinearLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_count);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calorie Counter");

        saveBtn = (Button) findViewById(R.id.save_btn);
        inputNewValue = (CardView) findViewById(R.id.inputsetting);
        calorieCount = (TextView) findViewById(R.id.countertext);

        sharedPreferences = getSharedPreferences("counterValue", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dialog = new AlertDialog.Builder(this);
        loadValues();
        setCount();
        inputNewValue.setOnClickListener(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterValue = 0;
                calorieCount.setText(String.valueOf(counterValue));

            }
        });
    }


    private void setCount() {
        counterValue = sharedPreferences.getInt("counterValue", 0);

        if (counterValue == 0) {
            calorieCount.setText("0");
        }
        else {
            calorieCount.setText(Integer.toString(counterValue));
        }
    }

    private void loadValues() {
        sharedPreferences = getSharedPreferences("counterValue", Context.MODE_PRIVATE);
    }

    public void buildAlertDialog(){
        dialog = new AlertDialog.Builder(this);

        AlertDialogLayout = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        AlertDialogLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialogLayout.setLayoutParams(layoutParams);

        AlertDialogLayout.setGravity(Gravity.CENTER);

        changeInput = new EditText(this);
        changeInput.setHint("Example: 250");
        changeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        AlertDialogLayout.addView(changeInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        dialog.setView(AlertDialogLayout);
        dialog.setCancelable(true);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                counterValue += Integer.parseInt(changeInput.getText().toString());
                calorieCount.setText((String.valueOf(counterValue)));
                saveCountertoPref();
            }
        });
        dialog.setIcon(R.drawable.ic_info_black);
        dialog.setTitle("Input your calories:");

        AlertDialog alertDialog = dialog.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCountertoPref(){
        editor.putInt("counterValue", counterValue);
        editor.commit();
        editor.apply();
    }

    public void plusCount(View view) {
        counterValue+=100;
        calorieCount.setText(String.valueOf(counterValue));
        saveCountertoPref();
    }

    public void minusCount(View view) {
        if(counterValue > 0) {
            counterValue -= 100;
        }   calorieCount.setText(String.valueOf(counterValue));
        saveCountertoPref();
    }


    public void coffeeInc(View view) {
        counterValue+=80;
        calorieCount.setText(String.valueOf(counterValue));
        saveCountertoPref();
    }

    public void crispInc(View view) {
        counterValue+=123;
        calorieCount.setText(String.valueOf(counterValue));
        saveCountertoPref();
    }

    public void chocoInc(View view) {
        counterValue+=267;
        calorieCount.setText(String.valueOf(counterValue));
        saveCountertoPref();
    }

    public void biscuitInc(View view) {
        counterValue+=60;
        calorieCount.setText(String.valueOf(counterValue));
        saveCountertoPref();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.inputsetting:
                buildAlertDialog();
            default:
                break;
        }

    }

}
