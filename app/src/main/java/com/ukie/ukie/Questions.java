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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    //private DatabaseReference myRef;
    private static final String TAG = "SignInActivity";

    //TextView QuestionText = (TextView) findViewById(R.id.textView2);

    Button checkBtn = (Button) findViewById(R.id.button2);

    StringRequest stringRequest; // Assume this exists.
    RequestQueue mRequestQueue;  // Assume this exists.

    String url ="https://ukie.herokuapp.com";

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

        mRequestQueue = Volley.newRequestQueue(this);

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

            /*if (tmp.compareToIgnoreCase(String.valueOf(getConjugation())) == 0) {
                textView1.setText("Correct!");
                textView1.setChecked(true);
                //textView1.setTextColor(100);
                if (prog.getProgress() != prog.getMax()) {
                    prog.setProgress(prog.getProgress() + 1);
                }
            } else {
                textView1.setText("That's incorrect, the correct conjugation for " + getConjType() + " of " + getInfinitive() + " is \"" + getConjugation() + "\"");
                textView1.setChecked(false);
                checkBtn.setBackgroundColor(getResources().getColor(md_red_800));
                //textView1.setTextColor(500);
                if (prog.getProgress() != 0) {
                    prog.setProgress(prog.getProgress() - 1);
                }
            }*/
        }
            return false;
    }

    public String getConjugation() {
        return serverRequest("/conj");
    }

    public String getConjType() {
        return serverRequest("/conjtype");
    }

    public String getInfinitive() {
        return serverRequest("/infin");
    }

    public String serverRequest(String req) {

        final StringBuilder res = null;

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, url + req,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response);
                        res.append(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);

        return res.toString();
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

}
