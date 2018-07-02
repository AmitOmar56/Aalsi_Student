package com.example.user.aalsi_student.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.ExpandableListAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Course_timingActivity extends AppCompatActivity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_timing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.boy);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

             /*
     * Preparing the list data
	 */

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Monday");
        listDataHeader.add("Tuesday");
        listDataHeader.add("Wednesday");
        listDataHeader.add("Thursday");
        listDataHeader.add("Friday");
        listDataHeader.add("Saturday");
        listDataHeader.add("Sunday");

        // Adding child data
        List<String> monday = new ArrayList<String>();
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        monday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");

        List<String> tuesday = new ArrayList<String>();
        tuesday.add("2:00pm  Sohna road  2 hours English");
        tuesday.add("2:00pm  Sohna road  2 hours English");
        tuesday.add("2:00pm  Sohna road  2 hours English");
        tuesday.add("2:00pm  Sohna road  2 hours English");
        tuesday.add("2:00pm  Sohna road  2 hours English");
        tuesday.add("2:00pm  Sohna road  2 hours English");
        tuesday.add("2:00pm  Sohna road  2 hours English");

        List<String> wednesday = new ArrayList<String>();
        wednesday.add("2:00pm  Sohna road  2 hours English");
        wednesday.add("2:00pm  Sohna road  2 hours English");
        wednesday.add("2:00pm  Sohna road  2 hours English");
        wednesday.add("2:00pm  Sohna road  2 hours English");
        wednesday.add("2:00pm  Sohna road  2 hours English");

        List<String> thursday = new ArrayList<String>();
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");
        thursday.add("2:00pm" + " , " + "Dur.- " + "2" + "hours" + " , " + "English" + "\n" + "Add:- " + "Sohna road");

        List<String> friday = new ArrayList<String>();
        friday.add("2:00pm  Sohna road  2 hours English");
        friday.add("2:00pm  Sohna road  2 hours English");
        friday.add("2:00pm  Sohna road  2 hours English");
        friday.add("2:00pm  Sohna road  2 hours English");
        friday.add("2:00pm  Sohna road  2 hours English");
        friday.add("2:00pm  Sohna road  2 hours English");
        friday.add("2:00pm  Sohna road  2 hours English");


        List<String> saturday = new ArrayList<String>();
        saturday.add("2:00pm  Sohna road  2 hours English");
        saturday.add("2:00pm  Sohna road  2 hours English");
        saturday.add("2:00pm  Sohna road  2 hours English");
        saturday.add("2:00pm  Sohna road  2 hours English");
        saturday.add("2:00pm  Sohna road  2 hours English");

        List<String> sunday = new ArrayList<String>();
        sunday.add("2:00pm  Sohna road  2 hours English");
        sunday.add("2:00pm  Sohna road  2 hours English");
        sunday.add("2:00pm  Sohna road  2 hours English");
        sunday.add("2:00pm  Sohna road  2 hours English");
        sunday.add("2:00pm  Sohna road  2 hours English");
        sunday.add("2:00pm  Sohna road  2 hours English");
        sunday.add("2:00pm  Sohna road  2 hours English");

        listDataChild.put(listDataHeader.get(0), monday); // Header, Child data
        listDataChild.put(listDataHeader.get(1), tuesday);
        listDataChild.put(listDataHeader.get(2), wednesday);
        listDataChild.put(listDataHeader.get(3), thursday);
        listDataChild.put(listDataHeader.get(4), friday);
        listDataChild.put(listDataHeader.get(5), saturday);
        listDataChild.put(listDataHeader.get(6), sunday);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
