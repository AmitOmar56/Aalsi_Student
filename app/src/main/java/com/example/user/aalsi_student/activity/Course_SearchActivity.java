package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.VideoAdapter;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class Course_SearchActivity extends AppCompatActivity implements VideoAdapter.Video_OnItemClicked {

    private RecyclerView recyclerView_vertical;
    private VideoAdapter adapter;
    private List<Video> videoList;
    private EditText searchBarEText;

    private String course_name;
    private String teacher_name;
    private String video_url;
    private int course_fee;
    private int teacher_id;
    private int sub_Id;
    private int check_id;
    private Video video = null;
    private ImageView notavailable;
    private int course_id;
    private String course_logo;
    private int offer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__search);

        notavailable = (ImageView) findViewById(R.id.notavailable);
        searchBarEText = (EditText) findViewById(R.id.searchBarEText);
        /*****************(Start) code For Card View Vertical*****************/

        recyclerView_vertical = (RecyclerView) findViewById(R.id.course_search_recyclerView);
        videoList = new ArrayList<>();
        adapter = new VideoAdapter(this, videoList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView_vertical.setLayoutManager(mLayoutManager);
        recyclerView_vertical.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView_vertical.setItemAnimator(new DefaultItemAnimator());
        recyclerView_vertical.setAdapter(adapter);
        adapter.setOnClick(this);

        /*****************(End) code For Card View Vertical*****************/
        searchBarEText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                videoList.clear();
//                video = null;
                signupRequest(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void signupRequest(final CharSequence charSequence) {

//        video = null;
//        videoList.clear();

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/search_cor.php",
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
                                videoList.clear();
                                notavailable.setVisibility(View.GONE);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject profile = jsonArray.getJSONObject(i);
                                    course_name = profile.getString("course_name");
                                    teacher_name = profile.getString("name");
                                    course_fee = profile.getInt("course_fee");
                                    video_url = profile.getString("video_name");
                                    sub_Id = profile.getInt("subcat_id");
                                    course_id = profile.getInt("cor_id");
                                    course_logo = profile.getString("course_logo");
                                    offer = profile.getInt("discount");


                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    video = new Video(course_name, teacher_name, video_url, 3.5, course_fee, course_id, course_logo, offer);
                                    videoList.add(video);


                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                notavailable.setVisibility(View.VISIBLE);
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
                        Toast.makeText(Course_SearchActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("keyword", charSequence + "");

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    @Override
    public void Video_onItemClick(int position) {
//        startActivity(new Intent(this, VideoDiscriptionActivity.class));
        Video video = videoList.get(position);
        Toast.makeText(this, video.getCourse() + "" + "", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, VideoDiscriptionActivity.class);
        intent.putExtra("id", video.getCourse() + "");
        intent.putExtra("t_name", video.getTeacher_name());
        startActivity(intent);

    }

    public void c_searchBackButton(View view) {
        finish();
    }
}
