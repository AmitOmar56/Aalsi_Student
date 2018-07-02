package com.example.user.aalsi_student.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.CoursesAdapter;
import com.example.user.aalsi_student.fragment.NotificationFragment;
import com.example.user.aalsi_student.model.Course;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import static com.example.user.aalsi_student.activity.LoginActivity.lresult;
import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class Main_ScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CoursesAdapter.OnItemClicked {

    private RecyclerView recyclerView;
    private CoursesAdapter adapter;
    private List<Course> courseList;
    private String cat_Name;
    private String cat_Image;
    private int cat_Id;
    private Course course = null;
    int[] images = new int[]{
            R.drawable.hobby,
            R.drawable.board,
            R.drawable.entrance_exam,
            R.drawable.technical_courses,
            R.drawable.book1,
            R.drawable.book2,
            R.drawable.book3,
            R.drawable.book4,
            R.drawable.book2,
            R.drawable.book3,
            R.drawable.book4,};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);

        /*****************code For Card View Start*****************/

        recyclerView = (RecyclerView) findViewById(R.id.mainScreen_recycler_view);


        courseList = new ArrayList<>();
        adapter = new CoursesAdapter(this, courseList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);
//        recyclerView.smoothScrollToPosition(adapter.getItemCount());

//        prepareAlbums();
        signupRequest();
        /*****************code For Card View End*****************/


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), Main_ScreenActivity.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_notification) {

            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.main, notificationFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_admission) {

            Intent intent = new Intent(getApplicationContext(), AdmissionActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Aalsi student,Aalsi student,Aalsi student,Aalsi student,Aalsi student,Aalsi student" + "http://www.aalsistudent.com/");
            startActivity(Intent.createChooser(i, "Share via"));


        } else if (id == R.id.nav_send) {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.sourceDir;

            Intent intent = new Intent(Intent.ACTION_SEND);

            // MIME of .apk is "application/vnd.android.package-archive".
            // but Bluetooth does not accept this. Let's use "*/*" instead.
            intent.setType("*/*");
            // Append file and send Intent
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            startActivity(Intent.createChooser(intent, "Share app via"));
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

            this.startActivity(intent);

        } else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_logout) {
            lresult = false;
            insert();
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_change_passwod) {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signupRequest() {
        MyProgressDialog.showPDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/category_root.php",
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
//                                private String cat_Name;
//                                private String cat_Image;
//                                private String cat_Id;
                                JSONObject profile = jsonArray.getJSONObject(i);
                                cat_Name = profile.getString("cat_name");
                                cat_Id = Integer.parseInt(profile.getString("cat_id"));
                                cat_Image = profile.getString("image");
                                Log.d("cat_Name", cat_Name);
                                Log.d("cat_Id", cat_Id + "");
                                Log.d("cat_Image", cat_Image);


                                course = new Course(cat_Name, cat_Id, images[i]);
                                courseList.add(course);


                                adapter.notifyDataSetChanged();

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
                        MyProgressDialog.hidePDialog();
                        Toast.makeText(Main_ScreenActivity.this, "network problem", Toast.LENGTH_LONG).show();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    @Override
    public void onItemClick(int position) {
        Course course = courseList.get(position);
        Log.d("abcbcbc", course.getCourse_Id() + "");
        Toast.makeText(Main_ScreenActivity.this, course.getCourse_Id() + "", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, CourseScreen_2Activity.class);

        intent.putExtra("id", course.getCourse_Id() + "");

        startActivity(intent);

    }

    public void job(View v) {
        Intent intent = new Intent(getApplicationContext(), AdmissionActivity.class);
        this.startActivity(intent);
    }

    public void course(View v) {
        Intent intent = new Intent(getApplicationContext(), Main_ScreenActivity.class);
        this.startActivity(intent);
    }

    public void notification(View v) {
        NotificationFragment notificationFragment = new NotificationFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main, notificationFragment);
        fragmentTransaction.commit();
    }

    public void video(View view) {

        Intent intent = new Intent(getApplicationContext(), VideoMainScreenActivity.class);
        this.startActivity(intent);
    }

    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_executed", false);
        edt.commit();
    }

    public void course_search(View view) {
        startActivity(new Intent(getApplicationContext(), Course_SearchActivity.class));
    }

}
