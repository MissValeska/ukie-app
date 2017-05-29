package com.ukie.ukie;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.ukie.ukie.R.color.md_amber_600;
import static com.ukie.ukie.R.color.md_red_800;

public class Questions extends AppCompatActivity implements View.OnKeyListener {

    /*public class Conjugations {
        public Conjugations() {}

        String Infinitive;
        String Conjugation;
        String ConjugationType;

        public String getInfinitive() {
            return Infinitive;
        }

        public String getConjugation() {
            return Conjugation;
        }

        public String getConjugationType() {
            return ConjugationType;
        }

    }*/

    private DatabaseReference myRef;
    private static final String TAG = "SignInActivity";

    TextView QuestionText = (TextView) findViewById(R.id.textView2);

    Button checkBtn = (Button) findViewById(R.id.button2);

    TextView Infin = (TextView) findViewById(R.id.Infin);
    TextView Conj = (TextView) findViewById(R.id.Conj);
    TextView ConjType = (TextView) findViewById(R.id.ConjType);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText textView = (EditText) findViewById(R.id.editText);
        textView.setOnKeyListener(this);

        //Infin.setText(serverConnect("http://192.168.0.135:3000/infin"));
        //Conj.setText(serverConnect("http://192.168.0.135:3000/conj"));
        //ConjType.setText(serverConnect("http://192.168.0.135:3000/conjtype"));

        checkBtn.setBackgroundColor(getResources().getColor(md_amber_600));

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("conjugation");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Conjugations conj = dataSnapshot.getValue(Conjugations.class);

                Infin.setText(conj.getInfinitive());
                Conj.setText(conj.getConjugation());
                ConjType.setText(conj.getConjugationType());
                QuestionText.setText("Conjugate " + Infin.getText() + " for " + ConjType.getText());

                //Log.d(TAG, "User name: " + conj.getInfinitive() + ", email " + conj.getConjugation());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/

    }

    public String serverConnect(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        String line = null;
        try {
            HttpResponse response = httpclient.execute(httpget);
            if(response != null) {
                InputStream inputstream = response.getEntity().getContent();
                line = convertStreamToString(inputstream);
                Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unable to complete your request", Toast.LENGTH_LONG).show();
            }
        } catch (ClientProtocolException e) {
            Toast.makeText(this, "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Caught IOException", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Caught Exception", Toast.LENGTH_SHORT).show();
        }

        return line;
    }

    public String convertStreamToString(InputStream is) {
        String line = null;
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
        }
        return total.toString();
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

            if (tmp.compareToIgnoreCase(String.valueOf(Conj.getText())) == 0) {
                textView1.setText("Correct!");
                textView1.setChecked(true);
                //textView1.setTextColor(100);
                if (prog.getProgress() != prog.getMax()) {
                    prog.setProgress(prog.getProgress() + 1);
                }
            } else {
                textView1.setText("That's incorrect, the correct conjugation for " + ConjType.getText() + " of " + Infin.getText() + " is \"" + Conj.getText() + "\"");
                textView1.setChecked(false);
                checkBtn.setBackgroundColor(getResources().getColor(md_red_800));
                //textView1.setTextColor(500);
                if (prog.getProgress() != 0) {
                    prog.setProgress(prog.getProgress() - 1);
                }
            }
        }
            return false;
    }

}
