package com.realfit1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeightLogActivity extends AppCompatActivity {

    private TextView mCurrentWeight;
    private ArrayList<Weight> weightList;
    private ListView mListView;
    private FloatingActionButton fab;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private static Context parentReference = null;
    private DatabaseReference mDatabase;
    private ChildEventListener mListener;
    Weight weightLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_log);
        parentReference = this;
        sp = getSharedPreferences("Profile_Settings", Activity.MODE_PRIVATE);
        editor = sp.edit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Weight Logger");
        mDatabase = FirebaseDatabase.getInstance().getReference("User").child("WeightLog");

        // assign views
        mCurrentWeight = (TextView) findViewById(R.id.current_weight_label);
        weightList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.listview_weight);
        CustomAdapter adapter = new CustomAdapter(parentReference, weightList);
        mListView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        mCurrentWeight.setText("Current Weight:  " + sp.getString("weight", "") + "" + sp.getString("unitIntext", ""));
        // now for weight goal

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(parentReference);
                dialog.setTitle("Add Weight Log");
                dialog.setMessage("Enter in your current weight:");
                // we want to create an edit text for the user to input in
                final EditText input = new EditText(parentReference);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                dialog.setView(input);

                // now we want to set up the box

                dialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // if clicked, we want to retrieve the current date and weight
                        Double weight = Double.parseDouble(input.getText().toString());
                        // we want to set the text view for last logged weight
                        Calendar cal = Calendar.getInstance(Locale.getDefault());
                        final String currDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
                        // set it up as a new weight object

                        String id = mDatabase.push().getKey();
                        final Weight currWeight = new Weight(id,weight, currDate);

                        mDatabase.child(id).setValue(currWeight).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(parentReference,"Saved to database",Toast.LENGTH_SHORT).show();
                            }
                        });

                        editor.putString("weight", "" + weight);
                        editor.commit();
                        // now we want to set the current weight text change
                        mCurrentWeight.setText("Current Weight: " + weight + "" + sp.getString("unitIntext", ""));

                        Toast.makeText(parentReference, "saved successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                // show the dialog
                dialog.show();

            }
        });


    }

    // create a custom adapter for this
    public class CustomAdapter extends ArrayAdapter<Weight> {

        public CustomAdapter(Context context, final ArrayList<Weight> weightList) {
            super(context, 0, weightList);

            mListener = mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    weightLog = dataSnapshot.getValue(Weight.class);
                    weightList.add(weightLog);

                    notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    weightLog = dataSnapshot.getValue(Weight.class);

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    weightLog = dataSnapshot.getValue(Weight.class);

                    notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // get the weight object

            final Weight weightItem = this.getItem(position);


            // setup layout
            if (convertView == null)
                convertView = LayoutInflater.from(parentReference).inflate(R.layout.view_weight_log, parent, false);

            // get text views
            TextView date = (TextView) convertView.findViewById(R.id.recorded_date);
            final TextView weight = (TextView) convertView.findViewById(R.id.recorded_weight);

            // set it up
            date.setText(weightItem.getFormattedDate());
            weight.setText(weightItem.getWeight()+ sp.getString("unitIntext","Kg"));
            // check out the menu button

            // create ImageView
            final ImageView menuButton = (ImageView) convertView.findViewById(R.id.menuButton);

            // create a listener
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // instantiate a pop up menu
                    PopupMenu menu = new PopupMenu(parentReference, menuButton);
                    // inflate the pop up menu with the xml
                    menu.getMenuInflater().inflate(R.menu.popup_menu, menu.getMenu());

                    // create an event listener
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    // create another alert dialog with a final edit text
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(parentReference);

                                    // set up dialog messages
                                    dialog.setTitle("Edit Weight Log");
                                    dialog.setMessage("Edit weight log\'s weight.");

                                    // create 1 edit text
                                    // we want to create an edit text for the user to input in
                                    final EditText input = new EditText(parentReference);

                                    //set up input
                                    input.setText("" + weightItem.getWeight());

                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    dialog.setView(input);

                                    // set up listeners for positive and negative buttons

                                    dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // here we attempt to update.

                                            Double parsedWeight = Double.parseDouble(input.getText().toString());
                                            weightItem.setWeight(parsedWeight);
                                            String id = weightItem.getId();

                                            mDatabase.child(id).setValue(weightItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(parentReference,"Weight updated in db",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    });

                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    break;
                                case R.id.delete:

                                    String id = weightItem.getId();
                                    mDatabase.child(id).removeValue();
                                    weightList.remove(position);
                                    Toast.makeText(parentReference, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return true;
                        }
                    });

                    // finally show the pop up menu
                    menu.show();
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabase.removeEventListener(mListener);
    }
}
