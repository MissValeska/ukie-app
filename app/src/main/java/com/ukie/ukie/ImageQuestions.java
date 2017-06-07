package com.ukie.ukie;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

public class ImageQuestions extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;

    ArrayList<String> forumsList = new ArrayList<String>();

    ArrayList<String> audioList = new ArrayList<String>();

    ArrayList<JSONObject> data = new ArrayList<JSONObject>();

    ArrayList<String> tmpArray;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView qText = (TextView) findViewById(R.id.imgQuestionTxt);

        final Button checkBtn = (Button) findViewById(R.id.imgNext);

        final ImageButton q1 = (ImageButton) findViewById(R.id.imgQuestion1);
        final ImageButton q2 = (ImageButton) findViewById(R.id.imgQuestion2);
        final ImageButton q3 = (ImageButton) findViewById(R.id.imgQuestion3);
        final ImageButton q4 = (ImageButton) findViewById(R.id.imgQuestion4);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() != null) {

                    Intent intent = getIntent();

                    tmpArray = intent.getStringArrayListExtra("data");

                    QuestionCount = intent.getIntExtra("count", QuestionCount);
                    index = intent.getIntExtra("index", index);
                    progress = intent.getIntExtra("progress", progress);

                    prog = (ProgressBar) findViewById(R.id.imgProgress);
                    prog.setMax(QuestionCount);
                    prog.setProgress(progress);

                    String type = null;

                    int tmpInt = 0;

                    String q1url = null;
                    String q2url = null;
                    String q3url = null;
                    String q4url = null;

                    try {
                        Log.w(TAG, data.get(index).getString("question-text"));
                        qText.setText(data.get(index).getString("question-text"));
                        audioList.add(data.get(index).getString("qAudio1"));
                        audioList.add(data.get(index).getString("qAudio2"));
                        audioList.add(data.get(index).getString("qAudio3"));
                        audioList.add(data.get(index).getString("qAudio4"));
                        tmpInt = data.get(index).getInt("correctAnswer") - 1;
                        type = data.get(index).getString("type");
                        q1url = data.get(index).getString("question1");
                        q2url = data.get(index).getString("question2");
                        q3url = data.get(index).getString("question3");
                        q4url = data.get(index).getString("question4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final int correctAnswr = tmpInt;

                    /*qText.setText(snapshot.child("question-text").getValue(String.class));

                    audioList.add(snapshot.child("qAudio1").getValue(String.class));
                    audioList.add(snapshot.child("qAudio2").getValue(String.class));
                    audioList.add(snapshot.child("qAudio3").getValue(String.class));
                    audioList.add(snapshot.child("qAudio4").getValue(String.class));

                    final int correctAnswr = snapshot.child("correctAnswer").getValue(int.class) - 1;

                    Log.w(TAG, String.valueOf(correctAnswr));*/

                    if(type.compareToIgnoreCase("image") == 0) {
                        Log.w(TAG, "RAWR");
                        Picasso.with(q1.getContext()).load(q1url).into(q1);
                        Picasso.with(q2.getContext()).load(q2url).into(q2);
                        Picasso.with(q3.getContext()).load(q3url).into(q3);
                        Picasso.with(q4.getContext()).load(q4url).into(q4);
                    }
                    if(type.compareToIgnoreCase("audio") == 0) {
                        Log.w(TAG, "OMG");
                        q1.setImageDrawable(new IconicsDrawable(ImageQuestions.this, GoogleMaterial.Icon.gmd_volume_up).backgroundColor(getResources().getColor(R.color.tw__transparent)).color(getResources().getColor(R.color.md_light_blue_500)));
                        q2.setImageDrawable(new IconicsDrawable(ImageQuestions.this, GoogleMaterial.Icon.gmd_volume_up).backgroundColor(getResources().getColor(R.color.tw__transparent)).color(getResources().getColor(R.color.md_light_blue_500)));
                        q3.setImageDrawable(new IconicsDrawable(ImageQuestions.this, GoogleMaterial.Icon.gmd_volume_up).backgroundColor(getResources().getColor(R.color.tw__transparent)).color(getResources().getColor(R.color.md_light_blue_500)));
                        q4.setImageDrawable(new IconicsDrawable(ImageQuestions.this, GoogleMaterial.Icon.gmd_volume_up).backgroundColor(getResources().getColor(R.color.tw__transparent)).color(getResources().getColor(R.color.md_light_blue_500)));
                    }

                    /*StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

                    for(int i = 0; i < audioList.size(); i++) {
                        Log.w(TAG, "uhm");
                        final int finalI = i;
                        mStorageRef.child(audioList.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.w(TAG, "so");
                                audioList.set(finalI, uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }*/

                    final boolean[] q1isPLAYING = {false};
                    final boolean[] q2isPLAYING = {false};
                    final boolean[] q3isPLAYING = {false};
                    final boolean[] q4isPLAYING = {false};

                    final boolean[] qisSelected = {false, false, false, false};

                    final MediaPlayer q1mp = new MediaPlayer();
                    try {
                        q1mp.setDataSource(audioList.get(0));
                        q1mp.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "q1 prepare() failed");
                    }
                    q1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[0]) {
                                qisSelected[0] = false;
                            }
                            else {
                                qisSelected[0] = true;
                                qisSelected[1] = false;
                                qisSelected[2] = false;
                                qisSelected[3] = false;
                            }

                            if (!q1isPLAYING[0]) {
                                q1isPLAYING[0] = true;
                                q1mp.seekTo(0);
                                q1mp.start();
                            } else {
                                q1isPLAYING[0] = false;
                                q1mp.seekTo(0);
                                q1mp.start();
                            }

                        }
                    });
                    final MediaPlayer q2mp = new MediaPlayer();
                    try {
                        q2mp.setDataSource(audioList.get(1));
                        q2mp.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "q2 prepare() failed");
                    }
                    q2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[1]) {
                                qisSelected[1] = false;
                            }
                            else {
                                qisSelected[0] = false;
                                qisSelected[1] = true;
                                qisSelected[2] = false;
                                qisSelected[3] = false;
                            }

                            if (!q2isPLAYING[0]) {
                                q2isPLAYING[0] = true;
                                q2mp.seekTo(0);
                                q2mp.start();
                            } else {
                                q2isPLAYING[0] = false;
                                q2mp.seekTo(0);
                                q2mp.start();
                            }

                        }
                    });
                    final MediaPlayer q3mp = new MediaPlayer();
                    try {
                        q3mp.setDataSource(audioList.get(2));
                        q3mp.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "q3 prepare() failed");
                    }
                    q3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[2]) {
                                qisSelected[2] = false;
                            }
                            else {
                                qisSelected[0] = false;
                                qisSelected[1] = false;
                                qisSelected[2] = true;
                                qisSelected[3] = false;
                            }

                            if (!q3isPLAYING[0]) {
                                q3isPLAYING[0] = true;
                                q3mp.seekTo(0);
                                q3mp.start();
                            } else {
                                q3isPLAYING[0] = false;
                                q3mp.seekTo(0);
                                q3mp.start();
                            }

                        }
                    });
                    final MediaPlayer q4mp = new MediaPlayer();
                    try {
                        q4mp.setDataSource(audioList.get(3));
                        q4mp.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "q4 prepare() failed");
                    }
                    q4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[3]) {
                                qisSelected[3] = false;
                            }
                            else {
                                qisSelected[0] = false;
                                qisSelected[1] = false;
                                qisSelected[2] = false;
                                qisSelected[3] = true;
                            }


                            if (!q4isPLAYING[0]) {
                                q4isPLAYING[0] = true;
                                q4mp.seekTo(0);
                                q4mp.start();
                            } else {
                                q4isPLAYING[0] = false;
                                q4mp.seekTo(0);
                                q4mp.start();
                            }

                        }
                    });

                    final int[] buttonPressCount = {0};

                    checkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.w(TAG, "Button stuff!");
                            if(qisSelected[correctAnswr] == true) {
                                prog.setProgress(prog.getProgress()+1);
                                Toast.makeText(ImageQuestions.this, "You are correct!",
                                        Toast.LENGTH_SHORT).show();
                                if(buttonPressCount[0] == 0) {
                                    if(index + 1 != QuestionCount) {
                                        ((Button) v).setText("Next");
                                    }
                                    else {
                                        ((Button) v).setText("Finish");
                                    }
                                }
                                else {
                                    if(index + 1 != QuestionCount) {

                                        Intent intent = null;
                                        try {
                                            if(data.get(index+1).getString("type").compareToIgnoreCase("text") == 0) {
                                                intent = new Intent(getApplicationContext(), Questions.class);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            if(data.get(index+1).getString("type").compareToIgnoreCase("audio") == 0 || data.get(index+1).getString("type").compareToIgnoreCase("image") == 0) {
                                                intent = new Intent(getApplicationContext(), ImageQuestions.class);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        /*if(firstType.compareToIgnoreCase("dropdown") == 0) {
                                            intent = new Intent(getApplicationContext(), ImageQuestions.class); // As of yet, non-existent
                                        }*/

                                        intent.putStringArrayListExtra("data", tmpArray);
                                        intent.putExtra("count", QuestionCount);
                                        intent.putExtra("index", index + 1);
                                        intent.putExtra("progress", prog.getProgress());
                                        startActivity(intent);
                                    }
                                }
                            }
                            else {
                                Toast.makeText(ImageQuestions.this, "You are incorrect, please try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            buttonPressCount[0]++;
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
