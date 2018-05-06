package com.realfit1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.R.attr.value;
import static java.lang.Integer.*;

public class ProfileActivity extends AppCompatActivity {
    private static double CONVERT_TO_KG = 0.453592;
    private static final int SELECT_FILE = 2;
    public static final String PREFS_NAME = "Profile_Settings";

    private ImageView pic;
    private EditText et_weight, et_height, et_age;
    private Spinner sp1_gender, sp1_wunit;
    private TextView tv_intbmi,tv_height;
    private Button btn_save;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Uri imageHoldUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        //firebase reference
        mAuth= FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            String Uid = mAuth.getCurrentUser().getUid();
            Log.i("user is logged in",Uid);
        }


        //acessing the views
        pic = (ImageView) findViewById(R.id.ProfileImageView);
        et_weight = (EditText) findViewById(R.id.et_weight);
        et_height = (EditText) findViewById(R.id.et_height);
        sp1_gender = (Spinner) findViewById(R.id.sp1_gender);
        et_age = (EditText) findViewById(R.id.et_age);
        sp1_wunit = (Spinner) findViewById(R.id.sp1_wunit);
        tv_height = (TextView) findViewById(R.id.tv_height1);
        tv_intbmi = (TextView) findViewById(R.id.tv_intbmi);
        btn_save = (Button) findViewById(R.id.btn_save);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadProfile();


        et_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBmi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getBmi();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(home);
                saveProfile();

            }
        });

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePicSelection();
            }
        });
    }

    private void loadProfile() {

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        et_age.setText(sharedPreferences.getString("age"," "));
        et_height.setText(sharedPreferences.getString("height"," "));
        et_weight.setText(sharedPreferences.getString("weight"," "));
        sp1_gender.setSelection(sharedPreferences.getInt("gender",0));
        sp1_wunit.setSelection(sharedPreferences.getInt("wunit",0));
        tv_intbmi.setText(sharedPreferences.getString("bmi",""));
        //pic.setImageURI(Uri.parse(sharedPreferences.getString("imagepath",null)));
    }

    private void saveProfile() {
        editor = sharedPreferences.edit();
        editor.putString("age", et_age.getText().toString());
        editor.putString("weight", et_weight.getText().toString());
        editor.putString("height", et_height.getText().toString());
        editor.putInt("gender", sp1_gender.getSelectedItemPosition());
        editor.putInt("wunit", sp1_wunit.getSelectedItemPosition());
        editor.putString("bmi",String.valueOf(getBmi()));
        editor.putString("unitIntext",String.valueOf(sp1_wunit.getSelectedItem()));



        editor.commit();
        editor.apply();

        if (imageHoldUri != null) {
            String imagepath = imageHoldUri.getLastPathSegment();
            editor.putString("imagepath",imagepath);
            editor.commit();
            editor.apply();

        }

    }
    private float getBmi() {

        String heightValue;
        String weightValue;
        double result = 0;
        double weight = 0.0;
        float height;
        // height in meters
        heightValue = et_height.getText().toString();

        weightValue = et_weight.getText().toString();
        if (heightValue != null && !"".equals(heightValue)
                && weightValue != null && !"".equals(weightValue)) {

            height = Float.parseFloat(heightValue) / 100;

            if (sp1_wunit.getSelectedItemPosition() == 0)
                weight = Float.parseFloat(weightValue);
            else
                weight = Float.parseFloat(weightValue) * CONVERT_TO_KG;


                    result = weight / (height * height);
                                                }
            tv_intbmi.setText(String.valueOf(Math.round(result)));
            return Math.round(result);
    }

    private void profilePicSelection() {

        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY
        // Camera doesnt work for some reason so we stick with gallery on.

        final CharSequence[] items = {"Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

               if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if(items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

       private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
               pic.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}