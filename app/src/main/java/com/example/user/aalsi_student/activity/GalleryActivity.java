package com.example.user.aalsi_student.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.InstituteAdapter;
import com.example.user.aalsi_student.adapter.VideoAdapter;
import com.example.user.aalsi_student.model.Institute;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class GalleryActivity extends AppCompatActivity implements InstituteAdapter.Institute_OnItemClicked, VideoAdapter.Video_OnItemClicked {

    private RecyclerView recyclerView_horizontal;
    private InstituteAdapter instituteAdapter;
    private List<Institute> instituteList;

    private RecyclerView recyclerView_vertical;
    private VideoAdapter adapter;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        /*****************(Start) code For Card View Vertical*****************/

        recyclerView_vertical = (RecyclerView) findViewById(R.id.relatedVideoRecyclerView);
        videoList = new ArrayList<>();
        adapter = new VideoAdapter(this, videoList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView_vertical.setLayoutManager(mLayoutManager);
        recyclerView_vertical.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView_vertical.setItemAnimator(new DefaultItemAnimator());
        recyclerView_vertical.setAdapter(adapter);
        adapter.setOnClick(this);

        LinearLayoutManager linearLayoutManagerCourse = new LinearLayoutManager(this);
        linearLayoutManagerCourse.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_vertical.setLayoutManager(linearLayoutManagerCourse);
        /*****************(End) code For Card View Vertical*****************/

        /*****************(Start) Code For Card View Horizontal*************/

        recyclerView_horizontal = (RecyclerView) findViewById(R.id.relatedCourseRecyclerView);
        instituteList = new ArrayList<>();
        instituteAdapter = new InstituteAdapter(this, instituteList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView_horizontal.setLayoutManager(layoutManager);
        recyclerView_horizontal.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView_horizontal.setItemAnimator(new DefaultItemAnimator());
        recyclerView_horizontal.setAdapter(instituteAdapter);
        instituteAdapter.setOnClick(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_horizontal.setLayoutManager(linearLayoutManager);

        /*****************(End) code For Card View Horizontal*****************/

        prepareAlbums();
        prepareAlbums1();
    }

    @Override
    public void institute_onItemClick(int position) {

    }

    @Override
    public void Video_onItemClick(int position) {

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

        Institute a = new Institute("", 5.0, images[4]);
        instituteList.add(a);

        a = new Institute("", 4.8, images[3]);
        instituteList.add(a);

        a = new Institute("", 3.9, images[1]);
        instituteList.add(a);

        a = new Institute("", 4.2, images[0]);
        instituteList.add(a);

        a = new Institute("", 4.1, images[2]);
        instituteList.add(a);

        a = new Institute("", 3.9, images[5]);
        instituteList.add(a);

        a = new Institute("", 5.0, images[6]);
        instituteList.add(a);

        a = new Institute("", 3.6, images[7]);
        instituteList.add(a);

        a = new Institute("", 5.0, images[8]);
        instituteList.add(a);

        a = new Institute("", 3.9, images[9]);
        instituteList.add(a);


        instituteAdapter.notifyDataSetChanged();
    }

    /*******************Code for RecyclerView****************/

    private void prepareAlbums1() {

        Video a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000, 1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        adapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
