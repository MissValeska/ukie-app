package com.ukie.ukie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class QuestionBlock extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    ArrayList<questionData> data = new ArrayList<questionData>();

    int QuestionCount = 0;
    int QuestionBlockCount = 0;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_block);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Intent rawr = new Intent(getApplicationContext(), DragQuestions.class);

        //startActivity(rawr);

        final Intent intent = getIntent();
        // !! Whatever data is passed likely doesn't matter
        //data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        class MyListAdapter extends ArrayAdapter<ExerciseData> {

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
                Log.w(TAG, "pos:" + position);
                if(position+1 == exercise.getQuestionBlock()) {
                        //imgBtn.setForegroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.md_amber_700)));
                        imgBtn.setBackgroundColor(getResources().getColor(R.color.md_amber_700));
                }
                Log.w(TAG, "Adapter view:" + exercise.getText() + " : " + exercise.getUrl());
                imgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w(TAG, "Does this work?");

                        /*FutureTask future = new FutureTask(new Callable() {
                            @Override
                            public Object call() throws Exception {*/


                        final long time1= System.currentTimeMillis();


                        FirebaseRef.child("Modules").child("Module" + String.valueOf(exercise.getModule())).child("Exercise" + String.valueOf(exercise.getExercise())).child("QuestionBlock" + String.valueOf(position+1)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                long time2= System.currentTimeMillis();
                                Log.w(TAG, "Time differential:" + String.valueOf(time2-time1));
                                if(snapshot.exists()) {
                                    Log.w(TAG, snapshot.getValue().toString());
                                    String firstType = null;

                                    Log.w(TAG, "Mod:" + exercise.getModule() + " : " + exercise.getExercise());

                                    QuestionCount = snapshot.child("QuestionNum").getValue(int.class);






                                    Log.w(TAG, String.valueOf(QuestionBlockCount));
                                    for (int i = 1; i <= QuestionCount; i++) {
                                        Log.w(TAG, snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class));
                                        Gson gson = new Gson();
                                        if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("audio") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.qAudio1 = snapshot.child("Question" + String.valueOf(i)).child("qAudio1").getValue(String.class);
                                            tmp.qAudio2 = snapshot.child("Question" + String.valueOf(i)).child("qAudio2").getValue(String.class);
                                            tmp.qAudio3 = snapshot.child("Question" + String.valueOf(i)).child("qAudio3").getValue(String.class);
                                            tmp.qAudio4 = snapshot.child("Question" + String.valueOf(i)).child("qAudio4").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            data.add(tmp);
                                        } else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("image") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.qAudio1 = snapshot.child("Question" + String.valueOf(i)).child("qAudio1").getValue(String.class);
                                            tmp.qAudio2 = snapshot.child("Question" + String.valueOf(i)).child("qAudio2").getValue(String.class);
                                            tmp.qAudio3 = snapshot.child("Question" + String.valueOf(i)).child("qAudio3").getValue(String.class);
                                            tmp.qAudio4 = snapshot.child("Question" + String.valueOf(i)).child("qAudio4").getValue(String.class);
                                            tmp.qImg1 = snapshot.child("Question" + String.valueOf(i)).child("qImg1").getValue(String.class);
                                            tmp.qImg2 = snapshot.child("Question" + String.valueOf(i)).child("qImg2").getValue(String.class);
                                            tmp.qImg3 = snapshot.child("Question" + String.valueOf(i)).child("qImg3").getValue(String.class);
                                            tmp.qImg4 = snapshot.child("Question" + String.valueOf(i)).child("qImg4").getValue(String.class);
                                            tmp.img1Text = snapshot.child("Question" + String.valueOf(i)).child("img1Text").getValue(String.class);
                                            tmp.img2Text = snapshot.child("Question" + String.valueOf(i)).child("img2Text").getValue(String.class);
                                            tmp.img3Text = snapshot.child("Question" + String.valueOf(i)).child("img3Text").getValue(String.class);
                                            tmp.img4Text = snapshot.child("Question" + String.valueOf(i)).child("img4Text").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            data.add(tmp);
                                        } else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("dropdown") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.questionText1 = snapshot.child("Question" + String.valueOf(i)).child("questionText1").getValue(String.class);
                                            tmp.questionText2 = snapshot.child("Question" + String.valueOf(i)).child("questionText2").getValue(String.class);
                                            tmp.dropDown1 = snapshot.child("Question" + String.valueOf(i)).child("dropDown1").getValue(String.class);
                                            tmp.dropDown2 = snapshot.child("Question" + String.valueOf(i)).child("dropDown2").getValue(String.class);
                                            data.add(tmp);
                                        } else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("radioText") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.radioAudio = snapshot.child("Question" + String.valueOf(i)).child("radioAudio").getValue(String.class);
                                            tmp.letter = snapshot.child("Question" + String.valueOf(i)).child("letter").getValue(String.class);
                                            tmp.radioText1 = snapshot.child("Question" + String.valueOf(i)).child("radioText1").getValue(String.class);
                                            tmp.radioText2 = snapshot.child("Question" + String.valueOf(i)).child("radioText2").getValue(String.class);
                                            tmp.radioText3 = snapshot.child("Question" + String.valueOf(i)).child("radioText3").getValue(String.class);
                                            tmp.radioText4 = snapshot.child("Question" + String.valueOf(i)).child("radioText4").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            data.add(tmp);
                                        } else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("radioImage") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.radioAudio = snapshot.child("Question" + String.valueOf(i)).child("radioAudio").getValue(String.class);
                                            tmp.radioImg = snapshot.child("Question" + String.valueOf(i)).child("radioImg").getValue(String.class);
                                            tmp.radioText1 = snapshot.child("Question" + String.valueOf(i)).child("radioText1").getValue(String.class);
                                            tmp.radioText2 = snapshot.child("Question" + String.valueOf(i)).child("radioText2").getValue(String.class);
                                            tmp.radioText3 = snapshot.child("Question" + String.valueOf(i)).child("radioText3").getValue(String.class);
                                            tmp.radioText4 = snapshot.child("Question" + String.valueOf(i)).child("radioText4").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            data.add(tmp);
                                        }
                                        else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("radio") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.radioAudio = snapshot.child("Question" + String.valueOf(i)).child("radioAudio").getValue(String.class);
                                            tmp.radioText1 = snapshot.child("Question" + String.valueOf(i)).child("radioText1").getValue(String.class);
                                            tmp.radioText2 = snapshot.child("Question" + String.valueOf(i)).child("radioText2").getValue(String.class);
                                            tmp.radioText3 = snapshot.child("Question" + String.valueOf(i)).child("radioText3").getValue(String.class);
                                            tmp.radioText4 = snapshot.child("Question" + String.valueOf(i)).child("radioText4").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            data.add(tmp);
                                        }
                                        else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("drag") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.correctAnswer = snapshot.child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                            tmp.qImg1 = snapshot.child("Question" + String.valueOf(i)).child("qImg1").getValue(String.class);
                                            tmp.qImg2 = snapshot.child("Question" + String.valueOf(i)).child("qImg2").getValue(String.class);
                                            tmp.qImg3 = snapshot.child("Question" + String.valueOf(i)).child("qImg3").getValue(String.class);
                                            tmp.qImg4 = snapshot.child("Question" + String.valueOf(i)).child("qImg4").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            data.add(tmp);
                                            Log.v(TAG, "What's going on?");
                                        }
                                        else if (snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class).compareToIgnoreCase("pairs") == 0) {
                                            questionData tmp = new questionData();
                                            tmp.type = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                            tmp.questionText = snapshot.child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                            // !! Add voice clips which are played when the TextViews are selected
                                            // !! Add text to put into the TextViews
                                            tmp._1pair1 = snapshot.child("Question" + String.valueOf(i)).child("1pair1").getValue(int.class);
                                            tmp._2pair1 = snapshot.child("Question" + String.valueOf(i)).child("2pair1").getValue(int.class);
                                            tmp._1pair2 = snapshot.child("Question" + String.valueOf(i)).child("1pair2").getValue(int.class);
                                            tmp._2pair2 = snapshot.child("Question" + String.valueOf(i)).child("2pair2").getValue(int.class);
                                            tmp._1pair3 = snapshot.child("Question" + String.valueOf(i)).child("1pair3").getValue(int.class);
                                            tmp._2pair3 = snapshot.child("Question" + String.valueOf(i)).child("2pair3").getValue(int.class);
                                            tmp._1pair4 = snapshot.child("Question" + String.valueOf(i)).child("1pair4").getValue(int.class);
                                            tmp._2pair4 = snapshot.child("Question" + String.valueOf(i)).child("2pair4").getValue(int.class);
                                            tmp._1pair5 = snapshot.child("Question" + String.valueOf(i)).child("1pair5").getValue(int.class);
                                            tmp._2pair5 = snapshot.child("Question" + String.valueOf(i)).child("2pair5").getValue(int.class);
                                            tmp._1pair6 = snapshot.child("Question" + String.valueOf(i)).child("1pair6").getValue(int.class);
                                            tmp._2pair6 = snapshot.child("Question" + String.valueOf(i)).child("2pair6").getValue(int.class);
                                            data.add(tmp);
                                            Log.v(TAG, "What's going on?");
                                        }
                                        else {
                                            data.add(gson.fromJson(snapshot.child("Question" + String.valueOf(i)).getValue().toString(), questionData.class));
                                        }
                                        if (i == 1) {
                                            firstType = snapshot.child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                        }
                                        Log.w(TAG, "HAI");
                                    }
                                    Log.w(TAG, firstType);
                                    Intent intent = null;
                                    if (firstType.compareToIgnoreCase("text") == 0) {
                                        intent = new Intent(getApplicationContext(), Questions.class);
                                    }
                                    else if (firstType.compareToIgnoreCase("audio") == 0 || firstType.compareToIgnoreCase("image") == 0) {
                                        intent = new Intent(getApplicationContext(), ImageQuestions.class);
                                    }
                                    else if (firstType.compareToIgnoreCase("dropdown") == 0) {
                                        intent = new Intent(getApplicationContext(), dropdownQuestions.class);
                                    }
                                    else if (firstType.contains("radio")) {
                                        intent = new Intent(getApplicationContext(), radioQuestions.class);
                                    }
                                    else if (firstType.compareToIgnoreCase("drag") == 0) {
                                        intent = new Intent(getApplicationContext(), DragQuestions.class);
                                    }
                                    else if (firstType.compareToIgnoreCase("pairs") == 0) {
                                        intent = new Intent(getApplicationContext(), pairingQuestions.class);
                                    }

                                    Log.w(TAG, String.valueOf(data.size()));
                                    intent.putExtra("data", data);
                                    intent.putExtra("count", QuestionCount);
                                    intent.putExtra("index", 0);
                                    intent.putExtra("progress", 0);
                                    // !! Use the following values in QuestionBlock.java
                                    intent.putExtra("module", exercise.getModule());
                                    intent.putExtra("exercise", exercise.getExercise());
                                    intent.putExtra("questionBlock", exercise.getQuestionBlock());

                                    ArrayList<Integer> correctCount = new ArrayList<Integer>();
                                    ArrayList<Integer> incorrectCount = new ArrayList<Integer>();

                                    intent.putIntegerArrayListExtra("correctCount", correctCount);
                                    intent.putIntegerArrayListExtra("incorrectCount", incorrectCount);
                                    long time3= System.currentTimeMillis();
                                    Log.w(TAG, "Time differential1:" + String.valueOf(time3-time1) + "Time differential2:" + String.valueOf(time3-time2));
                                    startActivity(intent);
                                }
                                else {
                                    Log.w(TAG, "Interesting, apparently snapshot doesn't exist?" + snapshot.toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                             /*   return null;
                            }
                        });*/
                    }
                });

                return exerciseView;
            }

        }

        int mod = 0;
        mod = intent.getIntExtra("module", mod);
        int exc = 0;
        exc = intent.getIntExtra("exercise", exc);

        final int finalExc = exc;
        final int finalMod = mod;
        FirebaseRef.child("Modules").child("Module" + String.valueOf(mod)).child("Exercise" + String.valueOf(exc)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.w(TAG, snapshot.getValue().toString());
                    String firstType = null;

                    QuestionBlockCount = snapshot.child("QuestionBlockNum").getValue(int.class);
                    Log.w(TAG, String.valueOf(QuestionBlockCount));

                    ArrayList<ExerciseData> dataList = new ArrayList<ExerciseData>();

                    for (int d = 1; d <= QuestionBlockCount; d++) {
                        String url = snapshot.child("QuestionBlock" + String.valueOf(d)).child("QuestionBlockData").child("url").getValue(String.class);
                        String text = snapshot.child("QuestionBlock" + String.valueOf(d)).child("QuestionBlockData").child("text").getValue(String.class);
                        //int exercise = snapshot.child("QuestionBlock" + String.valueOf(d)).child("QuestionBlockData").child("Exercise").getValue(int.class);
                        dataList.add(new ExerciseData(url, text, finalExc, finalMod, d));
                        Log.w(TAG, "Yo:" + dataList.get(d - 1).getText() + " : " + dataList.get(d - 1).getUrl() + " : " + dataList.get(d - 1).getExercise());
                    }

                    MyListAdapter itemsAdapter =
                            new MyListAdapter(QuestionBlock.this, R.layout.exercise_list_item, dataList);
                    ListView listView = (ListView) findViewById(R.id.QuestionBlockListView);
                    listView.setAdapter(itemsAdapter);
                } else {
                    Log.w(TAG, "Interesting, apparently snapshot doesn't exist?" + snapshot.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onUpButtonPressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(QuestionBlock.this);


        upIntent.putExtra("module", getIntent().getExtras().getInt("module"));

        if (NavUtils.shouldUpRecreateTask(QuestionBlock.this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(QuestionBlock.this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(QuestionBlock.this, upIntent);
            // !! Fix these weird and broken animations
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        }
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
        onUpButtonPressed();
    }

}
