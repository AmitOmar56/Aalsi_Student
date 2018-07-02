package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.VideoScreenAdapter;
import com.example.user.aalsi_student.model.VideoScreen;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class VideoMainScreenActivity extends AppCompatActivity implements VideoScreenAdapter.Video_OnItemClicked {

    private RecyclerView recyclerView;
    private VideoScreenAdapter adapter = null;
    private List<VideoScreen> videoScreenList;

    private String videoName;
    private int videoId;
    private String discription;
    private String videoLink;
    private int likes;
    private ImageView notavilable, video_mainImage;
    private VideoScreen videoScreen = null;
    private int views;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main_screen);

        notavilable = (ImageView) findViewById(R.id.notavailable);
        video_mainImage = (ImageView) findViewById(R.id.video_mainImage);
        Glide.with(this).load("http://aalsistudent.com/Apps/as-video").into(video_mainImage);
        /*****************code For Card View (Video) Start*****************/

        recyclerView = (RecyclerView) findViewById(R.id.video_mainScreen_recyclerView);


        videoScreenList = new ArrayList<>();
        adapter = new VideoScreenAdapter(this, videoScreenList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        signupRequest();

        /*****************code For Card View End*****************/

    }

    private void signupRequest() {
        MyProgressDialog.showPDialog(this);
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
                                    views = profile.getInt("views");
                                    videoLink = profile.getString("link");
                                    videoLink = videoLink.substring(30);
                                    Log.d("video.....>>>>", videoLink);
                                    Log.d("views", views + "");
                                    videoScreen = new VideoScreen(discription, videoName, videoId, videoLink, 3.5, views, likes);
                                    videoScreenList.add(videoScreen);

                                    adapter.notifyDataSetChanged();

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
                        Toast.makeText(VideoMainScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

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

    public void videoSearch(View view) {
        startActivity(new Intent(this, Video_SearchActivity.class));
    }

    public void videoScreenBack(View view) {
        finish();
    }
}
