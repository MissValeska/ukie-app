package com.ukie.ukie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    ArrayList<questionData> data = new ArrayList<questionData>();

    int ModuleCount = 0;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        }

        FirebaseRef.child("Modules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.w(TAG, snapshot.getValue().toString());

                    ModuleCount = snapshot.child("ModuleNum").getValue(int.class);
                    Log.w(TAG, String.valueOf(ModuleCount));
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    Log.w(TAG, "Is logged in?:" + String.valueOf(mAuth.getCurrentUser()));

                    ArrayList<ExerciseData> dataList = new ArrayList<ExerciseData>();

                    for (int d = 1; d <= ModuleCount; d++) {
                        String url = snapshot.child("Module" + String.valueOf(d)).child("ModuleData").child("url").getValue(String.class);
                        String text = snapshot.child("Module" + String.valueOf(d)).child("ModuleData").child("text").getValue(String.class);
                        dataList.add(new ExerciseData(url, text, -1, d, -1));
                        Log.w(TAG, "Yo:" + dataList.get(d - 1).getText() + " : " + dataList.get(d - 1).getUrl() + " : " + dataList.get(d - 1).getExercise());
                    }

                    MyListAdapter itemsAdapter =
                            new MyListAdapter(ModuleActivity.this, R.layout.exercise_list_item, dataList);
                    ListView listView = (ListView) findViewById(R.id.ModuleListView);
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
        Intent upIntent = NavUtils.getParentActivityIntent(ModuleActivity.this);

        if (NavUtils.shouldUpRecreateTask(ModuleActivity.this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(ModuleActivity.this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(ModuleActivity.this, upIntent);
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
