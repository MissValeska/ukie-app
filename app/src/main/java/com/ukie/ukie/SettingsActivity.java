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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // !! Make this into an image button (hopefully it won't crash)
        ImageButton imgBtn = (ImageButton) findViewById(R.id.profileImgButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, String.valueOf(mAuth.getCurrentUser()));

        if(mAuth.getCurrentUser() != null) {
            Picasso.with(imgBtn.getContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(imgBtn);
        }
        else {
            Toast.makeText(this, "You are not signed in, please go sign in now.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, google_login.class);
            startActivity(intent);
        }

    }

}
