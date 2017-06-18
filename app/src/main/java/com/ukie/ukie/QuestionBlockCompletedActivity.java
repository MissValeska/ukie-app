package com.ukie.ukie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QuestionBlockCompletedActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    ArrayList<questionData> data = new ArrayList<questionData>();

    int QuestionCount = 0;
    int ModuleCount = 0;
    int ExerciseCount = 0;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_block_completed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();

        final ArrayList<Integer> correctCount = intent.getIntegerArrayListExtra("correctCount");
        final ArrayList<Integer> incorrectCount = intent.getIntegerArrayListExtra("incorrectCount");

        int qBlock = getIntent().getExtras().getInt("questionBlock");
        ModuleCount = getIntent().getExtras().getInt("module");
        ExerciseCount = getIntent().getExtras().getInt("exercise");

        QuestionCount = intent.getIntExtra("count", QuestionCount);

        //QuestionBlockStatListView

        /*class MyListAdapter extends ArrayAdapter<ExerciseData> {

            int resource;

            public MyListAdapter(@NonNull Context context, int resource, @NonNull List<ExerciseData> dataList) {
                super(context, resource, dataList);

                Log.w(TAG, "Adapter view constructor:" + dataList.get(0).getText() + " : " + dataList.get(0).getUrl() + " and the resource:" + resource);

                this.resource=resource;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // To be sure we have a view, because null is possible

                final LinearLayout exerciseView;
                final ExerciseData exercise = getItem(position);
                if(convertView==null) {
                    exerciseView = new LinearLayout(getContext());
                    String inflater = Context.LAYOUT_INFLATER_SERVICE;
                    LayoutInflater vi;
                    vi = (LayoutInflater)getContext().getSystemService(inflater);
                    vi.inflate(resource, exerciseView, true);
                } else {
                    exerciseView = (LinearLayout) convertView;
                }
                TextView imgText =(TextView)exerciseView.findViewById(R.id.text01);
                imgText.setTypeface(null, Typeface.BOLD);
                imgText.setTextSize(TypedValue.COMPLEX_UNIT_PX,18);
                ImageButton imgBtn = (ImageButton)exerciseView.findViewById(R.id.imgButton);
                imgText.setText(exercise.getText());
                Picasso.with(imgBtn.getContext()).load(exercise.getUrl()).fit().centerCrop().into(imgBtn);
                imgBtn.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                Log.w(TAG, "Adapter view:" + exercise.getText() + " : " + exercise.getUrl());
                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(TAG, "Does this work?");

                        Log.w(TAG, "Mod:" + exercise.getModule());

                        Intent intent = new Intent(getApplicationContext(), Exercises.class);
                        intent.putExtra("module", exercise.getModule());
                        startActivity(intent);
                    }
                });

                return exerciseView;
            }

        }*/

        ArrayList<String> dataList = new ArrayList<String>();

        boolean flag1 = false;
        boolean flag2 = false;

        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        List<PieEntry> entries = new ArrayList<PieEntry>();

        int smallest = Math.min(correctCount.size(), incorrectCount.size());

        int tmpCorrect = 0;

        for (int x : correctCount) {
            tmpCorrect += x;
        }

        int tmpIncorrect = 0;

        for (int x : incorrectCount) {
            tmpIncorrect += x;
        }

        float correctPercentage;
        float incorrectPercentage;
        if(tmpCorrect == 0 && tmpIncorrect != 0) {
            incorrectPercentage = tmpIncorrect / (tmpCorrect + 1);
        }
        else {
            incorrectPercentage = tmpIncorrect / tmpCorrect;
        }
        if(tmpIncorrect == 0 && tmpCorrect != 0) {
            correctPercentage = tmpCorrect / (tmpIncorrect + 1);
        }
        else {
            correctPercentage = tmpCorrect / tmpIncorrect;
        }

        if(tmpCorrect != 0) {
            entries.add(new PieEntry(tmpCorrect, 0));
            entries.get(0).setLabel("Correct");
        }
        if(tmpIncorrect != 0) {
            entries.add(new PieEntry(tmpIncorrect, 1));
            entries.get(1).setLabel("Incorrect");
        }

        PieDataSet dataset = new PieDataSet(entries, "Correct versus Incorrect Answers");

        //pieChart.setLabelFor(0);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataset);
        pieChart.setData(data);

        /*for(int x = 0; x < smallest; x++) {
            try {
                entries.add(new Entry(correctCount.get(x), incorrectCount.get(x)));
            }
            catch (IndexOutOfBoundsException e) {
                /*if(smallest == 0) {
                    if (incorrectCount.size() == 0 && correctCount.size() != 0) {
                        entries.add(new Entry(correctCount.get(x), 0));
                    }
                    if (correctCount.size() == 0 && incorrectCount.size() != 0) {
                        entries.add(new Entry(0, incorrectCount.get(x)));
                    }
                }
                else {
                    if (incorrectCount.size() == 0) {
                        entries.add(new Entry(correctCount.get(x), 0));
                    }
                    if (correctCount.size() == 0) {
                        entries.add(new Entry(0, incorrectCount.get(x)));
                    }
                }*/
                /*entries.add(new Entry(5, 2));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Correct versus Incorrect answers");/* // add entries to dataset
        //dataSet.setColor(...);
        //dataSet.setValueTextColor(...); // styling, ...

        /*LineData lineData = new LineData(dataSet);
        chart.setData(lineData);*/
        //chart.invalidate(); // refresh

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(QuestionBlockCompletedActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int d = 1; d <= QuestionCount; d++) {
            //String url = snapshot.child("Module" + String.valueOf(d)).child("ModuleData").child("text").getValue(String.class);
            //String text = snapshot.child("Module" + String.valueOf(d)).child("ModuleData").child("url").getValue(String.class);
            Log.w(TAG, "Interesting correct stuff:" + String.valueOf(sharedPref.getInt("CorrectQuestion" + d, 0)));
            try {
                dataList.add("Correct Question question count for question " + String.valueOf(d) + ": " + correctCount.get(d - 1));
                editor.putInt("Module" + String.valueOf(ModuleCount) + "Exercise" + String.valueOf(ExerciseCount) + "QuestionBlock" + String.valueOf(qBlock) + "CorrectQuestion" + String.valueOf(d-1), sharedPref.getInt("CorrectQuestion" + String.valueOf(d-1), 0)+correctCount.get(d-1)).apply();
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                dataList.add("Correct Question question count for question " + String.valueOf(d) + ": " + 0);
                editor.putInt("Module" + String.valueOf(ModuleCount) + "Exercise" + String.valueOf(ExerciseCount) + "QuestionBlock" + String.valueOf(qBlock) + "CorrectQuestion" + String.valueOf(d-1), sharedPref.getInt("CorrectQuestion" + String.valueOf(d-1), 0)+0).apply();
            }
            Log.w(TAG, "Interesting incorrect stuff:" + String.valueOf(sharedPref.getInt("IncorrectQuestion" + d, 0)));
            try {
                dataList.add("Incorrect question count for question " + String.valueOf(d) + ": " + incorrectCount.get(d - 1));
                editor.putInt("Module" + String.valueOf(ModuleCount) + "Exercise" + String.valueOf(ExerciseCount) + "QuestionBlock" + String.valueOf(qBlock) + "IncorrectQuestion" + String.valueOf(d-1), sharedPref.getInt("IncorrectQuestion" + String.valueOf(d-1), 0)+incorrectCount.get(d-1)).apply();
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                dataList.add("Incorrect question count for question " + String.valueOf(d) + ": " + 0);
                editor.putInt("Module" + String.valueOf(ModuleCount) + "Exercise" + String.valueOf(ExerciseCount) + "QuestionBlock" + String.valueOf(qBlock) + "IncorrectQuestion" + String.valueOf(d-1), sharedPref.getInt("IncorrectQuestion" + String.valueOf(d-1), 0)+0).apply();
            }

            Log.w(TAG, "QuestionBlockCompleted Correct:" + String.valueOf(sharedPref.getInt("CorrectQuestion" + String.valueOf(d-1), 0)));
            Log.w(TAG, "QuestionBlockCompleted Incorrect:" + String.valueOf(sharedPref.getInt("IncorrectQuestion" + String.valueOf(d-1), 0)));
            //editor.putInt("CorrectQuestion" + String.valueOf(d-1), sharedPref.getInt("CorrectQuestion" + String.valueOf(d-1), 0)+correctCount.get(d-1)).apply();
            //editor.putInt("IncorrectQuestion" + String.valueOf(d-1), sharedPref.getInt("IncorrectQuestion" + String.valueOf(d-1), 0)+incorrectCount.get(d-1)).apply();
            Log.w(TAG, dataList.get(d-1));
            //Log.w(TAG, "Yo:" + dataList.get(d - 1).getText() + " : " + dataList.get(d - 1).getUrl() + " : " + dataList.get(d - 1).getExercise());
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(QuestionBlockCompletedActivity.this, R.layout.simple_list_item_1, dataList);
        ListView listView = (ListView) findViewById(R.id.QuestionBlockStatListView);
        //listView.setAdapter(itemsAdapter);

        Button checkBtn = (Button) findViewById(R.id.button5);

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionBlock.class);

                int mod = 0;
                mod = getIntent().getExtras().getInt("module");
                int exc = 0;
                exc = getIntent().getExtras().getInt("exercise");
                Log.w(TAG, "ModRawr:" + mod + " : " + exc);
                intent.putExtra("module", mod);
                intent.putExtra("exercise", exc);
                startActivity(intent);
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

        // !! Consider changing the message, as no data should be lost AFAIK (ensure this!)

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Are you sure you want to exit? You will lose all progress.")
                .setTitle("Exit Lesson");

// Add the buttons
        builder.setPositiveButton("Yes, I'm sure (Так)", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent upIntent = NavUtils.getParentActivityIntent(QuestionBlockCompletedActivity.this);

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
                // !! Add a question block completed boolean somewhere which is passed to the QuestionBlock activity
                // Indicating that it is completed, and enabling the next QuestionBlock within the Exercise.

                if (NavUtils.shouldUpRecreateTask(QuestionBlockCompletedActivity.this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(QuestionBlockCompletedActivity.this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                    overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                    dialog.cancel();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(QuestionBlockCompletedActivity.this, upIntent);
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

}
