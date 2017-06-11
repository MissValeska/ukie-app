package com.ukie.ukie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class google_login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    String serverID = "723836520365-n1bevgandt9o3s35gsnu2het64h50s9a.apps.googleusercontent.com";

    FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);

    IProfile profile;

    AccountHeader headerResult;

    private FirebaseAuth mAuth;

    DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatusTextView = (TextView) findViewById(R.id.status);

        profile = new ProfileDrawerItem().withName("Profile").withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_account_circle);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.mipmap.ic_header_background)
                .addProfiles(
// Adding profiles to header view
                        profile,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
// Adding setting drawer item other than profile drawer item
                        new ProfileSettingDrawerItem().withName("Add Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(10),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(11)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        /*if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }*/

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        //profile,
                        //new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Dashboard").withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_dashboard),
                        new PrimaryDrawerItem().withName("Exercises").withIdentifier(3).withIcon(GoogleMaterial.Icon.gmd_fitness_center),
                        new PrimaryDrawerItem().withName("Badges").withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_local_offer),
                        new PrimaryDrawerItem().withName("Friends").withIdentifier(5).withIcon(GoogleMaterial.Icon.gmd_people),
                        new PrimaryDrawerItem().withName("Discussions").withIdentifier(6).withIcon(GoogleMaterial.Icon.gmd_forum),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(7).withIcon(GoogleMaterial.Icon.gmd_settings),
                        new PrimaryDrawerItem().withName("About").withIdentifier(9).withIcon(GoogleMaterial.Icon.gmd_info),
                        new PrimaryDrawerItem().withName("Contact").withIdentifier(8).withIcon(GoogleMaterial.Icon.gmd_email)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        TextView textView = (TextView) findViewById(R.id.DText);
                        textView.setText(String.valueOf(drawerItem.getIdentifier()));
                        selectDrawerItem(drawerItem);
                        return false;
                    }
                })
                .build();

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(serverID).requestProfile()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        mAuth = FirebaseAuth.getInstance();

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

    /*
    @Override
    public Drawable placeholder(Context ctx) {
        return super.placeholder(ctx);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return super.placeholder(ctx, tag);
    }
    */
        });

    }

    public void sendToServer(String idToken) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://yourbackend.example.com/tokensignin");

        try {
            List nameValuePairs = new ArrayList(1);
            nameValuePairs.add(new BasicNameValuePair("idToken", idToken));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            Log.i(TAG, "Signed in as: " + responseBody);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error sending ID token to backend.", e);
        } catch (IOException e) {
            Log.e(TAG, "Error sending ID token to backend.", e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        String idToken = null;
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            idToken = acct.getIdToken();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            Log.d(TAG, "Account name:" + acct.getDisplayName());
            Log.d(TAG, "Account profile image url:" + acct.getPhotoUrl());
            //DrawerImageLoader.getInstance().setImage((ImageView) profile.generateView(this), result.getSignInAccount().getPhotoUrl(), "Profile");

            Query query = FirebaseRef.child("users").orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());

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
            });
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(null);
        }
        //result.getSignInAccount().getPhotoUrl();
        /*if(idToken != null) {
            sendToServer(idToken);
        }*/
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            profile.withName(user.getDisplayName());
                            profile.withEmail(user.getEmail());
                            if(user.getPhotoUrl() != null) {
                                profile.withIcon(user.getPhotoUrl().toString());
                            }
                            headerResult.updateProfile(profile);
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

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            //mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    public void selectDrawerItem(IDrawerItem menuItem) {
        Intent intent;
        // !! Prevent going to any activity if the user is already there, and simply close the drawer
        switch((int) menuItem.getIdentifier()) {
            case 1: // !! Add profile activity
                intent = new Intent(this, google_login.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, Exercises.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, Badges.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(this, Friends.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(this, ForumsActivity.class);
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 8:
                intent = new Intent(this, ReportActivity.class);
                startActivity(intent);
                break;
            default: // !! Examine more later
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateUI(currentUser);
            Log.d(TAG, "User email:" + currentUser.getEmail() + "User ID:" + currentUser.getUid() + "Photo url:" + currentUser.getPhotoUrl());
            profile.withName(currentUser.getDisplayName());
            profile.withEmail(currentUser.getEmail());
            if(currentUser.getPhotoUrl() != null) {
                profile.withIcon(currentUser.getPhotoUrl().toString());
            }
            headerResult.updateProfile(profile);
            //DrawerImageLoader.getInstance().setImage((ImageView) profile.generateView(this), currentUser.getPhotoUrl(), "Profile");

            Query query = FirebaseRef.child("users").orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());

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
                        //mAuth.getCurrentUser().sendEmailVerification();
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
            });

        }
        //Toast.makeText(this, "User email:" + currentUser.getEmail() + "User ID:" + currentUser.getUid() + "Photo url:" + currentUser.getPhotoUrl(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

}
