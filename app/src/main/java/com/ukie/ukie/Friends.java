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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Friends extends AppCompatActivity implements View.OnKeyListener {

    private FirebaseAuth mAuth;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        /*FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.w(TAG, snapshot.getValue().toString());

                JSONObject tmp = null;
                try {
                    tmp = new JSONObject(snapshot.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.w(TAG, snapshot.getValue().toString());

                JSONObject tmp = null;
                try {
                    tmp = new JSONObject(snapshot.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = null;

                try {
                    jsonArray = tmp.getJSONArray("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String[] StringArray = new String[jsonArray.length()];

                try {
                    for(int i = 0; i < jsonArray.length(); i++) {
                        StringArray[i] = jsonArray.get(i).toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_friends, StringArray);

                ListView listView = (ListView) findViewById(R.id.friendsList);

                listView.setAdapter(adapter);


                //listview.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.w(TAG, snapshot.getValue().toString());

                JSONObject tmp = null;
                try {
                    tmp = new JSONObject(snapshot.getValue().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/










        Log.w(TAG, "User ID:" + mAuth.getCurrentUser().getUid());

        EditText findFriend = (EditText) findViewById(R.id.findFriend);

        findFriend.setOnKeyListener(this);


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            Query query = null;

            if(android.util.Patterns.EMAIL_ADDRESS.matcher(((EditText) v).getText().toString()).matches()) {
                query = FirebaseRef.child("users").orderByChild("email").equalTo(((EditText) v).getText().toString());
            }
            else {
                query = FirebaseRef.child("users").orderByChild("username").equalTo(((EditText) v).getText().toString());
            }

            final String tmp = ((EditText) v).getText().toString();

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        ((TextView) findViewById(R.id.textView5)).setText("This user does not exist, please enter another.");
                    }
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.w(TAG, snapshot.getValue().toString());
                        Log.w(TAG, snapshot.child("username").getValue(String.class));

                        Log.w(TAG, snapshot.child("uid").getValue(String.class));

                        Query query = FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).orderByChild("friends").equalTo(tmp);
                        // !! Modify to use a for each loop, in case multiple users with the same information exist. (Try to minimize possible crash scenarios overall)
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.w(TAG, "HAI!");
                                Log.w(TAG, String.valueOf(dataSnapshot.exists()));
                                if(dataSnapshot.exists()) {
                                    Log.w(TAG, dataSnapshot.getValue().toString());
                                    Log.w(TAG, dataSnapshot.child("username").getValue(String.class));
                                    Log.w(TAG, dataSnapshot.child("uid").getValue(String.class));
                                    ((TextView) findViewById(R.id.textView5)).setText("You are already friends.");
                                }
                                else {
                                    FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("friends").child(snapshot.child("uid").getValue(String.class)).push().setValue(snapshot.child("uid").getValue(String.class));
                                    //FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("friends").setValue(snapshot.child("uid").getValue(String.class));
                                    FirebaseRef.child("users").child(snapshot.child("uid").getValue(String.class)).child("friends").setValue(mAuth.getCurrentUser().getUid());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
        }
        return false;
    }
}
