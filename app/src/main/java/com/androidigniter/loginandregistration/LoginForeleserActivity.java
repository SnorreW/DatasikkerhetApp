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

public class LoginForeleserActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etLoginEmailLecturer;
    private EditText etLoginPasswordLecturer;
    private String emailLecturer;
    private String passwordLecturer;
    private ProgressDialog pDialog;
    private String login_url = "http://158.39.188.215/branch/teste/gruppe15/oblig1/php/loginLecturerFromDatabase.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_loginlecturer);

        etLoginEmailLecturer = findViewById(R.id.etLoginEmailLectur);
        etLoginPasswordLecturer = findViewById(R.id.etLoginPassword);

        Button register = findViewById(R.id.btnLoginRegister);
        Button login = findViewById(R.id.btnLogin);

        //Launch Registration screen when Register Button is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginForeleserActivity.this, RegisterForeleserActivity.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                emailLecturer = etLoginEmailLecturer.getText().toString().toLowerCase().trim();
                passwordLecturer = etLoginPasswordLecturer.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    /**
     * Launch Dashboard Activity on Successful Login
     */
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();

    }

    /**
     * Display Progress bar while Logging in
     */

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginForeleserActivity.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_EMAIL, emailLecturer);
            request.put(KEY_PASSWORD, passwordLecturer);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(emailLecturer,response.getString(KEY_EMAIL));
                                loadDashboard();

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
        if(KEY_EMPTY.equals(emailLecturer)){
            etLoginEmailLecturer.setError("Email cannot be empty");
            etLoginEmailLecturer.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(passwordLecturer)){
            etLoginPasswordLecturer.setError("Password cannot be empty");
            etLoginPasswordLecturer.requestFocus();
            return false;
        }
        return true;
    }
}
