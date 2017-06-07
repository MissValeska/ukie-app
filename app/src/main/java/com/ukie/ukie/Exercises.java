package com.ukie.ukie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Exercises extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    String url ="https://ukie.herokuapp.com/";

    ArrayList<questionData> data = new ArrayList<questionData>();

    int QuestionCount = 0;
    int ExerciseCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageIcon();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

                FirebaseRef.child("Exercises").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.w(TAG, snapshot.getValue().toString());
                        String firstType = null;

                        ExerciseCount = snapshot.child("ExerciseNum").getValue(int.class);
                        Log.w(TAG, String.valueOf(ExerciseCount));

                        for (int d = 1; d <= ExerciseCount; d++) {
                            QuestionCount = snapshot.child("Exercise" + String.valueOf(d)).child("QuestionNum").getValue(int.class);
                                for (int i = 1; i <= QuestionCount; i++) {
                                    questionData tmp = new questionData();
                                    tmp.conj = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("conj").getValue(String.class);
                                    tmp.conjtype = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("conjtype").getValue(String.class);
                                    tmp.infin = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("infin").getValue(String.class);
                                    tmp.type = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                    tmp.correctAnswer = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("correctAnswer").getValue(int.class);
                                    for(int x = 0; x < 4; x++) {
                                        tmp.qAudio[x] = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("qAudio" + String.valueOf(x+1)).getValue(String.class);
                                        tmp.qImg[x] = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("qImg" + String.valueOf(x+1)).getValue(String.class);
                                    }
                                    tmp.questionText = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("questionText").getValue(String.class);
                                    data.add(tmp);
                                    //data.add(snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).getValue().toString());
                                    //Log.w(TAG, data.get(i-1));
                                    if(i == 1) {
                                        firstType = snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("type").getValue(String.class);
                                    }
                                    /*if(snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("type").getValue(String.class) == "audio" || snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("type").getValue(String.class) == "image") {
                                        typeList.add(snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("type").getValue(String.class));
                                        urlArr.add(snapshot.child("Exercise" + String.valueOf(d)).child("Question" + String.valueOf(i)).child("type").getValue(String.class));
                                    }*/
                                    Log.w(TAG, "HAI");
                                }
                            }
                        Log.w(TAG, firstType);
                        Intent intent = null;
                        if(firstType.compareToIgnoreCase("text") == 0) {
                            intent = new Intent(getApplicationContext(), Questions.class);
                        }
                        if(firstType.compareToIgnoreCase("audio") == 0 || firstType.compareToIgnoreCase("image") == 0) {
                            intent = new Intent(getApplicationContext(), ImageQuestions.class);
                        }
                        /*if(firstType.compareToIgnoreCase("dropdown") == 0) {
                            intent = new Intent(getApplicationContext(), ImageQuestions.class); // As of yet, non-existent
                        }*/
                        Log.w(TAG, String.valueOf(data.size()));
                        intent.putExtra("data", data);
                        intent.putExtra("count", QuestionCount);
                        intent.putExtra("index", 0);
                        intent.putExtra("progress", 0);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

    }

    @Override
    protected void onStop () {
        super.onStop();
    }

}