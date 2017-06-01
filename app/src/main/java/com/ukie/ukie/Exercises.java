package com.ukie.ukie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

    ArrayList<String> data = new ArrayList<String>();

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

                FirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.w(TAG, snapshot.getValue().toString());
                        Intent intent = new Intent(getApplicationContext(), Questions.class);
                        JSONObject tmp = null;
                        try {
                            tmp = new JSONObject(snapshot.getValue().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            ExerciseCount = tmp.getInt("ExerciseNum");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int d = 1; d <= ExerciseCount; d++) {
                                try {
                                    QuestionCount = (tmp.getJSONObject("Exercise" + String.valueOf(d)).getInt("QuestionNum"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                for (int i = 1; i <= QuestionCount; i++) {
                                    try {
                                        data.add((tmp.getJSONObject("Exercise" + String.valueOf(d))).getJSONObject("Question" + String.valueOf(i)).toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.w(TAG, "HAI");
                                }
                            }
                        intent.putStringArrayListExtra("data", data);
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