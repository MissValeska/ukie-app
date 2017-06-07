package com.ukie.ukie;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

public class ForumsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;

    ArrayList<String> forumsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView rawrTxt = (TextView) findViewById(R.id.rawrText);

        rawrTxt.setText("Ж");
        rawrTxt.setTextSize(500);
        //startActivity(new Intent(this, ImageQuestions.class));

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() != null) {

            FirebaseRef.child("forums").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.w(TAG, snapshot.getValue().toString());

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Log.d(TAG, "STUFF" + child.getKey());
                        forumsList.add(child.getKey());
                    }

                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(ForumsActivity.this, android.R.layout.simple_list_item_1, forumsList);
                    ListView listView = (ListView) findViewById(R.id.lvItems);
                    listView.setAdapter(itemsAdapter);

                    listView.setOnItemClickListener(ForumsActivity.this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(TAG, parent.getItemAtPosition(position).toString());

    }
}
