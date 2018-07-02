package com.example.user.aalsi_student.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.ConferenceAdapter;
import com.example.user.aalsi_student.model.Conference;
import com.example.user.aalsi_student.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class Conference_ClassTimingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConferenceAdapter adapter;
    private List<Conference> conferenceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference__class_timing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        /*****************code For Card View*****************/

        recyclerView = (RecyclerView) findViewById(R.id.conferenceRecyclerView);


        conferenceList = new ArrayList<>();
        adapter = new ConferenceAdapter(this, conferenceList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);

        prepareAlbums();

        /*****************code For Card View*****************/
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

        Conference a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a =new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        a = new Conference("BCA","10-12-2017", "2:00PM");
        conferenceList.add(a);

        adapter.notifyDataSetChanged();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
