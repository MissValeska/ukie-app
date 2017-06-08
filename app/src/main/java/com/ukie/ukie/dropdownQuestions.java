package com.ukie.ukie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class dropdownQuestions extends AppCompatActivity {

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth mAuth;

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

        final Spinner dropDown = (Spinner) findViewById(R.id.spinner);
        Button checkBtn = (Button) findViewById(R.id.button3);
        TextView txt1 = (TextView) findViewById(R.id.textView3);
        TextView txt2 = (TextView) findViewById(R.id.textView6);

        txt1.setText(data.get(index).getquestionText1());
        txt2.setText(data.get(index).getquestionText2());

        final ArrayList<String> items = new ArrayList<String>();
        items.add(data.get(index).getdropDown1());
        items.add(data.get(index).getdropDown2());

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,items);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        dropDown.setAdapter(spinnerArrayAdapter);
        dropDown.setSelection(0);

        final int[] buttonPressCount = {0};

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dropDown.getSelectedItemPosition() == data.get(index).getCorrectAnswer()-1) {

                    // !! Examine for excess incrementation upon a user entering additional input
                    prog.setProgress(prog.getProgress()+1);
                    Toast.makeText(dropdownQuestions.this, "You are correct!",
                            Toast.LENGTH_SHORT).show();
                    if(buttonPressCount[0] == 0) {
                        if(index + 1 != QuestionCount) {
                            ((Button) v).setText("Next");
                        }
                        else {
                            ((Button) v).setText("Finish");
                        }
                    }
                    final boolean[] ran = {false};
                    FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("correct").child(items.get(data.get(index).getCorrectAnswer()-1)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ran[0] = true;
                            int count = Integer.parseInt(dataSnapshot.getValue().toString());

                            FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("correct").child(items.get(data.get(index).getCorrectAnswer()-1)).setValue(count+1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(!ran[0]) {
                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("correct").child(items.get(data.get(index).getCorrectAnswer()-1)).setValue(1);
                    }
                    else {
                        if(index + 1 != QuestionCount) {

                            Intent intent = null;
                            if(data.get(index+1).getString("type").compareToIgnoreCase("text") == 0) {
                                intent = new Intent(getApplicationContext(), Questions.class);
                            }
                            if(data.get(index+1).getString("type").compareToIgnoreCase("audio") == 0 || data.get(index+1).getString("type").compareToIgnoreCase("image") == 0) {
                                intent = new Intent(getApplicationContext(), ImageQuestions.class);
                            }
                            if(data.get(index+1).getString("type").compareToIgnoreCase("dropdown") == 0) {
                                intent = new Intent(getApplicationContext(), dropdownQuestions.class);
                            }
                            if(data.get(index+1).getString("type").compareToIgnoreCase("radio") == 0) {
                                intent = new Intent(getApplicationContext(), radioQuestions.class);
                            }

                            intent.putExtra("data", data);
                            intent.putExtra("count", QuestionCount);
                            intent.putExtra("index", index + 1);
                            intent.putExtra("progress", prog.getProgress());
                            startActivity(intent);
                        }
                    }

                }
                else {
                    Toast.makeText(dropdownQuestions.this, "You are incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    final boolean[] ran = {false};
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
                    }
                }
                buttonPressCount[0]++;
            }
        });


    }

}
