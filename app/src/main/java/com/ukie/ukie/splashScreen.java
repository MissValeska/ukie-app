package com.ukie.ukie;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;


public class splashScreen extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    public GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    String serverID = "723836520365-n1bevgandt9o3s35gsnu2het64h50s9a.apps.googleusercontent.com";

    Context cntx;

    public boolean notify = false;

    private FirebaseAuth mAuth;

    Intent toMain = null;

    CallbackManager mCallbackManager;

    SignInButton signInButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        cntx = context;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view =inflater.inflate(R.layout.activity_splash_screen, container, false);

        //setContentView(R.layout.activity_splash_screen);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Log.v(TAG, "Entered Splash Screen");

        mStatusTextView = (TextView) view.findViewById(R.id.status);


        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);

        // Button listeners
        view.findViewById(R.id.sign_in_button).setOnClickListener(this);
        //findViewById(R.id.fb_sign_in_button).setOnClickListener(this);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        SignInButton signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.fb_sign_in_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
                 public void onSuccess(LoginResult loginResult) {
             Log.d(TAG, "facebook:onSuccess:" + loginResult);
             handleFacebookAccessToken(loginResult.getAccessToken());
             }

             @Override
            public void onCancel() {
             Log.d(TAG, "facebook:onCancel");
             // ...
             }

             @Override
             public void onError(FacebookException error) {
             Log.d(TAG, "facebook:onError", error);
            // ...
            }
        });


        silentSignIn();

        return view;

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
              } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                /*Toast.makeText(splashScreen.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();*/
                updateUI(null);
              }

          // ...
        }
      });
    }
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Log.d(TAG, "onConnectionFailed1:" + connectionResult.getErrorMessage() + " : " + connectionResult.getErrorCode());
        //Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private boolean silentSignIn() {

        final boolean[] works = {true};
    if(mGoogleApiClient != null) {
        Log.v(TAG, "Is google connected?" + String.valueOf(mGoogleApiClient.isConnected()));
        if (mGoogleApiClient.isConnected()) {
            ConnectionResult result = mGoogleApiClient.getConnectionResult(Auth.GOOGLE_SIGN_IN_API);

            Log.v(TAG, "result info:" + result.isSuccess());

            if (result.isSuccess()) {
                Log.v(TAG, "In at least");
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        Log.v(TAG, "Whatya think?" + googleSignInResult.isSuccess());
                        works[0] = googleSignInResult.isSuccess();
                        if (works[0]) {
                            handleSignInResult(googleSignInResult);

                        }
                        Log.v(TAG, "Do we ever reach here?");
                    }
                });
            } else {
                Log.v(TAG, "Google's api is not connected or something, issues abound!");
                //Intent signInIntent1 = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                //startActivityForResult(signInIntent1, RC_SIGN_IN);
            }
        }
}
            return works[0];
    }

    private void signIn() {

        Intent signInIntent1 = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.v(TAG, "Interesting");
        startActivityForResult(signInIntent1, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        String idToken = null;
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            // !! IMPORTANT The main activity should probably become the splash activity and the main activity,
            // differentiated according to current login status, and the two separate displays being done via fragments.

            Log.v(TAG, "ID:" + acct.getId());
            Log.v(TAG, "ID Token:" + acct.getIdToken());

            firebaseAuthWithGoogle(acct);
            idToken = acct.getIdToken();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            Log.d(TAG, "Account name:" + acct.getDisplayName());
            Log.d(TAG, "Account profile image url:" + acct.getPhotoUrl());
            //DrawerImageLoader.getInstance().setImage((ImageView) profile.generateView(this), result.getSignInAccount().getPhotoUrl(), "Profile");

            updateUI(acct);

            /*toMain = new Intent(this, MainActivity.class);

            toMain.putExtra("acctName", acct.getDisplayName());
            toMain.putExtra("acctEmail", acct.getEmail());

            //profile.withName(acct.getDisplayName());
            //profile.withEmail(acct.getEmail());
            if(acct.getPhotoUrl() != null) {
                toMain.putExtra("acctPhotoUrl", acct.getPhotoUrl().toString());
                //profile.withIcon(acct.getPhotoUrl().toString());
            }
            //headerResult.updateProfile(profile);

            startActivity(toMain);*/

            /*Query query = FirebaseRef.child("users").orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());

            Log.d(TAG, String.valueOf(query));

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.w(TAG, "HAI!");
                    Log.w(TAG, String.valueOf(dataSnapshot.exists()));
                    if(dataSnapshot.exists()) {
                        Log.w(TAG, dataSnapshot.getValue().toString());
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.w(TAG, snapshot.child("username").getValue(String.class));
                            Log.w(TAG, snapshot.child("uid").getValue(String.class));
                        }
                    }
                    else {
                        mAuth.getCurrentUser().sendEmailVerification();
                        FirebaseRef.child("users").setValue(mAuth.getCurrentUser().getUid());
                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("username").setValue(mAuth.getCurrentUser().getDisplayName());
                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("uid").setValue(mAuth.getCurrentUser().getUid());
                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("photoURL").setValue(mAuth.getCurrentUser().getPhotoUrl().toString());
                        FirebaseRef.child("users").child(mAuth.getCurrentUser().getUid()).child("email").setValue(mAuth.getCurrentUser().getEmail());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });*/
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(null);
        }
        //result.getSignInAccount().getPhotoUrl();
        /*if(idToken != null) {
            sendToServer(idToken);
        }*/
    }

    private void updateUI(Object user) {
        //hideProgressDialog();
        if (user != null) {
            //mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            signInButton.setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            signInButton.setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            /*profile.withName(user.getDisplayName());
                            profile.withEmail(user.getEmail());
                            if(user.getPhotoUrl() != null) {
                                profile.withIcon(user.getPhotoUrl().toString());
                            }
                            headerResult.updateProfile(profile);*/
                            notify = true;
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                            //Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            /*case R.id.fb_sign_in_button:
                silentSignIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.v(TAG, "Are we logged in with firebase?" + String.valueOf(currentUser));
        /*if(currentUser != null) {
            toMain = new Intent(this, MainActivity.class);
            startActivity(toMain);
        }*/
        updateUI(currentUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) cntx);
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        if(mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) cntx);
            mGoogleApiClient.disconnect();
        }

    }
}
