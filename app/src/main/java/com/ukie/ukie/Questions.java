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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Questions extends AppCompatActivity implements View.OnKeyListener {

    private static final String TAG = "SignInActivity";

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    ArrayList<questionData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText textView = (EditText) findViewById(R.id.enterConj);
        textView.setOnKeyListener(this);

        Intent intent = getIntent();

        data = intent.getParcelableExtra("data");

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        Log.w(TAG, String.valueOf(data.size()));

        for (questionData rawr : data) {
            Log.w(TAG, rawr.toString());
        }

        /*for(int i = 0; i < QuestionCount; i++) {
            Log.w(TAG, "Combien fois?" + QuestionCount);
            try {
                data.add(new JSONObject(data.get(i)));
            } catch (JSONException e) {
                Log.e(TAG, "Error in json:" + e.getMessage());
            }
        }*/

        prog = (ProgressBar) findViewById(R.id.progressBar3);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        Log.w(TAG, "Rawr:" + String.valueOf(QuestionCount));

        Log.w(TAG, data.get(index).conj);
        ((TextView) findViewById(R.id.instructions)).setText("Conjugate " + data.get(index).infin + " for " + data.get(index).conjtype);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            CheckedTextView textView1 = (CheckedTextView) findViewById(R.id.checkedTextView);

            String tmp = ((EditText) v).getText().toString();

            if (tmp.compareToIgnoreCase(data.get(index).conj) == 0) {
                textView1.setText("Correct!");
                textView1.setChecked(true);
                //textView1.setTextColor(100);
                if (prog.getProgress() != prog.getMax()) {
                    prog.setProgress(prog.getProgress() + 1);
                }

                Log.w(TAG, "Victory!");

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                //fab.setImageIcon();
                fab.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(index + 1 != QuestionCount) {
                            Intent intent = null;
                            if(data.get(index+1).type.compareToIgnoreCase("text") == 0) {
                                intent = new Intent(getApplicationContext(), Questions.class);
                            }
                            if(data.get(index+1).type.compareToIgnoreCase("audio") == 0 || data.get(index+1).type.compareToIgnoreCase("image") == 0) {
                                intent = new Intent(getApplicationContext(), ImageQuestions.class);
                            }
                            /*if(firstType.compareToIgnoreCase("dropdown") == 0) {
                                intent = new Intent(getApplicationContext(), ImageQuestions.class); // As of yet, non-existent
                            }*/

                            intent.putExtra("data", data);
                            intent.putExtra("count", QuestionCount);
                            intent.putExtra("index", index + 1);
                            intent.putExtra("progress", prog.getProgress());
                            startActivity(intent);
                        }
                    }
                });

            } else { // !! Consider locking this if the user gets the question correct, so the progress bar cannot decrement again
                textView1.setText("That's incorrect, the correct conjugation for " + data.get(index).conjtype + " of " + data.get(index).infin + " is \"" + data.get(index).conj + "\"");
                textView1.setChecked(false);
                //checkBtn.setBackgroundColor(getResources().getColor(md_red_800));
                //textView1.setTextColor(500);
                if (prog.getProgress() != 0) {
                    prog.setProgress(prog.getProgress() - 1);
                }
            }
        }
            return false;
    }
}
