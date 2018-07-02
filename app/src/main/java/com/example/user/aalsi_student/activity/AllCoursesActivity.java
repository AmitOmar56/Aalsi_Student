package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.CoursesAdapter;
import com.example.user.aalsi_student.model.Course;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class AllCoursesActivity extends AppCompatActivity implements CoursesAdapter.OnItemClicked {


    private RecyclerView recyclerView;
    private CoursesAdapter adapter;
    private List<Course> courseList;
    private String cat_Name;
    private String cat_Image;
    private int sub_Id;
    private int cat_Id;
    private int subCat_id;
    private Course course = null;
    int[] images = new int[]{
            R.drawable.book1,
            R.drawable.book2,
            R.drawable.book3,
            R.drawable.book4,
            R.drawable.book1,
            R.drawable.book2,
            R.drawable.book3,
            R.drawable.book4,
            R.drawable.book2,
            R.drawable.book3,
            R.drawable.book4,};

    private ImageView notavailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        /*****************code For Card View Start*****************/
        getdatafromIntent();
        recyclerView = (RecyclerView) findViewById(R.id.allCoursesRecyclerView);
        notavailable = (ImageView) findViewById(R.id.notavailable);

        courseList = new ArrayList<>();
        adapter = new CoursesAdapter(this, courseList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
//        recyclerView.smoothScrollToPosition(adapter.getItemCount());

        signupRequest();
        /*****************code For Card View End*****************/
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            cat_Id = Integer.parseInt(getIntent().getStringExtra("cat_id"));
            Log.d("cat_Id", cat_Id + "");
        }
    }

    @Override
    public void onItemClick(int position) {
        Course course = courseList.get(position);
        Toast.makeText(this, course.getCourse_Name(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, InstituteScreenActivity.class);
        intent.putExtra("subCat_id", course.getCourse_Id() + "");
        startActivity(intent);
    }

    private void signupRequest() {
        MyProgressDialog.showPDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/sub_category.php",
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
                                cat_Name = profile.getString("sub_cat_name");
                                sub_Id = Integer.parseInt(profile.getString("cat_id"));
                                subCat_id = profile.getInt("subcat_id");
                                Log.d("subcat_id", subCat_id + "");
                                Log.d("cat_Id", cat_Id + "");

                                if (cat_Id == sub_Id) {
                                    course = new Course(cat_Name, subCat_id, images[1]);
                                    courseList.add(course);
                                }

                                adapter.notifyDataSetChanged();

                            }
                            if (course == null) {
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
                        Toast.makeText(AllCoursesActivity.this, "Network Error", Toast.LENGTH_LONG).show();

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
