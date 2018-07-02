package com.example.user.aalsi_student.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.activity.AllCoursesActivity;
import com.example.user.aalsi_student.activity.Main_ScreenActivity;
import com.example.user.aalsi_student.activity.TeacherScreenActivity;
import com.example.user.aalsi_student.adapter.CoursesAdapter;
import com.example.user.aalsi_student.model.Course;
import com.example.user.aalsi_student.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends Fragment implements CoursesAdapter.OnItemClicked {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private CoursesAdapter adapter;
    private List<Course> courseList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobFragment newInstance(String param1, String param2) {
        JobFragment fragment = new JobFragment();
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
        View view = inflater.inflate(R.layout.fragment_job, container, false);

        /*****************code For Card View*****************/

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        courseList = new ArrayList<>();
        adapter = new CoursesAdapter(getContext(), courseList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(getActivity(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        prepareAlbums();

        /*****************code For Card View*****************/

        return view;
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

    @Override
    public void onItemClick(int position) {
        Course course = courseList.get(position);
        Toast.makeText(getActivity(), course.getCourse_Name(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), AllCoursesActivity.class);
        startActivity(intent);
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

        Course a = new Course("PHP", 13, images[0]);
        courseList.add(a);

        a = new Course("Java", 8, images[1]);
        courseList.add(a);

        a = new Course("Android 5", 11, images[2]);
        courseList.add(a);

        a = new Course("Python", 12, images[3]);
        courseList.add(a);

        a = new Course("Data", 14, images[4]);
        courseList.add(a);

        a = new Course("My sql", 1, images[5]);
        courseList.add(a);

        a = new Course("Java", 11, images[6]);
        courseList.add(a);

        a = new Course("Python", 14, images[7]);
        courseList.add(a);

        a = new Course("Android", 11, images[8]);
        courseList.add(a);

        a = new Course("Data", 17, images[9]);
        courseList.add(a);

        adapter.notifyDataSetChanged();
    }
    /*******************Code for RecyclerView****************/

}
