package com.ukie.ukie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class radioQuestions extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        Log.w(TAG, "Entered Radio:" + data.size() + " : " + data.toString());

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        prog = (ProgressBar) findViewById(R.id.progressBar5);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        String audioURL = data.get(index).getString("radioAudio");

        final int correctAnswr = data.get(index).getInt("correctAnswer") - 1;

        final TextView txt = (TextView) findViewById(R.id.textView16);

        final RadioButton radio1 = (RadioButton) findViewById(R.id.radioButton1);
        final RadioButton radio2 = (RadioButton) findViewById(R.id.radioButton2);
        final RadioButton radio3 = (RadioButton) findViewById(R.id.radioButton3);
        final RadioButton radio4 = (RadioButton) findViewById(R.id.radioButton4);

        txt.setText(data.get(index).getString("questionText"));

        radio1.setText(data.get(index).getString("radioText1"));
        radio2.setText(data.get(index).getString("radioText2"));
        radio3.setText(data.get(index).getString("radioText3"));
        radio4.setText(data.get(index).getString("radioText4"));
        String type = data.get(index).getString("type");
        String letter = data.get(index).getString("letter");
        String radioImg = data.get(index).getString("radioImg");

        Button checkBtn = (Button) findViewById(R.id.checkRadio);

        Object img = null;
        Object text = null;
        int choose = -1;
        Object[] qStuff = {img, text};

        if(type.compareToIgnoreCase("radioImage") == 0) {
            choose = 0;
            qStuff[choose] = findViewById(R.id.imageButton);
            //((ImageButton)qStuff[choose]).setMinimumHeight(200);
            //((ImageButton)qStuff[choose]).setMinimumWidth(200);
            //((ImageButton)qStuff[choose]).set
            Picasso.with(((ImageButton)qStuff[0]).getContext()).load(radioImg).fit().centerCrop().into((ImageButton)qStuff[0]);
            ((ImageButton)qStuff[choose]).setBackgroundColor(getResources().getColor(R.color.tw__transparent));
            ((ImageButton)qStuff[choose]).setVisibility(View.VISIBLE);
        }
        else if(type.compareToIgnoreCase("radioText") == 0) {
            choose = 1;
            qStuff[choose] = findViewById(R.id.button4);
            ((Button)qStuff[choose]).setText(letter);
            ((Button)qStuff[choose]).setTextSize(200);
            ((Button)qStuff[choose]).setBackgroundColor(getResources().getColor(R.color.tw__transparent));
            ((Button)qStuff[choose]).setVisibility(View.VISIBLE);
        }
        else if(type.compareToIgnoreCase("radio") == 0) {
            choose = 0;
            qStuff[choose] = findViewById(R.id.imageButton);
            ((ImageButton)qStuff[choose]).setImageDrawable(new IconicsDrawable(radioQuestions.this, GoogleMaterial.Icon.gmd_volume_up).backgroundColor(getResources().getColor(R.color.tw__transparent)).color(getResources().getColor(R.color.md_light_blue_500)));
            ((ImageButton)qStuff[choose]).setBackgroundColor(getResources().getColor(R.color.tw__transparent));
            ((ImageButton)qStuff[choose]).setVisibility(View.VISIBLE);
        }

        final MediaPlayer q1mp = new MediaPlayer();
        try {
            q1mp.setDataSource(audioURL);
            q1mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((View)qStuff[choose]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!q1mp.isPlaying()) {
                    q1mp.start();
                } else if(q1mp.isPlaying()) {
                    q1mp.seekTo(0);
                    q1mp.start();
                }

            }
        });

        radio1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radio2.setChecked(false);
                    radio3.setChecked(false);
                    radio4.setChecked(false);
                }
            }
        });

        radio2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radio1.setChecked(false);
                    radio3.setChecked(false);
                    radio4.setChecked(false);
                }
            }
        });

        radio3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio4.setChecked(false);
                }
            }
        });

        radio4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radio1.setChecked(false);
                    radio2.setChecked(false);
                    radio3.setChecked(false);
                }
            }
        });



        final int[] buttonPressCount = {0};

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(radioQuestions.this);

        final ArrayList<Integer> correctCount = intent.getIntegerArrayListExtra("correctCount");
        final ArrayList<Integer> incorrectCount = intent.getIntegerArrayListExtra("incorrectCount");

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "Button stuff!");
                boolean[] checkList = {radio1.isChecked(), radio2.isChecked(), radio3.isChecked(), radio4.isChecked()};
                if(checkList[correctAnswr]) {
                    if(buttonPressCount[0] == 0) {
                        if(index + 1 != QuestionCount) {
                            ((Button) v).setText("Next");
                        }
                        else {
                            ((Button) v).setText("Finish");
                        }
                        prog.setProgress(prog.getProgress() + 1);
                        Toast.makeText(radioQuestions.this, "You are correct!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (index + 1 != QuestionCount) {

                            Intent intent = null;
                            if (data.get(index + 1).getString("type").compareToIgnoreCase("text") == 0) {
                                intent = new Intent(getApplicationContext(), Questions.class);
                            }
                            if (data.get(index + 1).getString("type").compareToIgnoreCase("audio") == 0 || data.get(index + 1).getString("type").compareToIgnoreCase("image") == 0) {
                                intent = new Intent(getApplicationContext(), ImageQuestions.class);
                            }
                            if (data.get(index + 1).getString("type").compareToIgnoreCase("dropdown") == 0) {
                                intent = new Intent(getApplicationContext(), dropdownQuestions.class);
                            }
                            if (data.get(index + 1).getString("type").contains("radio")) {
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

                            if(correctCount.size() == 0)  {
                                correctCount.add(1);
                            }
                            else if(correctCount.size() >= index+1) {
                                correctCount.set(index, correctCount.get(index) + 1);
                            }
                            else {
                                correctCount.add(index, 1);
                            }

                            Log.w(TAG, "Mod:" + mod + " : " + exc);
                            intent.putExtra("module", mod);
                            intent.putExtra("exercise", exc);
                            intent.putIntegerArrayListExtra("correctCount", correctCount);
                            intent.putIntegerArrayListExtra("incorrectCount", incorrectCount);
                            //intent.putExtra("completed", cmp);
                            startActivity(intent);
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
                            intent.putIntegerArrayListExtra("correctCount", correctCount);
                            intent.putIntegerArrayListExtra("incorrectCount", incorrectCount);
                            startActivity(intent);
                        }
                    }
                }
                else {
                    Toast.makeText(radioQuestions.this, "You are incorrect, please try again.",
                            Toast.LENGTH_SHORT).show();
                    if(incorrectCount.size() == 0)  {
                        incorrectCount.add(1);
                    }
                    else if(incorrectCount.size() >= index+1) {
                        incorrectCount.set(index, incorrectCount.get(index) + 1);
                    }
                    else {
                        incorrectCount.add(index, 1);
                    }
                }
                buttonPressCount[0]++;
            }
        });

    }

}
