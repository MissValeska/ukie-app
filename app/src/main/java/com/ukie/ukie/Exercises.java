package com.ukie.ukie;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Exercises extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    StringRequest stringRequest; // Assume this exists.
    RequestQueue mRequestQueue;  // Assume this exists.

    String[] list = new String[3];

    String url ="https://ukie.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageIcon();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Questions.class);
                startActivity(intent);
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);
        //Log.w(TAG,"That's incorrect, the correct conjugation for " + getConjType() + " of " + getInfinitive() + " is \"" + getConjugation() + "\"");

        /**
         * Method to make json object request where json response starts wtih {
         * */
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    String conj = response.getString("conj");
                    String conjtype = response.getString("conjtype");
                    String infin = response.getString("infin");
                    Log.w(TAG,"Stuff:" + conj);
                    /*JSONObject phone = response.getJSONObject("phone");
                    String home = phone.getString("home");
                    String mobile = phone.getString("mobile");*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Request a string response from the provided URL.
       /* stringRequest = new StringRequest(Request.Method.GET, url + "/conj",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response);
                        Log.w(TAG,"Stuff:" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });*/
        // Add the request to the RequestQueue.
        stringRequest.setTag(TAG);
        mRequestQueue.add(jsonObjReq);
        /*final TextView outputText = (TextView) findViewById(R.id.textView4);

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                while(stringRequest.hasHadResponseDelivered() != true) {

                }
                //outputText.setText();
                return null;
            }
        }.execute();*/

        //setConjugation();
        //setConjType();
        //setInfinitive();

        //Log.w(TAG,"That's incorrect, the correct conjugation for " + getConjType() + " of " + getInfinitive() + " is \"" + getConjugation() + "\"");


        //serverGET("http://192.168.0.135:3000/infin");

    }

    public String getConjugation() {
        return list[0];
    }

    public String getConjType() {
        return list[1];
    }

    public String getInfinitive() {
        return list[2];
    }

    public void setConjugation() {
        serverRequest("/conj", 0);
    }

    public void setConjType() {
        serverRequest("/conjtype", 1);
    }

    public void setInfinitive() {
        serverRequest("/infin", 2);
    }

    public void serverRequest(String req, final int i) {

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, url + req,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response);
                        list[i] = response;
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
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

}
