package com.androidigniter.loginandregistration;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterForeleserActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BILDEURL = "bildeURL";
    private static final String KEY_EMPTY = "";
    private EditText etFullNameLecturer;
    private EditText etPasswordLecturer;
    private EditText etEmailLecturer;
    private EditText etBildeURL;
    private String fullName;
    private String password;
    private String email;
    private String bildeURL;
    private ProgressDialog pDialog;
    private String register_url = "http://158.39.188.215/branch/teste/gruppe15/oblig1/php/registerForeleserToDatabase.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_registerlecturer);

        etFullNameLecturer = findViewById(R.id.etFullNameLecturer);
        etPasswordLecturer = findViewById(R.id.etPasswordLecturer);
        etEmailLecturer = findViewById(R.id.etEmailLecturer);
        etBildeURL = findViewById(R.id.etBildeURL);

        Button login = findViewById(R.id.btnRegisterLoginLecturer);
        Button register = findViewById(R.id.btnRegisterLecturer);

        //Launch Login screen when Login Button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterForeleserActivity.this, LoginForeleserActivity.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                fullName = etFullNameLecturer.getText().toString().trim();
                password = etPasswordLecturer.getText().toString().trim();
                email = etEmailLecturer.getText().toString().trim();
                bildeURL = etBildeURL.getText().toString().trim();
                if (validateInputs()) {
                    registerUser();
                }

            }
        });

    }

    /**
     * Display Progress bar while registering
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterForeleserActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();

    }

    private void registerUser() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_FULL_NAME, fullName);
            request.put(KEY_PASSWORD, password);
            request.put(KEY_EMAIL, email);
            request.put(KEY_BILDEURL, bildeURL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                session.loginUser(email, fullName);
                                loadDashboard();

                            }else if(response.getInt(KEY_STATUS) == 1){
                                //Display error message if email is already existsing
                                etEmailLecturer.setError("Email already taken!");
                                etEmailLecturer.requestFocus();

                            }else{
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * Validates inputs and shows error if any
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(fullName)) {
            etFullNameLecturer.setError("Full Name cannot be empty");
            etFullNameLecturer.requestFocus();
            return false;

        }
        if (KEY_EMPTY.equals(password)) {
            etPasswordLecturer.setError("Password cannot be empty");
            etPasswordLecturer.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(email)) {
            etEmailLecturer.setError("Email cannot be empty");
            etEmailLecturer.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(bildeURL)) {
            etBildeURL.setError("BilderURL cannot be empty");
            etBildeURL.requestFocus();
            return false;
        }

        return true;
    }
}

