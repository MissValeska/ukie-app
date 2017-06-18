package com.ukie.ukie;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.media.MediaDrm;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.caverock.androidsvg.SVG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class ImageQuestions extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;

    ArrayList<String> forumsList = new ArrayList<String>();

    ArrayList<String> audioList = new ArrayList<String>();

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;

    final DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

    MediaPlayer q1mp;
    MediaPlayer q2mp;
    MediaPlayer q3mp;
    MediaPlayer q4mp;


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

        //if(mAuth.getCurrentUser() != null) {

                    Intent intent = getIntent();

                    data = (ArrayList<questionData>) intent.getSerializableExtra("data");

                    QuestionCount = intent.getIntExtra("count", QuestionCount);
                    index = intent.getIntExtra("index", index);
                    progress = intent.getIntExtra("progress", progress);

                    prog = (ProgressBar) findViewById(R.id.imgProgress);
                    prog.setMax(QuestionCount);
                    prog.setProgress(progress);

                    final TextView img1Text = (TextView) findViewById(R.id.img1Text);
                    final TextView img2Text = (TextView) findViewById(R.id.img2Text);
                    final TextView img3Text = (TextView) findViewById(R.id.img3Text);
                    final TextView img4Text = (TextView) findViewById(R.id.img4Text);

                    String type = null;

                    int tmpInt = 0;

                    Log.w(TAG, data.get(index).getString("question-text"));
                    qText.setText(data.get(index).getString("question-text"));
                    audioList.add(data.get(index).getString("qAudio1"));
                    audioList.add(data.get(index).getString("qAudio2"));
                    audioList.add(data.get(index).getString("qAudio3"));
                    audioList.add(data.get(index).getString("qAudio4"));
                    final int correctAnswr = data.get(index).getInt("correctAnswer") - 1;
                    type = data.get(index).getString("type");

                    /*qText.setText(snapshot.child("question-text").getValue(String.class));

                    audioList.add(snapshot.child("qAudio1").getValue(String.class));
                    audioList.add(snapshot.child("qAudio2").getValue(String.class));
                    audioList.add(snapshot.child("qAudio3").getValue(String.class));
                    audioList.add(snapshot.child("qAudio4").getValue(String.class));

                    final int correctAnswr = snapshot.child("correctAnswer").getValue(int.class) - 1;

                    Log.w(TAG, String.valueOf(correctAnswr));*/


                    if(type.compareToIgnoreCase("image") == 0) {
                        Log.w(TAG, "RAWR");
                        img1Text.setText(data.get(index).getString("img1Text"));
                        img2Text.setText(data.get(index).getString("img2Text"));
                        img3Text.setText(data.get(index).getString("img3Text"));
                        img4Text.setText(data.get(index).getString("img4Text"));
                        img1Text.setVisibility(View.VISIBLE);
                        img2Text.setVisibility(View.VISIBLE);
                        img3Text.setVisibility(View.VISIBLE);
                        img4Text.setVisibility(View.VISIBLE);

                        final String q1url = data.get(index).getString("qImg1");
                        final String q2url = data.get(index).getString("qImg2");
                        final String q3url = data.get(index).getString("qImg3");
                        final String q4url = data.get(index).getString("qImg4");

                        final FutureTask task = new FutureTask(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                Log.v(TAG, "Entered future task radioImage");
                                // !! Consider changing to properly center the image, compared to the textviews below the images, and or reposition the textviews
                                // !! Add a placeholder in case the image doesn't load for some reason
                                // !! Consider modifying the code to start with a placeeholder image, and then add the proper image later
                                // assuming that process doesn't make the placeholder visible on the screen, unless there are network problems
                                // making the image take a while to load, which would otherwise result in the image buttons simply not being visible
                                // !! Copy this code to all question formats and whatever else that use images.
                                Picasso.with(q1.getContext()).load(q1url).fit().centerCrop().into(q1);
                                Picasso.with(q2.getContext()).load(q2url).fit().centerCrop().into(q2);
                                Picasso.with(q3.getContext()).load(q3url).fit().centerCrop().into(q3);
                                Picasso.with(q4.getContext()).load(q4url).fit().centerCrop().into(q4);

                                q1.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                                q2.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                                q3.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                                q4.setBackgroundColor(getResources().getColor(R.color.tw__transparent));
                                return null;
                            }
                        });
                        task.run();

                    }
                    else if(type.compareToIgnoreCase("audio") == 0) {
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


                    final FutureTask task1 = new FutureTask(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            try {
                                q1mp = new MediaPlayer();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                q1mp.setDataSource(audioList.get(0));
                                q1mp.prepare();
                            } catch (Exception e) {
                                e.printStackTrace();
                                e.getMessage();
                            }
                            return q1mp;
                        }
                    });
                    task1.run();
                final FutureTask task2 = new FutureTask(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            q2mp = new MediaPlayer();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            q2mp.setDataSource(audioList.get(1));
                            q2mp.prepare();
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                        }
                        return q2mp;
                    }
                });
                task2.run();
                final FutureTask task3 = new FutureTask(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            q3mp = new MediaPlayer();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            q3mp.setDataSource(audioList.get(2));
                            q3mp.prepare();
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                        }
                        return q3mp;
                    }
                });
                task3.run();
                final FutureTask task4 = new FutureTask(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            q4mp = new MediaPlayer();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            q4mp.setDataSource(audioList.get(3));
                            q4mp.prepare();
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                        }
                        return q4mp;
                    }
                });
                task4.run();

                    q1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[0]) {
                                qisSelected[0] = false;
                                img1Text.setTextColor(Color.parseColor("#7A7A7A"));
                            }
                            else {
                                img1Text.setTextColor(Color.parseColor("#00AEF9"));

                                img2Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img3Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img4Text.setTextColor(Color.parseColor("#7A7A7A"));

                                qisSelected[0] = true;
                                qisSelected[1] = false;
                                qisSelected[2] = false;
                                qisSelected[3] = false;
                            }
                            do {
                                if (task1.isDone()) {
                                    if (!q1mp.isPlaying()) {
                                        q1mp.start();
                                    } else if (q1mp.isPlaying()) {
                                        q1mp.seekTo(0);
                                        q1mp.start();
                                    }
                                }
                            } while(!task1.isDone());

                        }
                    });

                    q2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[1]) {
                                qisSelected[1] = false;
                                img2Text.setTextColor(Color.parseColor("#7A7A7A"));
                            }
                            else {
                                img2Text.setTextColor(Color.parseColor("#00AEF9"));

                                img1Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img3Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img4Text.setTextColor(Color.parseColor("#7A7A7A"));

                                qisSelected[0] = false;
                                qisSelected[1] = true;
                                qisSelected[2] = false;
                                qisSelected[3] = false;
                            }

                            do {
                                if (task2.isDone()) {
                                    if (!q2mp.isPlaying()) {
                                        q2mp.start();
                                    } else if (q2mp.isPlaying()) {
                                        q2mp.seekTo(0);
                                        q2mp.start();
                                    }
                                }
                            } while(!task2.isDone());

                        }
                    });

                    q3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[2]) {
                                img3Text.setTextColor(Color.parseColor("#7A7A7A"));
                                qisSelected[2] = false;
                            }
                            else {
                                img3Text.setTextColor(Color.parseColor("#00AEF9"));

                                img1Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img2Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img4Text.setTextColor(Color.parseColor("#7A7A7A"));

                                qisSelected[0] = false;
                                qisSelected[1] = false;
                                qisSelected[2] = true;
                                qisSelected[3] = false;
                            }

                            do {
                                if (task3.isDone()) {
                                    if (!q3mp.isPlaying()) {
                                        q3mp.start();
                                    } else if (q3mp.isPlaying()) {
                                        q3mp.seekTo(0);
                                        q3mp.start();
                                    }
                                }
                            } while(!task3.isDone());

                        }
                    });

                    q4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(qisSelected[3]) {
                                img4Text.setTextColor(Color.parseColor("#7A7A7A"));
                                qisSelected[3] = false;
                            }
                            else {
                                img4Text.setTextColor(Color.parseColor("#00AEF9"));

                                img1Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img2Text.setTextColor(Color.parseColor("#7A7A7A"));
                                img3Text.setTextColor(Color.parseColor("#7A7A7A"));

                                qisSelected[0] = false;
                                qisSelected[1] = false;
                                qisSelected[2] = false;
                                qisSelected[3] = true;
                            }


                            do {
                                if (task4.isDone()) {
                                    if (!q4mp.isPlaying()) {
                                        q4mp.start();
                                    } else if (q4mp.isPlaying()) {
                                        q4mp.seekTo(0);
                                        q4mp.start();
                                    }
                                }
                            } while(!task4.isDone());

                        }
                    });

                    final int[] buttonPressCount = {0};

                    checkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.w(TAG, "Button stuff!");
                            if(qisSelected[correctAnswr]) {
                                if(buttonPressCount[0] == 0) {
                                    prog.setProgress(prog.getProgress() + 1);
                                    Toast.makeText(ImageQuestions.this, "You are correct!",
                                            Toast.LENGTH_SHORT).show();
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
                                        // !! Update the other question formats to use "else if" for greater efficiency
                                        if(data.get(index+1).getString("type").compareToIgnoreCase("text") == 0) {
                                            intent = new Intent(getApplicationContext(), Questions.class);
                                        }
                                        else if(data.get(index+1).getString("type").compareToIgnoreCase("audio") == 0 || data.get(index+1).getString("type").compareToIgnoreCase("image") == 0) {
                                            intent = new Intent(getApplicationContext(), ImageQuestions.class);
                                        }
                                        else if(data.get(index+1).getString("type").compareToIgnoreCase("dropdown") == 0) {
                                            intent = new Intent(getApplicationContext(), dropdownQuestions.class);
                                        }
                                        else if(data.get(index+1).getString("type").contains("radio")) {
                                            intent = new Intent(getApplicationContext(), radioQuestions.class);
                                        }
                                        else if (data.get(index + 1).getString("type").compareToIgnoreCase("drag") == 0) {
                                            intent = new Intent(getApplicationContext(), DragQuestions.class);
                                        }
                                        else if (data.get(index + 1).getString("type").compareToIgnoreCase("pairs") == 0) {
                                            intent = new Intent(getApplicationContext(), pairingQuestions.class);
                                        }

                                        intent.putExtra("data", data);
                                        intent.putExtra("count", QuestionCount);
                                        intent.putExtra("index", index + 1);
                                        intent.putExtra("progress", prog.getProgress());

                                        int mod = 0;
                                        mod = getIntent().getExtras().getInt("module");
                                        int exc = 0;
                                        exc = getIntent().getExtras().getInt("exercise");
                                        Log.w(TAG, "Mod:" + mod + " : " + exc);
                                        intent.putExtra("module", mod);
                                        intent.putExtra("exercise", exc);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), QuestionBlock.class);

                                        /*intent.putExtra("data", data);
                                        intent.putExtra("count", QuestionCount);
                                        intent.putExtra("index", index + 1);
                                        intent.putExtra("progress", prog.getProgress());*/

                                        int mod = 0;
                                        mod = getIntent().getExtras().getInt("module");
                                        int exc = 0;
                                        exc = getIntent().getExtras().getInt("exercise");
                                        Log.w(TAG, "ModRawr:" + mod + " : " + exc);
                                        intent.putExtra("module", mod);
                                        intent.putExtra("exercise", exc);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

                //} //mAuth.getCurrentUser() != null

        /*else {
            Toast.makeText(this, "You are not signed in, please go sign in now.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, google_login.class);
            startActivity(intent);
        }*/


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        q1mp.stop();
        q1mp.release();

        q2mp.stop();
        q2mp.release();

        q3mp.stop();
        q3mp.release();

        q4mp.stop();
        q4mp.release();
    }

}
