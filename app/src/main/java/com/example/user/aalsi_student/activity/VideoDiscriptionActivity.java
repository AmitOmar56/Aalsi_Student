package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
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
import com.example.user.aalsi_student.adapter.VideoAdapter;
import com.example.user.aalsi_student.adapter.VideoScreenAdapter;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.model.VideoScreen;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class VideoDiscriptionActivity extends AppCompatActivity implements VideoScreenAdapter.Video_OnItemClicked {
    private TextView cutPrice;

    /**************For Video Player**************/
    private static final String TAG = "MainActivity";


    TextView loading;

    /**************For Video Player**************/
    /*****************For Card View (Video) Start*****************/

    private int course_id;
    private int course_fee;
    private String course_description;
    private String What_will_be_learn;
    private String video_url;
    private int check_id;
    private WebView webview;

    /*****************For Card View (Video) End*****************/

    private TextView course_disc;
    private ImageView instructor_image;
    private String course_logo;

    private RecyclerView recyclerView;
    private VideoScreenAdapter adapter = null;
    private List<VideoScreen> videoScreenList;

    private String videoName;
    private int videoId;
    private String discription;
    private String videoLink;
    private int likes;
    private ImageView notavilable;
    private VideoScreen videoScreen = null;
    private TextView instructor_name;
    private String teacher_name;
    private int view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_discription);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        MyProgressDialog.showPDialog(this);
        cutPrice = (TextView) findViewById(R.id.video_disc_cutprice);
        cutPrice.setPaintFlags(cutPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        /*************Code For Video Player*************/

        findIds();
        getdatafromIntent();

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);


        instructor_name.setText(teacher_name);
        signupRequest();
        relatedRequest();


        /*************Code For Video Player*************/


        /*****************code For Card View (Video) Start*****************/

        recyclerView = (RecyclerView) findViewById(R.id.instructor_recyclerView);


        videoScreenList = new ArrayList<>();
        adapter = new VideoScreenAdapter(this, videoScreenList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        LinearLayoutManager linearLayoutManager_new = new LinearLayoutManager(this);
        linearLayoutManager_new.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager_new);

        /*****************code For Card View (Video) End*****************/

    }

    @Override
    public void Video_onItemClick(int position) {

        VideoScreen videoScreen = videoScreenList.get(position);
        String videoUrl = videoScreen.getVideo();

        videoId = videoScreen.getVideo_id();
        int likes = videoScreen.getLikes();
        Log.d("videoUrl", videoUrl + "");
        Toast.makeText(this, videoScreen.getVideo(), Toast.LENGTH_LONG).show();
        // startActivity(new Intent(this,VideoPlayerActivity.class));

        Intent intent = new Intent(this, YoutubePlayer.class);

        intent.putExtra("video_url", videoUrl);
        intent.putExtra("like", likes + "");
        intent.putExtra("v_id", videoId + "");
        intent.putExtra("views", videoScreen.getView() + "");
        intent.putExtra("disc", videoScreen.getDiscription());

        startActivity(intent);
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            check_id = Integer.parseInt(getIntent().getStringExtra("id"));
            teacher_name = getIntent().getStringExtra("t_name");
            Log.d("cat_Id", check_id + "");
        }
    }


//    http://aalsistudent.com/admin_panel/api/Course/5a1818dbb1aad.jpg

    private void findIds() {
        loading = (TextView) findViewById(R.id.loading_text);
        course_disc = (TextView) findViewById(R.id.course_disc);
        webview = (WebView) findViewById(R.id.webview);
        instructor_image = (ImageView) findViewById(R.id.instructor_image);
        instructor_name = (TextView) findViewById(R.id.instructor_name);
    }

    public void buyNow(View view) {
        startActivity(new Intent(this, VideoListScreenActivity.class));

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    private void signupRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/course.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.d("jsonObject", jsonObject + "");
                            Log.d("jsonArray", jsonArray + "");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject profile = jsonArray.getJSONObject(i);
                                course_description = profile.getString("course_description");
                                What_will_be_learn = profile.getString("What_will_be_learn");
                                course_fee = profile.getInt("course_fee");
                                video_url = profile.getString("video_name");
                                course_id = profile.getInt("cor_id");
                                course_logo = profile.getString("course_logo");

                                Log.d("course_description", course_description);
                                Log.d("What_will_be_learn", What_will_be_learn + "");
                                Log.d("video_url", video_url);
                                Log.d("course_fee", course_fee + "");
                                Log.d("course_logo", course_logo + "");

                                if (check_id == course_id) {
                                    webview.loadUrl(video_url);
                                    course_disc.setText(course_description);
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    Log.d("course_logo", course_logo);
                                    Glide.with(VideoDiscriptionActivity.this).load(course_logo).into(instructor_image);
                                }
                            }
                            MyProgressDialog.hidePDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VideoDiscriptionActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void relatedRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/video.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("jsonObject", jsonObject + "");
                                Log.d("jsonArray", jsonArray + "");


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject profile = jsonArray.getJSONObject(i);
                                    videoName = profile.getString("vi_name");
                                    videoId = profile.getInt("video_id");
                                    likes = profile.getInt("likes");
                                    discription = profile.getString("description");
                                    videoLink = profile.getString("link");
                                    videoLink = videoLink.substring(30);
                                    view = profile.getInt("views");

                                    videoScreen = new VideoScreen(discription, videoName, videoId, videoLink, 3.5, view, likes);
                                    videoScreenList.add(videoScreen);

                                    adapter.notifyDataSetChanged();

                                }

                            } else {

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
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

}
