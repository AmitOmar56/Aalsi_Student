package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.example.user.aalsi_student.utils.utils.isValidEmail;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView incorrectid;
    private EditText emailEdit;
    private String email;
    private LinearLayout forgotlayout;
    private TextView success_email;
    private RelativeLayout successrellay;
    private static Animation shakeAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        findId();
    }

    private void findId() {
        incorrectid = (TextView) findViewById(R.id.incorrectid);
        emailEdit = (EditText) findViewById(R.id.editText);
        forgotlayout = (LinearLayout) findViewById(R.id.forgotlayout);
        success_email = (TextView) findViewById(R.id.success_email);
        successrellay = (RelativeLayout) findViewById(R.id.successrellay);
        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake_animation);
    }

    public void resetPassword(View view) {

        email = emailEdit.getText().toString();

        if (email.isEmpty()) {
            successrellay.startAnimation(shakeAnimation);
            emailEdit.setError(getResources().getString(R.string.enter_email));
        } else if (!isValidEmail(email)) {
            successrellay.startAnimation(shakeAnimation);
            emailEdit.setError(getResources().getString(R.string.enter_valid_email));
        } else {
            MyProgressDialog.showPDialog(this);

            logInRequest();
        }
    }

    private void logInRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/forgot_password.php?",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pd.hide();
                        //Response
//                        showSnackbar(response);
                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;

                            Log.d("json", json + "");
                            Log.d("jsonObject", jsonObject.getString("status") + "");

                            if (jsonObject.getInt("status") == 1) {
                                successrellay.setVisibility(View.GONE);
                                forgotlayout.setVisibility(View.VISIBLE);
                                success_email.setText(email);
                                MyProgressDialog.hidePDialog();

                            } else {
                                MyProgressDialog.hidePDialog();
                                forgotlayout.setVisibility(View.GONE);
                                incorrectid.setText(" incorrect email");
                                Toast.makeText(ForgotPasswordActivity.this, "Incorrect email", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void ok(View view) {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
