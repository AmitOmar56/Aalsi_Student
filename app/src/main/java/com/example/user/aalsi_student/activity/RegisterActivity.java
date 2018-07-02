package com.example.user.aalsi_student.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText text_name;
    private EditText text_email;
    private EditText text_phone;
    private EditText text_password;
    private EditText phonegmail;
    private EditText input_passwordgmail;
    String name;
    String email;
    String phone;
    String password;
    private String personName;
    private String personEmail;
    private LinearLayout registerLayout;
    private static Animation shakeAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        text_name = (EditText) findViewById(R.id.input_name);
        text_email = (EditText) findViewById(R.id.input_email);
        text_phone = (EditText) findViewById(R.id.phone);
        text_password = (EditText) findViewById(R.id.input_password);
        registerLayout = (LinearLayout) findViewById(R.id.registerLayout);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake_animation);

    }

    public void signUp(View view) {
        boolean isExecuteNext = true;
        name = text_name.getText().toString();
        email = text_email.getText().toString();
        phone = text_phone.getText().toString();
        password = text_password.getText().toString();

        if (name.isEmpty()) {
            Log.d("name", name + "");
            text_name.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }

        if (email.isEmpty()) {
            text_email.setError(getResources().getString(R.string.enter_email));
        } else if (!isValidEmail(email)) {
            text_email.setError(getResources().getString(R.string.enter_valid_email));
            isExecuteNext = false;
        }
        if (phone.isEmpty()) {
            text_phone.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (phone.length() < 10) {
            text_phone.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }
        if (password.isEmpty()) {
            Log.d("password", password + "");
            text_password.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }

        if (!isExecuteNext) {
            registerLayout.startAnimation(shakeAnimation);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_fill_all_required_field), Toast.LENGTH_SHORT).show();
            return;
        } else {
            MyProgressDialog.showPDialog(this);

            signupRequest();

        }
    }

    private void signupRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/registration.php?",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            Log.d("json", json + "");
                            Log.d("jsonObject", jsonObject + "");

                            if (jsonObject.getInt("status") == 1) {
                                Snackbar snackbar = Snackbar.make(RegisterActivity.this.getWindow().getDecorView().getRootView(), "Successfully Registered", Snackbar.LENGTH_LONG)
                                        .setAction("Go to Login", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(getApplicationContext(), Main_ScreenActivity.class));
                                            }
                                        });

                                snackbar.show();
                                MyProgressDialog.hidePDialog();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void alreadylogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
