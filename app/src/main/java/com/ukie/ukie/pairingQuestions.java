package com.ukie.ukie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class pairingQuestions extends AppCompatActivity implements View.OnKeyListener {

    private static final String TAG = "SignInActivity";

    TextView txt;

    int correctAnswr = 0;

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    ArrayList<Button> btnArray;
    ArrayList<Integer> btnSelectedArray;
    ArrayList<String> correctAnswerArray;
    int numCompleted = 0;

    HashMap<Integer, Integer> k = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        Log.w(TAG, "Entered Drag:" + data.size() + " : " + data.toString());

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        prog = (ProgressBar) findViewById(R.id.progressBar7);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        for(int i = 0; i < data.get(index).getInt("pairCount"); i++) {
            k.put(data.get(index).getInt("1pair" + String.valueOf(i)), data.get(index).getInt("2pair" + String.valueOf(i)));
            k.put(data.get(index).getInt("2pair" + String.valueOf(i)), data.get(index).getInt("1pair" + String.valueOf(i)));
        }


        //correctAnswr = data.get(index).getInt("correctAnswer") - 1;


        for(int i = 0; i < btnArray.size(); i++) {
            Button x = btnArray.get(i);
            x.setTag(i);
            btnArray.set(i, x);
        }

    }

    int lastId = -1;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if((int)v.getTag() == lastId) {
            v.setSelected(false);
        }
        else {
            v.setSelected(true);
        }

        for (Button x : btnArray) {
            if((int)x.getTag() == (int)v.getTag() && v.isSelected()) {
                if(lastId != -1) {
                    if(k.get((int)x.getTag()) == lastId) {
                        Toast.makeText(pairingQuestions.this, "You're correct!", Toast.LENGTH_SHORT);
                        numCompleted++;
                        if(numCompleted == 6) {
                            prog.setProgress(prog.getProgress() + 1);
                            onCorrect();
                        }
                    }
                    else {
                        Toast.makeText(pairingQuestions.this, "You're incorrect", Toast.LENGTH_SHORT);
                        btnArray.get(lastId).setSelected(false);
                        btnArray.get((int)v.getTag()).setSelected(false);
                    }
                    lastId = -1;
                }
                else {
                    lastId = (int) x.getTag();
                }
            }
        }

        return false;
    }

    public void onCorrect() {
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
                            /*int cmp = 0;
                            cmp = getIntent().getExtras().getInt("completed");
                            int qBlock = 0;
                            qBlock = getIntent().getExtras().getInt("qBlock");*/

                            /*try {
                                correctArray.put("Module" + mod, new JSONObject());
                                correctArray.put("Exercise" + exc, new JSONObject());
                                correctArray.put("QuestionBlock" + qBlock, new JSONObject());
                                correctArray.put("Question" + index+1, new JSONObject());
                                correctArray.get("Module" + mod);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
            // !! Copy this and its related code to every other question format
            /*if(correctCount.size() == 0)  {
                correctCount.add(1);
            }
            else if(correctCount.size() >= index+1) {
                correctCount.set(index, correctCount.get(index) + 1);
            }
            else {
                correctCount.add(index, 1);
            }*/

            Log.w(TAG, "Mod:" + mod + " : " + exc);
            intent.putExtra("module", mod);
            intent.putExtra("exercise", exc);
            //intent.putIntegerArrayListExtra("correctCount", correctCount);
            //intent.putIntegerArrayListExtra("incorrectCount", incorrectCount);
            //intent.putExtra("completed", cmp);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            Intent intent = new Intent(getApplicationContext(), QuestionBlockCompletedActivity.class);

                                        /*intent.putExtra("data", data);
                                        intent.putExtra("count", QuestionCount);
                                        intent.putExtra("index", index + 1);
                                        intent.putExtra("progress", prog.getProgress());*/

            int mod = 0;
            mod = getIntent().getExtras().getInt("module");
            int exc = 0;
            exc = getIntent().getExtras().getInt("exercise");
            int qBlock = 0;
            qBlock = getIntent().getExtras().getInt("questionBlock");
            Log.w(TAG, "ModRawr:" + mod + " : " + exc);

            intent.putExtra("module", mod);
            intent.putExtra("exercise", exc);
            intent.putExtra("questionBlock", qBlock);
            intent.putExtra("count", QuestionCount);
            //intent.putIntegerArrayListExtra("correctCount", correctCount);
            //intent.putIntegerArrayListExtra("incorrectCount", incorrectCount);
            startActivity(intent);
            // !! Consider changing this animation to be something else
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        }
    }

}
