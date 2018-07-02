package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.InstituteAdapter;
import com.example.user.aalsi_student.adapter.NotificationAdapter;
import com.example.user.aalsi_student.adapter.VideoAdapter;
import com.example.user.aalsi_student.adapter.Viewpager_Adapter;
import com.example.user.aalsi_student.model.Course;
import com.example.user.aalsi_student.model.Institute;
import com.example.user.aalsi_student.model.Notification;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;
import com.example.user.aalsi_student.viewPager.ZoomOutPageTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class TeacherScreenActivity extends AppCompatActivity implements VideoAdapter.Video_OnItemClicked {

    private RecyclerView recyclerView_vertical;
    private VideoAdapter adapter;
    private List<Video> videoList;

    private String course_name;
    private String teacher_name;
    private String video_url;
    private int course_fee;
    private int teacher_id;
    private int sub_Id;
    private int check_id;
    private Video video = null;
    private ImageView notavilable;
    private int course_id;
    private String course_logo;
    private int offer;

    /********************code for view pager************************/
    private Viewpager_Adapter viewpager_adapter;
    private ViewPager viewPager;
    int images[] = {R.drawable.poster1, R.drawable.poster_2, R.drawable.poster_3, R.drawable.poster_4};
    private Handler handler;
    private int delay = 1500; //milliseconds
    private int page = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            if (viewpager_adapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    /********************code for view pager************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_screen);
        MyProgressDialog.showPDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);
        notavilable = (ImageView) findViewById(R.id.notavailable);
        getdatafromIntent();

        /*****************(Start) code For Card View Vertical*****************/

        recyclerView_vertical = (RecyclerView) findViewById(R.id.teacher_vertical_recyclerView);
        videoList = new ArrayList<>();
        adapter = new VideoAdapter(this, videoList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView_vertical.setLayoutManager(mLayoutManager);
        recyclerView_vertical.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView_vertical.setItemAnimator(new DefaultItemAnimator());
        recyclerView_vertical.setAdapter(adapter);
        adapter.setOnClick(this);
        /*****************(End) code For Card View Vertical*****************/

        signupRequest();

        /***************(start) code for viewPager*************************/

        handler = new Handler();
        viewPager = (ViewPager) findViewById(R.id.teacherViewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewpager_adapter = new Viewpager_Adapter(this, images);

        viewPager.setAdapter(viewpager_adapter);
        tabLayout.setupWithViewPager(viewPager, true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /***************(End) code for viewPager*************************/
    }

    /********************code for view pager************************/

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    /********************code for view pager************************/


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

    private void getdatafromIntent() {
        if (getIntent() != null) {
            check_id = Integer.parseInt(getIntent().getStringExtra("id"));
            Log.d("cat_Id", check_id + "");
        }
    }

    private void signupRequest() {
        videoList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/instr.php",
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
                                course_name = profile.getString("course_name");
                                teacher_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                video_url = profile.getString("video_name");
                                sub_Id = profile.getInt("subcat_id");
                                course_id = profile.getInt("cor_id");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("teacher_name", teacher_name + "");
                                Log.d("video_url", video_url);
                                Log.d("course_fee", course_fee + "");
                                Log.d("sub_Id", sub_Id + "");

                                if (check_id == sub_Id) {
//                                    course = new Course(cat_Name, sub_Id, images[1]);
//                                    courseList.add(course);
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    video = new Video(course_name, teacher_name, video_url, 3.5, course_fee, course_id, course_logo, offer);
                                    videoList.add(video);

                                }

                                adapter.notifyDataSetChanged();

                            }
                            if (video == null) {
                                notavilable.setVisibility(View.VISIBLE);
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
                        Toast.makeText(TeacherScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

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

    public void price(View view) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_sort, null);
        RelativeLayout discountLayout = (RelativeLayout) mView.findViewById(R.id.discountLayout);
        RelativeLayout pricelthLayout = (RelativeLayout) mView.findViewById(R.id.pricelthLayout);
        RelativeLayout pricehtlLayout = (RelativeLayout) mView.findViewById(R.id.pricehtlLayout);
        final BottomSheetDialog dialog = new BottomSheetDialog(TeacherScreenActivity.this, R.style.ThemeCustomDialog);
        dialog.setContentView(mView);
        dialog.show();
        discountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.showPDialog(TeacherScreenActivity.this);
                discountApi();
                dialog.dismiss();
            }
        });
        pricelthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.showPDialog(TeacherScreenActivity.this);
                lowtohigh();
                dialog.dismiss();
            }
        });
        pricehtlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.showPDialog(TeacherScreenActivity.this);
                hightolow();
                dialog.dismiss();
            }
        });
    }

    private void lowtohigh() {
        videoList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/cor_price.php",
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
                                course_name = profile.getString("course_name");
                                teacher_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                video_url = profile.getString("video_name");
                                sub_Id = profile.getInt("subcat_id");
                                course_id = profile.getInt("cor_id");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("teacher_name", teacher_name + "");
                                Log.d("video_url", video_url);
                                Log.d("course_fee", course_fee + "");
                                Log.d("sub_Id", sub_Id + "");

                                if (check_id == sub_Id) {
//                                    course = new Course(cat_Name, sub_Id, images[1]);
//                                    courseList.add(course);
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    video = new Video(course_name, teacher_name, video_url, 3.5, course_fee, course_id, course_logo, offer);
                                    videoList.add(video);

                                }

                                adapter.notifyDataSetChanged();

                            }
                            if (video == null) {
                                notavilable.setVisibility(View.VISIBLE);
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
                        Toast.makeText(TeacherScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void hightolow() {
        videoList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/cor_price_desc.php",
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
                                course_name = profile.getString("course_name");
                                teacher_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                video_url = profile.getString("video_name");
                                sub_Id = profile.getInt("subcat_id");
                                course_id = profile.getInt("cor_id");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("teacher_name", teacher_name + "");
                                Log.d("video_url", video_url);
                                Log.d("course_fee", course_fee + "");
                                Log.d("sub_Id", sub_Id + "");

                                if (check_id == sub_Id) {
//                                    course = new Course(cat_Name, sub_Id, images[1]);
//                                    courseList.add(course);
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    video = new Video(course_name, teacher_name, video_url, 3.5, course_fee, course_id, course_logo, offer);
                                    videoList.add(video);

                                }

                                adapter.notifyDataSetChanged();

                            }
                            if (video == null) {
                                notavilable.setVisibility(View.VISIBLE);
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
                        Toast.makeText(TeacherScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void discountApi() {
        videoList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/cor_discount.php",
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
                                course_name = profile.getString("course_name");
                                teacher_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                video_url = profile.getString("video_name");
                                sub_Id = profile.getInt("subcat_id");
                                course_id = profile.getInt("cor_id");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("teacher_name", teacher_name + "");
                                Log.d("video_url", video_url);
                                Log.d("course_fee", course_fee + "");
                                Log.d("sub_Id", sub_Id + "");

                                if (check_id == sub_Id) {
//                                    course = new Course(cat_Name, sub_Id, images[1]);
//                                    courseList.add(course);
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    video = new Video(course_name, teacher_name, video_url, 3.5, course_fee, course_id, course_logo, offer);
                                    videoList.add(video);

                                }

                                adapter.notifyDataSetChanged();

                            }
                            if (video == null) {
                                notavilable.setVisibility(View.VISIBLE);
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
                        Toast.makeText(TeacherScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }
}
