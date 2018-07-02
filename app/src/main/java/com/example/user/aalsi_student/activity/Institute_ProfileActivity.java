package com.example.user.aalsi_student.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.user.aalsi_student.adapter.InstituteServicesAdapter;
import com.example.user.aalsi_student.adapter.VideoAdapter;
import com.example.user.aalsi_student.model.InstituteServices;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class Institute_ProfileActivity extends AppCompatActivity implements OnMapReadyCallback, InstituteServicesAdapter.InstituteServices_OnItemClicked, VideoAdapter.Video_OnItemClicked {
    private GoogleMap mMap;

    private RecyclerView recyclerView;
    private InstituteServicesAdapter institute_adapter;
    private List<InstituteServices> instituteServicesList;
    private TextView institute_name;
    private TextView course_level;
    private TextView ins_cut_price;
    private TextView ins_full_price;
    private TextView ins_discount;
    private TextView ins_fee;
    private TextView ins_duration;
    private TextView disc_text;
    private ImageView institute_profile_img;

    private String ins_Name;
    private String ins_level;
    private String ins_logo;
    private String ins_course_name;
    private String ins_course_duration;
    private String ins_discription;
    private String ins_offer;
    private int ins_fees;
    private int ins_id;
    private InstituteServices instituteServices = null;
    private int key_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute__profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        MyProgressDialog.showPDialog(this);
        getId();
        getdatafromIntent();
        signupRequest();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*************Code For Dialog Box Start************/

        Button mShowDialog = (Button) findViewById(R.id.institute_profile_button_online);

        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Institute_ProfileActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_online_option, null);

                Button mLogin = (Button) mView.findViewById(R.id.onlineVideoButton);

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(Institute_ProfileActivity.this, OnlineVideoScreenActivity.class));
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
        /*************Code For Dialog Box End*************/


        /**********Code For Graph Start*****************/
        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 0)); /***Red***/
        entries.add(new BarEntry(8f, 1)); /***Orange***/
        entries.add(new BarEntry(15f, 3)); /***Yellow***/
        entries.add(new BarEntry(20f, 4)); /***Green***/
        entries.add(new BarEntry(5f, 2)); /***Brown***/


        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("poor");
        labels.add("Avarage");
        labels.add("Good");
        labels.add("Very Good");
        labels.add("Excellent");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of lables into chart

        barChart.setDescription("Set Bar Chart Description");  // set the description

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        barChart.animateY(5000);
        /**************Code For Graph End*************/

        /*****************code For Card View Start*****************/
//
        recyclerView = (RecyclerView) findViewById(R.id.service_recyclerView);
        instituteServicesList = new ArrayList<>();
        institute_adapter = new InstituteServicesAdapter(this, instituteServicesList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(institute_adapter);
        institute_adapter.setOnClick(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        prepareAlbums();

        /*****************code For Card View End*****************/

    }

    private void getId() {
        institute_name = (TextView) findViewById(R.id.institute_name);
        course_level = (TextView) findViewById(R.id.course_level);
        ins_cut_price = (TextView) findViewById(R.id.ins_cut_price);
        ins_full_price = (TextView) findViewById(R.id.ins_full_price);
        ins_discount = (TextView) findViewById(R.id.ins_discount);
        ins_fee = (TextView) findViewById(R.id.ins_fee);
        ins_duration = (TextView) findViewById(R.id.ins_duration);
        disc_text = (TextView) findViewById(R.id.disc_text);
        institute_profile_img = (ImageView) findViewById(R.id.institute_profile_img);


    }

    @Override
    public void instituteServices_onItemClick(int position) {
        InstituteServices instituteServices = instituteServicesList.get(position);
        Toast.makeText(this, instituteServices.getInstitute_name(), Toast.LENGTH_LONG).show();
//        Course course = courseList.get(position);
//        Toast.makeText(Main_ScreenActivity.this, course.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void Video_onItemClick(int position) {

    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            key_id = Integer.parseInt(getIntent().getStringExtra("id"));
            Log.d("key_id", key_id + "");
        }
    }

    /**********************Code For Show Map (Start)******************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.boy);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(28.4109501, 77.0408846);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My location")).setIcon(icon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    /**********************Code For Show Map (End)******************/

    public void course_timing(View view) {
        startActivity(new Intent(this, Course_timingActivity.class));
    }

    public void online_courses_timing(View view) {
        startActivity(new Intent(this, Conference_ClassTimingActivity.class));
    }


    private void signupRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/adm.php",
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
                                ins_Name = profile.getString("name");
                                ins_level = profile.getString("course_level");
                                ins_fees = profile.getInt("course_fee");
                                ins_course_name = profile.getString("course_name");
                                ins_course_duration = profile.getString("course_duration");
                                ins_id = profile.getInt("inst_id");
                                ins_logo = profile.getString("image");
                                ins_discription = profile.getString("instr_desc");
                                ins_offer = profile.getString("discount");

                                if (key_id == ins_id) {
                                    institute_name.setText(ins_Name);
                                    course_level.setText(ins_level);
                                    ins_cut_price.setText(ins_fees + "");
                                    ins_full_price.setText(ins_fees + "");
                                    ins_discount.setText(ins_offer + "%");
                                    ins_fee.setText(ins_fees + "");
                                    ins_duration.setText(ins_course_duration);
                                    disc_text.setText(ins_discription);

                                    Glide.with(Institute_ProfileActivity.this).load("http://aalsistudent.com/admin_panel/api/" + ins_logo).into(institute_profile_img);


                                }
                            }
                            MyProgressDialog.hidePDialog();
                            MyProgressDialog.hidePDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Institute_ProfileActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] images = new int[]{
                R.drawable.banner5,
                R.drawable.banner3,
                R.drawable.banner2,
                R.drawable.banner4,
                R.drawable.banner1,
                R.drawable.banner6,
                R.drawable.banner5,
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner6,};

        InstituteServices a = new InstituteServices("", 5.0, images[4]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 4.8, images[3]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 3.9, images[1]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 4.2, images[0]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 4.1, images[2]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 3.9, images[5]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 5.0, images[6]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 3.6, images[7]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 5.0, images[8]);
        instituteServicesList.add(a);

        a = new InstituteServices("", 3.9, images[9]);
        instituteServicesList.add(a);

        institute_adapter.notifyDataSetChanged();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


}
