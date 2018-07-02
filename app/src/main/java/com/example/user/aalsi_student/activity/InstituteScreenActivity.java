package com.example.user.aalsi_student.activity;

import android.content.Intent;
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
import android.widget.ImageView;
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
import com.example.user.aalsi_student.adapter.InstituteProfile_Adapter;
import com.example.user.aalsi_student.adapter.NotificationAdapter;
import com.example.user.aalsi_student.adapter.Viewpager_Adapter;
import com.example.user.aalsi_student.model.Institute;
import com.example.user.aalsi_student.model.InstituteLayout;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class InstituteScreenActivity extends AppCompatActivity implements InstituteProfile_Adapter.Instituteprofile_OnItemClicked {

    private RecyclerView institute_recyclerView;
    private List<InstituteLayout> instituteLayoutList;
    private InstituteProfile_Adapter instituteProfile_adapter;

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
    private String course_name;
    private String institute_name;
    private int course_fee;
    private String course_logo;
    private int offer;
    private int subcat_Id;
    private int institute_id;
    private InstituteLayout instituteLayout = null;
    private ImageView notavilable;
    private int key_id;

    /********************code for view pager************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);
        MyProgressDialog.showPDialog(this);
        getdatafromIntent();

        /*****************(Start) code For Card View*****************/
        notavilable = (ImageView) findViewById(R.id.notavailable);

        institute_recyclerView = (RecyclerView) findViewById(R.id.vertical_recyclerView);
        instituteLayoutList = new ArrayList<>();
        instituteProfile_adapter = new InstituteProfile_Adapter(this, instituteLayoutList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        institute_recyclerView.setLayoutManager(mLayoutManager);
        institute_recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        institute_recyclerView.setItemAnimator(new DefaultItemAnimator());
        institute_recyclerView.setAdapter(instituteProfile_adapter);
        instituteProfile_adapter.setOnClick(this);

        /*****************(End) code For Card View Vertical*****************/

        /***************(start) code for viewPager*************************/

        handler = new Handler();
        viewPager = (ViewPager) findViewById(R.id.institute_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
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
        signupRequest();
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

    private void getdatafromIntent() {
        if (getIntent() != null) {
            key_id = Integer.parseInt(getIntent().getStringExtra("subCat_id"));
            Log.d("cat_Id", key_id + "");
        }
    }

    @Override
    public void instituteprofile_onItemClick(int position) {
        InstituteLayout instituteLayout = instituteLayoutList.get(position);
        Toast.makeText(this, instituteLayout.getCourseName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Institute_ProfileActivity.class);
        intent.putExtra("id", instituteLayout.getInstitute_id()+"");
        startActivity(intent);
    }

    private void signupRequest() {
        instituteLayoutList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/admission.php",
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
                                subcat_Id = profile.getInt("subcat_id");
                                institute_id = profile.getInt("inst_id");
                                course_name = profile.getString("sub_cat_name");
                                institute_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("institute_name", institute_name + "");
                                Log.d("course_fee", course_fee + "");
                                Log.d("course_logo", course_logo + "");
                                Log.d("offer", offer + "");
                                Log.d("subcat_Id", subcat_Id + "");
                                Log.d("institute_id", institute_id + "");


                                if (key_id == subcat_Id) {
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    instituteLayout = new InstituteLayout(institute_name, course_name, course_fee, 3.5, offer, institute_id, course_logo);
                                    instituteLayoutList.add(instituteLayout);
                                }
                                instituteProfile_adapter.notifyDataSetChanged();
                            }
                            if (instituteLayout == null) {
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
                        Toast.makeText(InstituteScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void sort(View view) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_sort, null);
        RelativeLayout discountLayout = (RelativeLayout) mView.findViewById(R.id.discountLayout);
        RelativeLayout pricelthLayout = (RelativeLayout) mView.findViewById(R.id.pricelthLayout);
        RelativeLayout pricehtlLayout = (RelativeLayout) mView.findViewById(R.id.pricehtlLayout);
        final BottomSheetDialog dialog = new BottomSheetDialog(InstituteScreenActivity.this, R.style.ThemeCustomDialog);
        dialog.setContentView(mView);
        dialog.show();
        discountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.showPDialog(InstituteScreenActivity.this);
                discountApi();
                dialog.dismiss();
            }
        });
        pricelthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.showPDialog(InstituteScreenActivity.this);
                lowtohigh();
                dialog.dismiss();
            }
        });
        pricehtlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProgressDialog.showPDialog(InstituteScreenActivity.this);
                hightolow();
                dialog.dismiss();
            }
        });
    }

    private void lowtohigh() {
        instituteLayoutList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/adm_price.php",
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
                                subcat_Id = profile.getInt("subcat_id");
                                institute_id = profile.getInt("inst_id");
                                course_name = profile.getString("sub_cat_name");
                                institute_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("institute_name", institute_name + "");
                                Log.d("course_fee", course_fee + "");
                                Log.d("course_logo", course_logo + "");
                                Log.d("offer", offer + "");
                                Log.d("subcat_Id", subcat_Id + "");
                                Log.d("institute_id", institute_id + "");


                                if (key_id == subcat_Id) {
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    instituteLayout = new InstituteLayout(institute_name, course_name, course_fee, 3.5, offer, institute_id, course_logo);
                                    instituteLayoutList.add(instituteLayout);
                                }
                                instituteProfile_adapter.notifyDataSetChanged();
                            }
                            if (instituteLayout == null) {
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
                        Toast.makeText(InstituteScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void hightolow() {
        instituteLayoutList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/adm_price_dsc.php",
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
                                subcat_Id = profile.getInt("subcat_id");
                                institute_id = profile.getInt("inst_id");
                                course_name = profile.getString("sub_cat_name");
                                institute_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("institute_name", institute_name + "");
                                Log.d("course_fee", course_fee + "");
                                Log.d("course_logo", course_logo + "");
                                Log.d("offer", offer + "");
                                Log.d("subcat_Id", subcat_Id + "");
                                Log.d("institute_id", institute_id + "");


                                if (key_id == subcat_Id) {
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    instituteLayout = new InstituteLayout(institute_name, course_name, course_fee, 3.5, offer, institute_id, course_logo);
                                    instituteLayoutList.add(instituteLayout);
                                }
                                instituteProfile_adapter.notifyDataSetChanged();
                            }
                            if (instituteLayout == null) {
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
                        Toast.makeText(InstituteScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void discountApi() {
        instituteLayoutList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/adm_discount.php",
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
                                subcat_Id = profile.getInt("subcat_id");
                                institute_id = profile.getInt("inst_id");
                                course_name = profile.getString("sub_cat_name");
                                institute_name = profile.getString("name");
                                course_fee = profile.getInt("course_fee");
                                course_logo = profile.getString("course_logo");
                                offer = profile.getInt("discount");


                                Log.d("course_name", course_name);
                                Log.d("institute_name", institute_name + "");
                                Log.d("course_fee", course_fee + "");
                                Log.d("course_logo", course_logo + "");
                                Log.d("offer", offer + "");
                                Log.d("subcat_Id", subcat_Id + "");
                                Log.d("institute_id", institute_id + "");


                                if (key_id == subcat_Id) {
                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    instituteLayout = new InstituteLayout(institute_name, course_name, course_fee, 3.5, offer, institute_id, course_logo);
                                    instituteLayoutList.add(instituteLayout);
                                }
                                instituteProfile_adapter.notifyDataSetChanged();
                            }
                            if (instituteLayout == null) {
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
                        Toast.makeText(InstituteScreenActivity.this, "Network Error", Toast.LENGTH_LONG).show();

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


}
