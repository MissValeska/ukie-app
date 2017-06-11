package com.ukie.ukie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;

public class Questions extends AppCompatActivity implements View.OnKeyListener {

    private static final String TAG = "SignInActivity";

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    ArrayList<questionData> data;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        EditText textView = (EditText) findViewById(R.id.enterConj);
        textView.setOnKeyListener(this);

        Intent intent = getIntent();

        data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        Log.w(TAG, data.get(0).getConj());

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        Log.w(TAG, String.valueOf(data.size()));


        prog = (ProgressBar) findViewById(R.id.progressBar3);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        Log.w(TAG, "Rawr:" + String.valueOf(QuestionCount));

        Log.w(TAG, data.get(index).getString("conj"));
        ((TextView) findViewById(R.id.instructions)).setText("Conjugate " + data.get(index).getString("infin") + " for " + data.get(index).getString("conjtype"));

    }

    public String getConj(String str) {

        if(data.get(index).getString(str) != null) {
            return data.get(index).getString(str);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            CheckedTextView textView1 = (CheckedTextView) findViewById(R.id.checkedTextView);

            String tmp = ((EditText) v).getText().toString();

            if (tmp.compareToIgnoreCase(getConj("conj")) == 0) {
                textView1.setText("Correct!");
                textView1.setChecked(true);
                //textView1.setTextColor(100);
                if (prog.getProgress() != prog.getMax()) {
                    prog.setProgress(prog.getProgress() + 1);
                }
                final boolean[] ran = {false};
                FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("correct").child(getConj("conj")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ran[0] = true;
                        int count = Integer.parseInt(dataSnapshot.getValue().toString());

                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("correct").child(getConj("conj")).setValue(count+1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!ran[0]) {
                    FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("correct").child(getConj("conj")).setValue(1);
                }
                Log.w(TAG, "Victory!");

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                //fab.setImageIcon();
                fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
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
                            if(data.get(index+1).getString("type").contains("radio")) {
                                intent = new Intent(getApplicationContext(), radioQuestions.class);
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
                        }
                    }
                });

            } else { // !! Consider locking this if the user gets the question correct, so the progress bar cannot decrement again
                textView1.setText("That's incorrect, the correct conjugation for " + getConj("conjtype") + " of " + getConj("infin") + " is \"" + getConj("conj") + "\"");
                textView1.setChecked(false);
                //checkBtn.setBackgroundColor(getResources().getColor(md_red_800));
                //textView1.setTextColor(500);
                if (prog.getProgress() != 0) {
                    prog.setProgress(prog.getProgress() - 1);
                }
                final boolean[] ran = {false};
                FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("incorrect").child(getConj("conj")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ran[0] = true;
                        int count = Integer.parseInt(dataSnapshot.getValue().toString());

                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("incorrect").child(getConj("conj")).setValue(count+1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!ran[0]) {
                    FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wordList").child("incorrect").child(getConj("conj")).setValue(1);
                }
            }
        }
            return false;
    }
}
