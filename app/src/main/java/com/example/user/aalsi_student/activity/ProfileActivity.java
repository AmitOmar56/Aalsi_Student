package com.example.user.aalsi_student.activity;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.user.aalsi_student.activity.LoginActivity.S_email;
import static com.example.user.aalsi_student.activity.LoginActivity.S_first_name;
import static com.example.user.aalsi_student.activity.LoginActivity.S_headline;
import static com.example.user.aalsi_student.activity.LoginActivity.S_image;
import static com.example.user.aalsi_student.activity.LoginActivity.S_last_name;
import static com.example.user.aalsi_student.activity.LoginActivity.S_user_id;

public class ProfileActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private ImageView imgView;

    private String first_name;
    private String last_name;
    private String headline;
    private String email;
    private String image = "";


    private TextView profile_name;
    private TextView profile_email;
    private EditText firstName_Profile;
    private EditText lastName_Profile;
    private EditText headLine_Profile;
    private EditText email_Profile;
    private ImageView user_profile_photo;
    private String imageSource = "";
    private View view;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        getId();
        profile_name.setText(S_first_name);
        profile_email.setText(S_email);
        firstName_Profile.setText(S_first_name);
        lastName_Profile.setText(S_last_name);
        headLine_Profile.setText(S_headline);
        email_Profile.setText(S_email);
        String imageUrl = "http://aalsistudent.com/" + S_image;
        Log.d("imageUrl", imageUrl);

        Glide.with(this).load(imageUrl).into(imgView);
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    public void loadImagefromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            // When an Image is picked

            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                // Get the Image from data


                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                // Move to first row

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                imgDecodableString = cursor.getString(columnIndex);

                cursor.close();

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                imgView.setBackgroundDrawable(ob);
                //  Glide.with(this).load("").into(imgView);
                imgView.setImageBitmap(bitmap);
                Toast.makeText(this, "picked" + bitmap + "", Toast.LENGTH_LONG).show();


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.d("imageString", imageString);

            }

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageSource = String.valueOf(photo);

                BitmapDrawable ob = new BitmapDrawable(getResources(), photo);
                imgView.setBackgroundDrawable(ob);
                Glide.with(this).load("").into(imgView);
            } else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();

        }

    }

    public void update(View view) {
        first_name = firstName_Profile.getText().toString();
        last_name = lastName_Profile.getText().toString();
        headline = headLine_Profile.getText().toString();
        email = email_Profile.getText().toString();

        Log.d("first_name", first_name);
        Log.d("last_name", last_name);
        Log.d("headline", headline);
        Log.d("email", email);

        MyProgressDialog.showPDialog(this);
        updateProfile();
    }

    private void updateProfile() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

//        Log.d("bitmap", bitmap + "");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/update_pro.php?",
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;


                            if (jsonObject.getInt("status") == 1) {

                                S_first_name = first_name;
                                S_last_name = last_name;
                                S_email = email;
                                S_headline = headline;

                                profile_name.setText(S_first_name);
                                profile_email.setText(S_email);
                                firstName_Profile.setText(S_first_name);
                                lastName_Profile.setText(S_last_name);
                                headLine_Profile.setText(S_headline);
                                email_Profile.setText(S_email);

                                Toast.makeText(ProfileActivity.this, "Updated", Toast.LENGTH_LONG).show();
                                Snackbar.make(ProfileActivity.this.getWindow().getDecorView().getRootView(), "Updated", Snackbar.LENGTH_LONG).show();


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
                        Toast.makeText(ProfileActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", first_name);
                params.put("last_name", last_name);
                params.put("headline", headline);
                params.put("email", email);
                params.put("user_id", S_user_id);
                params.put("image", image);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void getId() {
        profile_name = (TextView) findViewById(R.id.user_profile_name);
        profile_email = (TextView) findViewById(R.id.user_profile_short_bio);
        firstName_Profile = (EditText) findViewById(R.id.first_name);
        lastName_Profile = (EditText) findViewById(R.id.last_name);
        headLine_Profile = (EditText) findViewById(R.id.head_line);
        email_Profile = (EditText) findViewById(R.id.email);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        imgView = (ImageView) findViewById(R.id.user_profile_photo);
    }


    public void cartClick(View view) {
        startActivity(new Intent(this, GalleryActivity.class));
    }

    public void walletClick(View view) {
        startActivity(new Intent(this, WalletActivity.class));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
