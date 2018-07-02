package com.example.user.aalsi_student.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private EditText username_text;
    private EditText password_text;
    private String username;
    private String password;
    private TextView incorrectid;
    static String S_first_name;
    static String S_last_name;
    static String S_headline;
    static String S_email;
    static String S_image;
    static String S_user_id;
    private Button mob_button;
    private TextInputLayout mobLayout;
    private EditText mob_Text;
    private String f_mobile;

    static boolean lresult = true;


    public LoginButton loginButton;
    private CallbackManager callbackManager;
    private String birthday;
    /***********Gmail************/
    /****************************/
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private String personPhotoUrl;
    private SignInButton btnSignIn;

    private String f_email;
    private String f_name;
    private static Animation shakeAnimation;
    private static LinearLayout loginLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);


        username_text = (EditText) findViewById(R.id.editText);
        password_text = (EditText) findViewById(R.id.password);
        incorrectid = (TextView) findViewById(R.id.incorrectid);
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        mob_button = (Button) findViewById(R.id.mob_button);
        mobLayout = (TextInputLayout) findViewById(R.id.mobLayout);
        mob_Text = (EditText) findViewById(R.id.mob_Text);

// Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake_animation);
        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);


        btnSignIn.setOnClickListener(this);
        /**************Gmail************/

        gmailApi();
        if (lresult == false) {
            loginButton.setVisibility(View.VISIBLE);
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

        }

        /*************Facebook***********/

        callbackManager = CallbackManager.Factory.create();
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.GONE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("LoginActivity", response.toString());
                                try {
                                    Log.d("facebook", object.getString("name") + "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.d("facebook", object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(LoginActivity.this, Main_ScreenActivity.class));
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
                Log.d("birthday", birthday + "");

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        /*************Facebook***********/


    }

    private void gmailApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            Object o = acct.getPhotoUrl();
            Log.d("=->.>>>>>>>>>>", o + "");
            if (o == null) {
                personPhotoUrl = "https://pleper.com/html/assets/img/no-image-found.jpg";
            } else {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            String personEmail = acct.getEmail();


            Log.e(TAG, "Name: " + personName + ", email: " + personEmail
                    + ", Image: " + personPhotoUrl);

            Log.d("personPhotoUrl", personPhotoUrl);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);

            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
        signOut();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", data + "");
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.VISIBLE);
        } else {
            if (lresult == true) {
                btnSignIn.setVisibility(View.VISIBLE);
            } else {
                btnSignIn.setVisibility(View.VISIBLE);
            }
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void loginPage(View view) {

        boolean isExecuteNext = true;
        username = username_text.getText().toString();
        password = password_text.getText().toString();

        Log.d("username", username);
        if (username.isEmpty()) {
            Log.d("hightString", username);
            username_text.setError(getResources().getString(R.string.name_error));
            isExecuteNext = false;
        }
        if (password.isEmpty()) {
            Log.d("hightString", password);
            password_text.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (!isExecuteNext) {
            loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_fill_all_required_field), Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d("amit", "response");

            MyProgressDialog.showPDialog(this);
            logInRequest();
        }
    }

    private void logInRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;
        Log.d("amit", "amit");
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/login.php?",
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


                            if (jsonObject.getInt("status") == 1) {
                                JSONObject data = jsonObject.getJSONObject("data");

                                S_first_name = data.getString("name");
                                S_last_name = data.getString("last_name");
                                S_headline = data.getString("headline");
                                S_image = data.getString("image");
                                S_email = data.getString("email");
                                S_user_id = data.getString("st_id");
                                Log.d("S_image->>>>>>", S_image);
                                insert();
                                Intent intent = new Intent(LoginActivity.this, Main_ScreenActivity.class);

                                startActivity(intent);

                                MyProgressDialog.hidePDialog();

                            } else {
                                MyProgressDialog.hidePDialog();
                                loginLayout.startAnimation(shakeAnimation);
                                incorrectid.setText(" incorrect username or password");
                                Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void forgot(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", true);
        edt.commit();
    }


    public void facebook_next(View view) {


        f_mobile = mob_Text.getText().toString();

        if (f_mobile.isEmpty()) {
            mob_Text.setError(getResources().getString(R.string.Phone_error));
        } else if (mob_Text.length() < 10) {
            mob_Text.setError(getResources().getString(R.string.Phone_length_validation));
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
                                Snackbar snackbar = Snackbar.make(LoginActivity.this.getWindow().getDecorView().getRootView(), "Successfully Registered", Snackbar.LENGTH_LONG)
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
                        Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", f_name);
                params.put("email", f_email);
                params.put("phone", f_mobile);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

}
