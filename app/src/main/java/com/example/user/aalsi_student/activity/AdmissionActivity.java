package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class AdmissionActivity extends AppCompatActivity implements CoursesAdapter.OnItemClicked {

    private RecyclerView recyclerView;
    private CoursesAdapter adapter;
    private List<Course> courseList;
    public PlaceAutocompleteFragment autocompleteFragment;
    private String TAG = "Place_Api_Activity";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String city_Name;
    private String latitude;
    private String longitude;

    private String cat_Name;
    private String cat_Image;
    private int cat_Id;
    private Course course = null;
    int[] images = new int[]{
            R.drawable.ug,
            R.drawable.pg,
            R.drawable.book2,
            R.drawable.book1
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);


        placeApi();
        autocompleteFragment.setText("Gurgaon");

        /*****************code For Card View*****************/

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        courseList = new ArrayList<>();
        adapter = new CoursesAdapter(this, courseList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        signupRequest();

        /*****************code For Card View*****************/


    }

    @Override
    public void onItemClick(int position) {
        Course course = courseList.get(position);
        Toast.makeText(this, course.getCourse_Name(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AllCoursesActivity.class);

        intent.putExtra("cat_id", course.getCourse_Id() + "");
        startActivity(intent);
    }

    /****************For Place Api Start********************/

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void placeApi() {
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        /*
        * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
        * set a filter returning only results with a precise address.
        */
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint("Enter Your Location");
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(10.0f);
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint(" Enter Your Location");
        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).setVisibility(View.GONE);

        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).setLayoutParams(new LinearLayout.LayoutParams(70, 25));

        autocompleteFragment.getView().setBackground(getResources().getDrawable(R.drawable.place_search_box));
        ((View) findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);

        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).setImageResource(R.drawable.downimage);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, place.getLatLng().toString());
                LatLng Location = place.getLatLng();
                Log.i(TAG, "Place: " + Location.latitude);
                Log.i(TAG, "Place: " + Location.longitude);

                Double place_lat = Location.latitude;
                Double place_lng = Location.longitude;
                latitude = place_lat.toString();
                longitude = place_lng.toString();

                Log.d("latitude", place.toString());
                Log.d("longitude", longitude);


                try {
                    Geocoder geocoder = new Geocoder(AdmissionActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(place_lat, place_lng, 1);

                    //city_Name = (addresses.get(0).getLocality() == null) ? "" : addresses.get(0).getLocality().trim();
                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    /****************For Place Api End********************/

    private void signupRequest() {
        MyProgressDialog.showPDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/category_adm.php",
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
                        Toast.makeText(AdmissionActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    /*******************Code for RecyclerView****************/


    /*******************Code for RecyclerView****************/

    public void admissionBackButton(View view) {
        finish();
    }

    public void admissionSearch(View view) {
        startActivity(new Intent(this, Institute_SearchActivity.class));
    }
}
