package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.VideoView;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.VideoAdapter;
import com.example.user.aalsi_student.model.Video;
import com.example.user.aalsi_student.utils.utils;
import java.util.ArrayList;
import java.util.List;
import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class OnlineVideoScreenActivity extends AppCompatActivity implements VideoAdapter.Video_OnItemClicked {
    private VideoView videoView;
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_video_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);
        /*****************code For Card View Start*****************/

        recyclerView = (RecyclerView) findViewById(R.id.online_video_recyclerView);


        videoList = new ArrayList<>();
        adapter = new VideoAdapter(this, videoList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        prepareAlbums();

        /*****************code For Card View End*****************/

//        /*******************code for video view******************/
//        videoView = (VideoView) findViewById(R.id.video);
//        videoView.setVideoPath("http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v");
//        videoView.start();
////        videoView.canPause();
////        videoView.seekTo(10);
//        /***********************code for video view end***************/
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
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

        Video a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        a = new Video("Advance Php", "R.k.Verma", "http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v", 3.5, 5000,1,"http://aalsistudent.com/admin_panel/api/Course//5a28d698afde0.jpg",20);
        videoList.add(a);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void Video_onItemClick(int position) {

        startActivity(new Intent(OnlineVideoScreenActivity.this, VideoDiscriptionActivity.class));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
