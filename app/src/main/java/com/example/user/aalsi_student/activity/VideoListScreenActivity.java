package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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


public class VideoListScreenActivity extends AppCompatActivity implements VideoScreenAdapter.Video_OnItemClicked {


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
    private int views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);
        MyProgressDialog.showPDialog(this);

        notavilable = (ImageView) findViewById(R.id.notavailable);

        /*****************code For Card View (Video) Start*****************/

        recyclerView = (RecyclerView) findViewById(R.id.videolist_recyclerView);
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
                                    views = profile.getInt("views");

                                    videoScreen = new VideoScreen(discription, videoName, videoId, videoLink, 3.5, views, likes);
                                    videoScreenList.add(videoScreen);

                                }
                                adapter.notifyDataSetChanged();
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
                        Toast.makeText(VideoListScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void Video_onItemClick(int position) {

        VideoScreen videoScreen = videoScreenList.get(position);
        String videoUrl = videoScreen.getVideo();
        videoId = videoScreen.getVideo_id();
        int likes = videoScreen.getLikes();
        Log.d("videoUrl", videoUrl + "");
        Toast.makeText(this, videoScreen.getVideo(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, YoutubePlayer.class);

        intent.putExtra("video_url", videoUrl);
        intent.putExtra("like", likes + "");
        intent.putExtra("v_id", videoId + "");
        intent.putExtra("views", videoScreen.getView() + "");
        intent.putExtra("disc", videoScreen.getDiscription());

        startActivity(intent);
    }
}
