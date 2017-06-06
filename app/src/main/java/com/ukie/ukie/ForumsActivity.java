package com.ukie.ukie;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForumsActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;

    DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null) {

            FirebaseRef.child("forums").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.w(TAG, snapshot.getValue().toString());
                    JSONObject tmp = null;
                    ArrayList<String> strArr = new ArrayList<String>();
                    try {
                        tmp = new JSONObject(snapshot.getValue().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (String i : tmp) {
                        strArr.add(i);
                    }

                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArr.toArray());
                    ListView listView = (ListView) findViewById(R.id.lvItems);
                    listView.setAdapter(itemsAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        }
        else {
            Toast.makeText(this, "You are not signed in, please go sign in now.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, google_login.class);
            startActivity(intent);
        }

    }

}
