package com.ukie.ukie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Questions extends AppCompatActivity implements View.OnKeyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText textView = (EditText) findViewById(R.id.editText);
        textView.setOnKeyListener(this);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            ProgressBar prog = (ProgressBar) findViewById(R.id.progressBar2);
            CheckedTextView textView1 = (CheckedTextView) findViewById(R.id.checkedTextView);

            String tmp = ((EditText) v).getText().toString();

            if (tmp.compareToIgnoreCase("живеш") == 0) {
                textView1.setText("Correct!");
                textView1.setChecked(true);
                //textView1.setTextColor(100);
                if (prog.getProgress() != prog.getMax()) {
                    prog.setProgress(prog.getProgress() + 1);
                }
            } else {
                textView1.setText("That's incorrect, the correct conjugation for ти of жити is \"живеш\"");
                textView1.setChecked(false);
                //textView1.setTextColor(500);
                if (prog.getProgress() != 0) {
                    prog.setProgress(prog.getProgress() - 1);
                }
            }
        }
            return false;
    }

}
