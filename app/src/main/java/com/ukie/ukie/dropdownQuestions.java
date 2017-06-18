package com.ukie.ukie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dropdownQuestions extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth mAuth;

    final ArrayList<String> items = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropdown_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        prog = (ProgressBar) findViewById(R.id.progressBar4);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        TextView txt1 = (TextView) findViewById(R.id.textView3);
        TextView txt2 = (TextView) findViewById(R.id.textView6);

        txt1.setText(data.get(index).getquestionText1());
        txt2.setText(data.get(index).getquestionText2());

        items.add(data.get(index).getdropDown1());
        items.add(data.get(index).getdropDown2());

        final Spinner dropDown = (Spinner) findViewById(R.id.spinner);
        final Button checkBtn = (Button) findViewById(R.id.button3);

        class CustomArrayAdapter<T> extends ArrayAdapter<T>
        {
            public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
                super(context, resource, objects);
            }


            //other constructors

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                //we know that simple_spinner_item has android.R.id.text1 TextView:

        /* if(isDroidX) {*/
                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(Color.parseColor("#00AEF9"));
                // !! Finish this by somehow setting all of the other items to be the normal de-selected color, as used in ImageQuestions.java
        /*}*/

                return view;

            }
        }

        final CustomArrayAdapter<String> spinnerArrayAdapter = new CustomArrayAdapter<String>(
                dropdownQuestions.this,android.R.layout.simple_spinner_item,items);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(spinnerArrayAdapter);
        //dropDown.setSelection(0);

        Log.w(TAG, "Items in dropdown:" + String.valueOf(dropDown.getCount()));

        dropDown.setBackgroundColor(getResources().getColor(R.color.tw__transparent));

        Log.w(TAG, dropDown.toString());

        final int[] buttonPressCount = {0};

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dropDown.getSelectedItemPosition() == data.get(index).getCorrectAnswer()-1) {

                    // !! Examine for excess incrementation upon a user entering additional input
                    if(buttonPressCount[0] == 0) {
                        prog.setProgress(prog.getProgress() + 1);
                        Toast.makeText(dropdownQuestions.this, "You are correct!",
                                Toast.LENGTH_SHORT).show();
                        if(index + 1 != QuestionCount) {
                            checkBtn.setText("Next");
                        }
                        else {
                            checkBtn.setText("Finish");
                        }
                    }
                    else {
                        if (index + 1 != QuestionCount) {

                            Intent intent = null;
                            if (data.get(index + 1).getString("type").compareToIgnoreCase("text") == 0) {
                                intent = new Intent(getApplicationContext(), Questions.class);
                            }
                            else if (data.get(index + 1).getString("type").compareToIgnoreCase("audio") == 0 || data.get(index + 1).getString("type").compareToIgnoreCase("image") == 0) {
                                intent = new Intent(getApplicationContext(), ImageQuestions.class);
                            }
                            else if (data.get(index + 1).getString("type").compareToIgnoreCase("dropdown") == 0) {
                                intent = new Intent(getApplicationContext(), dropdownQuestions.class);
                            }
                            else if (data.get(index + 1).getString("type").contains("radio")) {
                                intent = new Intent(getApplicationContext(), radioQuestions.class);
                            }
                            else if (data.get(index + 1).getString("type").compareToIgnoreCase("drag") == 0) {
                                intent = new Intent(getApplicationContext(), DragQuestions.class);
                            }
                            else if (data.get(index + 1).getString("type").compareToIgnoreCase("pairs") == 0) {
                                intent = new Intent(getApplicationContext(), pairingQuestions.class);
                            }

                            intent.putExtra("data", data);
                            intent.putExtra("count", QuestionCount);
                            intent.putExtra("index", index + 1);
                            intent.putExtra("progress", prog.getProgress());

                            int mod = 0;
                            mod = getIntent().getExtras().getInt("module");
                            int exc = 0;
                            exc = getIntent().getExtras().getInt("exercise");
                            Log.w(TAG, "Mod:" + mod + " : " + exc);
                            intent.putExtra("module", mod);
                            intent.putExtra("exercise", exc);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), QuestionBlock.class);

                                        /*intent.putExtra("data", data);
                                        intent.putExtra("count", QuestionCount);
                                        intent.putExtra("index", index + 1);
                                        intent.putExtra("progress", prog.getProgress());*/

                            int mod = 0;
                            mod = getIntent().getExtras().getInt("module");
                            int exc = 0;
                            exc = getIntent().getExtras().getInt("exercise");
                            Log.w(TAG, "ModRawr:" + mod + " : " + exc);
                            intent.putExtra("module", mod);
                            intent.putExtra("exercise", exc);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                }
                else {
                    Toast.makeText(dropdownQuestions.this, "You are incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    /*final boolean[] ran = {false};
                    FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("incorrect").child(items.get(data.get(index).getCorrectAnswer()-1)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ran[0] = true;
                            int count = Integer.parseInt(dataSnapshot.getValue().toString());

                            FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("incorrect").child(items.get(data.get(index).getCorrectAnswer()-1)).setValue(count+1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(!ran[0]) {
                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("incorrect").child(items.get(data.get(index).getCorrectAnswer()-1)).setValue(1);
                    }*/
                }
                buttonPressCount[0]++;
            }
        });


    }

}
