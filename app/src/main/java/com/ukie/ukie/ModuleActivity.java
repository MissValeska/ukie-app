package com.ukie.ukie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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

                    ArrayList<ExerciseData> dataList = new ArrayList<ExerciseData>();

                    for (int d = 1; d <= ModuleCount; d++) {
                        String url = snapshot.child("Module" + String.valueOf(d)).child("ModuleData").child("text").getValue(String.class);
                        String text = snapshot.child("Module" + String.valueOf(d)).child("ModuleData").child("url").getValue(String.class);
                        dataList.add(new ExerciseData(text, url, -1, d, -1));
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

}
