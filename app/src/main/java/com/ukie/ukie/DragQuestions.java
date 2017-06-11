package com.ukie.ukie;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DragQuestions extends AppCompatActivity implements View.OnTouchListener {

    TextView txt;

    int correctAnswer = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton dragButton1 = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton dragButton2 = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton dragButton3 = (ImageButton) findViewById(R.id.imageButton4);
        ImageButton dragButton4 = (ImageButton) findViewById(R.id.imageButton5);

        txt = (TextView) findViewById(R.id.textView18);

        //Picasso.with(dragButton1.getContext()).load("").fit().centerCrop().into(dragButton1);
        Picasso.with(dragButton1.getContext()).load("https://firebasestorage.googleapis.com/v0/b/ukie-f3bcd.appspot.com/o/images%2Ffood%2F051-apple.png?alt=media&token=118564ef-13f7-411c-ac16-6641a82c4f78").fit().centerCrop().into(dragButton1);

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
    float startRawX;
    float distanceX;
    float startY;
    float startRawY;
    float distanceY;
    int lastAction;
    int[] c = {0, 0, 0, 0};

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(c[(int)view.getTag()-1] == 0) {
            startRawX = event.getRawX();
            startRawY = event.getRawY();
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
                outRect.offset(10, 10);
                //txt.setText(String.valueOf((int) view.getTag()));
                if(outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    if((int) view.getTag() == correctAnswer) {
                        txt.setText("Correct!");
                        view.setVisibility(View.INVISIBLE);
                    }
                    else {
                        txt.setText("Incorrect");
                        view.setX(startRawX);
                        view.setY(startRawY);
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
                distanceX = event.getRawX()-startRawX;
                distanceY = event.getRawY()-startRawY;

                // !! Provide a grace period, somehow
                view.setX(startRawX);
                view.setY(startRawY);

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

            default:
                return false;
        }
        return true;
    }
}
