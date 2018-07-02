package com.example.user.aalsi_student.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.activity.CourseScreen_2Activity;
import com.example.user.aalsi_student.activity.Institute_ProfileActivity;
import com.example.user.aalsi_student.activity.Main_ScreenActivity;
import com.example.user.aalsi_student.adapter.CoursesAdapter;
import com.example.user.aalsi_student.adapter.InstituteAdapter;
import com.example.user.aalsi_student.adapter.NotificationAdapter;
import com.example.user.aalsi_student.model.Course;
import com.example.user.aalsi_student.model.Institute;
import com.example.user.aalsi_student.model.Institute;
import com.example.user.aalsi_student.model.Notification;
import com.example.user.aalsi_student.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Institute_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Institute_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Institute_Fragment extends Fragment implements NotificationAdapter.OnItemClicked, InstituteAdapter.Institute_OnItemClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private InstituteAdapter instituteAdapter;
    private List<Institute> instituteList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Institute_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Institute_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Institute_Fragment newInstance(String param1, String param2) {
        Institute_Fragment fragment = new Institute_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institute_, container, false);


        /*****************(Start) code For Card View*****************/

        recyclerView = (RecyclerView) view.findViewById(R.id.vertical_recyclerView);
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(), notificationList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(getActivity(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        /*****************(End) code For Card View Vertical*****************/

        /*****************(Start) Code For Card View Horizontal*************/
        recyclerView = (RecyclerView) view.findViewById(R.id.horizontal_recyclerView);
        instituteList = new ArrayList<>();
        instituteAdapter = new InstituteAdapter(getContext(), instituteList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(getActivity(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(instituteAdapter);
        instituteAdapter.setOnClick(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*****************(End) code For Card View Horizontal*****************/
        prepareAlbums();
        prepareAlbums1();
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Notification notification = notificationList.get(position);
        Toast.makeText(getActivity(), notification.getName(), Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getActivity(),Institute_ProfileActivity.class);
        startActivity(intent);
    }
    @Override
    public void institute_onItemClick(int position) {
        Institute institute = instituteList.get(position);
        Toast.makeText(getActivity(), institute.getInstitute_rating()+"", Toast.LENGTH_LONG).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /*******************Code for RecyclerView****************/
    /**
     * Adding few albums for testing
     */

    private void prepareAlbums() {
        int[] images = new int[]{
                R.drawable.book4,
                R.drawable.book3,
                R.drawable.book2,
                R.drawable.book1,
                R.drawable.book4,
                R.drawable.book3,
                R.drawable.book2,
                R.drawable.book4,
                R.drawable.book3,
                R.drawable.book2,
                R.drawable.book1,
        };

        Notification a = new Notification("PHP", 13, images[0]);
        notificationList.add(a);

        a = new Notification("Java", 8, images[1]);
        notificationList.add(a);

        a = new Notification("Android 5", 11, images[2]);
        notificationList.add(a);

        a = new Notification("Python", 12, images[3]);
        notificationList.add(a);

        a = new Notification("Data", 14, images[4]);
        notificationList.add(a);

        a = new Notification("My sql", 1, images[5]);
        notificationList.add(a);

        a = new Notification("Java", 11, images[6]);
        notificationList.add(a);

        a = new Notification("Python", 14, images[7]);
        notificationList.add(a);

        a = new Notification("Android", 11, images[8]);
        notificationList.add(a);

        a = new Notification("Data", 17, images[9]);
        notificationList.add(a);

        adapter.notifyDataSetChanged();
    }
    /*******************Code for RecyclerView****************/
    /**
     * Adding few albums for testing
     */
    private void prepareAlbums1() {
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

        adapter.notifyDataSetChanged();
    }
}
