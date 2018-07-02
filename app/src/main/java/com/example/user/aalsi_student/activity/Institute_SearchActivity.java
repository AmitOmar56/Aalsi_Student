package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.InstituteProfile_Adapter;
import com.example.user.aalsi_student.model.InstituteLayout;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class Institute_SearchActivity extends AppCompatActivity implements InstituteProfile_Adapter.Instituteprofile_OnItemClicked {

    private RecyclerView institute_recyclerView;
    private List<InstituteLayout> instituteLayoutList;
    private InstituteProfile_Adapter instituteProfile_adapter;

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
    private EditText i_searchBarEText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute__search);

        /*****************(Start) code For Card View*****************/

        i_searchBarEText = (EditText) findViewById(R.id.i_searchBarEText);
        notavilable = (ImageView) findViewById(R.id.notavailable);

        institute_recyclerView = (RecyclerView) findViewById(R.id.institute_search_recyclerView);
        instituteLayoutList = new ArrayList<>();
        instituteProfile_adapter = new InstituteProfile_Adapter(this, instituteLayoutList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        institute_recyclerView.setLayoutManager(mLayoutManager);
        institute_recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        institute_recyclerView.setItemAnimator(new DefaultItemAnimator());
        institute_recyclerView.setAdapter(instituteProfile_adapter);
        instituteProfile_adapter.setOnClick(this);

        /*****************(End) code For Card View Vertical*****************/
        i_searchBarEText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                signupRequest(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void instituteprofile_onItemClick(int position) {
        InstituteLayout instituteLayout = instituteLayoutList.get(position);
        Toast.makeText(this, instituteLayout.getCourseName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Institute_ProfileActivity.class);
        intent.putExtra("id", instituteLayout.getInstitute_id() + "");
        startActivity(intent);
    }

    private void signupRequest(final CharSequence charSequence) {
        instituteLayoutList.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/search_adm.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;

                            if (jsonObject.getInt("status") == 1) {
                                instituteLayoutList.clear();
                                notavilable.setVisibility(View.GONE);
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

                                    course_logo = "http://aalsistudent.com/admin_panel/api/" + course_logo;
                                    instituteLayout = new InstituteLayout(institute_name, course_name, course_fee, 3.5, offer, institute_id, course_logo);
                                    instituteLayoutList.add(instituteLayout);

                                    instituteProfile_adapter.notifyDataSetChanged();
                                }
                            } else {
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
                        Toast.makeText(Institute_SearchActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("keyword", charSequence + "");

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void i_searchBackButton(View view) {
        startActivity(new Intent(this, Institute_SearchActivity.class));
    }

}
