package com.ukie.ukie;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DragQuestions extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "SignInActivity";

    TextView txt;

    int correctAnswr = 0;

    ArrayList<questionData> data;

    int QuestionCount = 0;
    int index = 0;
    int progress = 0;

    ProgressBar prog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        data = (ArrayList<questionData>) intent.getSerializableExtra("data");

        Log.w(TAG, "Entered Drag:" + data.size() + " : " + data.toString());

        QuestionCount = intent.getIntExtra("count", QuestionCount);
        index = intent.getIntExtra("index", index);
        progress = intent.getIntExtra("progress", progress);

        prog = (ProgressBar) findViewById(R.id.progressBar7);
        prog.setMax(QuestionCount);
        prog.setProgress(progress);

        correctAnswr = data.get(index).getInt("correctAnswer");

        ImageButton dragButton1 = (ImageButton) findViewById(R.id.imageButton6);
        ImageButton dragButton2 = (ImageButton) findViewById(R.id.imageButton7);
        ImageButton dragButton3 = (ImageButton) findViewById(R.id.imageButton8);
        ImageButton dragButton4 = (ImageButton) findViewById(R.id.imageButton9);

        txt = (TextView) findViewById(R.id.textView18);

        txt.setText(data.get(index).getString("questionText"));

        String q1url = data.get(index).getString("qImg1");
        String q2url = data.get(index).getString("qImg2");
        String q3url = data.get(index).getString("qImg3");
        String q4url = data.get(index).getString("qImg4");

        String type = data.get(index).getString("type");

        //Picasso.with(dragButton1.getContext()).load("").fit().centerCrop().into(dragButton1);
        Picasso.with(dragButton1.getContext()).load(q1url).fit().centerCrop().into(dragButton1);
        Picasso.with(dragButton2.getContext()).load(q2url).fit().centerCrop().into(dragButton2);
        Picasso.with(dragButton3.getContext()).load(q3url).fit().centerCrop().into(dragButton3);
        Picasso.with(dragButton4.getContext()).load(q4url).fit().centerCrop().into(dragButton4);

        final float originalX = dragButton1.getTranslationX();
        final float originalY = dragButton1.getTranslationY();

        dragButton1.setTag(1);
        dragButton2.setTag(2);
        dragButton3.setTag(3);
        dragButton4.setTag(4);

        dragButton1.setOnTouchListener(DragQuestions.this);
        dragButton2.setOnTouchListener(DragQuestions.this);
        dragButton3.setOnTouchListener(DragQuestions.this);
        dragButton4.setOnTouchListener(DragQuestions.this);

    }

    float startX;
    float distanceX;
    float startY;
    float[] startRawX = {0, 0, 0, 0};
    float[] startRawY = {0, 0, 0, 0};
    int[] location2 = new int[2];
    float distanceY;
    int lastAction;
    int[] c = {0, 0, 0, 0};

    /*private int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }*/

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(c[(int)view.getTag()-1] == 0) {
            //Log.v(TAG, String.valueOf(location1[(int)view.getTag()-1].length));
            view.getLocationOnScreen(location2);
            startRawX[(int)view.getTag()-1] = location2[0];
            startRawY[(int)view.getTag()-1] = location2[1];
        }
        c[(int)view.getTag()-1]++;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                startX = view.getX() - event.getRawX();
                startY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setX(event.getRawX() + startX);
                view.setY(event.getRawY() + startY);

                Rect outRect = new Rect();
                int[] location = new int[2];

                txt.getDrawingRect(outRect);
                txt.getLocationOnScreen(location);
                outRect.offset(location[0], location[1]);
                // !! Change to make the bounds larger, probably just for the outRect, making it easier to get an
                // !! imagebutton within the bounds of the textview
                //outRect.offset(150, 150);
                //txt.setText(String.valueOf((int) view.getTag()));
                if(outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    if((int) view.getTag() == correctAnswr) {
                        txt.setText("Correct!");
                        view.setVisibility(View.INVISIBLE);

                        prog.setProgress(prog.getProgress() + 1);
                        Toast.makeText(DragQuestions.this, "You are correct!",
                                Toast.LENGTH_SHORT).show();

                        // !! Consider making this asyncronous with a slight delay, to allow the user to read the correct message text and so on
                        if(index + 1 != QuestionCount) {

                            Intent intent = null;
                            // !! Update the other question formats to use "else if" for greater efficiency
                            if (data.get(index + 1).getString("type").compareToIgnoreCase("text") == 0) {
                                intent = new Intent(getApplicationContext(), Questions.class);
                            } else if (data.get(index + 1).getString("type").compareToIgnoreCase("audio") == 0 || data.get(index + 1).getString("type").compareToIgnoreCase("image") == 0) {
                                intent = new Intent(getApplicationContext(), ImageQuestions.class);
                            } else if (data.get(index + 1).getString("type").compareToIgnoreCase("dropdown") == 0) {
                                intent = new Intent(getApplicationContext(), dropdownQuestions.class);
                            } else if (data.get(index + 1).getString("type").contains("radio")) {
                                intent = new Intent(getApplicationContext(), radioQuestions.class);
                            } else if (data.get(index + 1).getString("type").compareToIgnoreCase("drag") == 0) {
                                intent = new Intent(getApplicationContext(), DragQuestions.class);
                            } else if (data.get(index + 1).getString("type").compareToIgnoreCase("pairs") == 0) {
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
                        }
                        else {
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
                    else {
                        txt.setText("Incorrect");
                        if((int)view.getTag() < 3) {
                            view.setX(startRawX[(int) view.getTag() - 1]);
                            view.setY(startRawY[(int) view.getTag() - 1]-100);
                        }
                        else {
                            view.setX(startRawX[(int) view.getTag() - 1]);
                            view.setY(startRawY[(int) view.getTag() - 1]-100);
                        }
                    }
                }

                        /*float plusTenX = Math.abs(txt.getX() + 10);
                        float minusTenX = Math.abs(txt.getX() - 10);

                        float plusTenY = Math.abs(txt.getY() + 10);
                        float minusTenY = Math.abs(txt.getY() - 10);

                        if(Math.abs(view.getX()) <= plusTenX || Math.abs(view.getX()) <= minusTenX && (Math.abs(view.getY()) <= plusTenY || Math.abs(view.getY()) <= minusTenY)) {
                            txt.setText("Yo");
                            view.setVisibility(View.INVISIBLE);
                        }*/

                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                distanceX = event.getRawX()-startRawX[(int)view.getTag()-1];
                distanceY = event.getRawY()-startRawY[(int)view.getTag()-1];

                // !! Provide a grace period, somehow
                if((int)view.getTag() < 3) {
                    view.setX(startRawX[(int) view.getTag() - 1]);
                    view.setY(startRawY[(int) view.getTag() - 1]-100);
                }
                else {
                    view.setX(startRawX[(int) view.getTag() - 1]);
                    view.setY(startRawY[(int) view.getTag() - 1]-100);
                }

                        /*if(Math.abs(view.getY()) <= plusTenY || Math.abs(view.getY()) <= minusTenY) {
                            txt.setText("Man");
                        }*/

                //txt.getX();
                //txt.getY();

                //Math.abs(distanceX);
                //Math.abs(distanceY);
                        /*if (Math.abs(distanceX)< 10){
                            //edit();
                        }*/
                break;
            case MotionEvent.ACTION_BUTTON_PRESS:
                // !! Consider adding some kind of card, audio, etc, something that would happen when the button is clicked
                break;

            default:
                return false;
        }
        return true;
    }
}
