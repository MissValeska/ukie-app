package com.ukie.ukie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaDrm;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class radioQuestions extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    MediaPlayer q1mp;

    ArrayList<Integer> correctCount;
    ArrayList<Integer> incorrectCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*try {
            q1mp = new MediaPlayer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        Intent intent = getIntent();

        correctCount = intent.getIntegerArrayListExtra("correctCount");

        incorrectCount = intent.getIntegerArrayListExtra("incorrectCount");

        data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        Log.w(TAG, "Entered Radio:" + data.size() + " : " + data.toString());

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        prog = (ProgressBar) findViewById(R.id.progressBar5);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        final String audioURL = data.get(index).getString("radioAudio");

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
        final String radioImg = data.get(index).getString("radioImg");

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
            final Object tmp = qStuff[0];
            final FutureTask task = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    Log.v(TAG, "Entered future task radioImage");
                    Picasso.with(((ImageButton)tmp).getContext()).load(radioImg).fit().centerCrop().into((ImageButton)tmp);
                    ((ImageButton)tmp).setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                    ((ImageButton)tmp).setVisibility(View.VISIBLE);
                    return null;
                }
            });
            task.run();
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

        /*try {
            Log.v(TAG, "radio URL:" + String.valueOf(audioURL));
            q1mp.setDataSource(audioURL);
            q1mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        final FutureTask task1 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                Log.v(TAG, "Entered future task");
                try {
                    q1mp = new MediaPlayer();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    q1mp.setDataSource(audioURL);
                    // !! Fix this so that it doesn't hang the program on a black screen for thirty plus seconds if the URL is invalid
                    q1mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                return q1mp;
            }
        });
        task1.run();

        final int[] c = {0};
        ((View)qStuff[choose]).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                do {
                    if (task1.isDone()) {
                        if (!q1mp.isPlaying()) {
                            q1mp.start();
                        } else if (q1mp.isPlaying()) {
                            q1mp.seekTo(0);
                            q1mp.start();
                        }
                    }
                } while(!task1.isDone());

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
                            intent.putIntegerArrayListExtra("correctCount", correctCount);
                            intent.putIntegerArrayListExtra("incorrectCount", incorrectCount);
                            startActivity(intent);
                            // !! Consider changing this animation to be something else
                            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
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

    public void onUpButtonPressed() {

        onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                onUpButtonPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "Back button pressed! Omg!");
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Are you sure you want to exit? You will lose all progress.")
                    .setTitle("Exit Lesson");

// Add the buttons
            builder.setPositiveButton("Yes, I'm sure (Так)", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Intent upIntent = NavUtils.getParentActivityIntent(radioQuestions.this);

                    int mod = 0;
                    mod = getIntent().getExtras().getInt("module");
                    int exc = 0;
                    exc = getIntent().getExtras().getInt("exercise");
                    int qBlock = 0;
                    qBlock = getIntent().getExtras().getInt("questionBlock");
                    Log.w(TAG, "ModRawr:" + mod + " : " + exc);

                    upIntent.putExtra("module", mod);
                    upIntent.putExtra("exercise", exc);
                    upIntent.putExtra("questionBlock", qBlock);
                    upIntent.putExtra("count", QuestionCount);
                    upIntent.putIntegerArrayListExtra("correctCount", correctCount);
                    upIntent.putIntegerArrayListExtra("incorrectCount", incorrectCount);

                    if (NavUtils.shouldUpRecreateTask(radioQuestions.this, upIntent)) {
                        // This activity is NOT part of this app's task, so create a new task
                        // when navigating up, with a synthesized back stack.
                        TaskStackBuilder.create(radioQuestions.this)
                                // Add all of this activity's parents to the back stack
                                .addNextIntentWithParentStack(upIntent)
                                // Navigate up to the closest parent
                                .startActivities();
                        overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                        dialog.cancel();
                    } else {
                        // This activity is part of this app's task, so simply
                        // navigate up to the logical parent activity.
                        NavUtils.navigateUpTo(radioQuestions.this, upIntent);
                        // !! Fix these weird and broken animations
                        overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                        dialog.cancel();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Log.v(TAG, "regular cancel");
                    dialog.cancel();
                }
            });

            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.v(TAG, "back button cancel");
                        dialog.cancel();
                    }
                    return false;
                }
            });


// Set other dialog properties


// Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        q1mp.stop();
        q1mp.release();
    }

}
