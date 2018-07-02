package com.example.user.aalsi_student.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.NewsAdapter;
import com.example.user.aalsi_student.model.News;
import com.example.user.aalsi_student.utils.utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.aalsi_student.utils.utils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements NewsAdapter.News_OnItemClicked{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        /*****************code For Card View*****************/

        recyclerView = (RecyclerView) view.findViewById(R.id.notification_recycler_view);
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(getContext(), newsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(getActivity(),10), true));
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
    public void news_onItemClick(int position) {
        News news=newsList.get(position);
        Toast.makeText(getActivity(),news.getNews_name(),Toast.LENGTH_LONG).show();
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
                R.drawable.banner1,
                R.drawable.book2,
                R.drawable.book1,
                R.drawable.banner2,
                R.drawable.book3,
                R.drawable.banner3,
                R.drawable.book4,
                R.drawable.banner4,
                R.drawable.book2,
                R.drawable.book1,
        };

        News a = new News("Kejriwal calls Delhi 'gas chamber' as air pollution hits severe levels, visibility down to 200m.", images[0]);
        newsList.add(a);

        a = new News("Recovery of US-made rifle shows Pak complicity in Kashmir militancy: Army", images[1]);
        newsList.add(a);

        a = new News("The recovery of US-made rifle, meant for Pakistani army",  images[2]);
        newsList.add(a);

        a = new News("This weapon (the M4 carbine) is with the special forces of Pakistan army. ",  images[3]);
        newsList.add(a);

        a = new News("A police spokesperson said that it was the same group",  images[4]);
        newsList.add(a);

        a = new News("This weapon (the M4 carbine) is with the special forces of Pakistan army. ",  images[5]);
        newsList.add(a);

        a = new News("A police spokesperson said that it was the same group",  images[6]);
        newsList.add(a);

        a = new News("he three slain militants killed on Monday night were identified as Waseem Ganaie",  images[7]);
        newsList.add(a);

        a = new News("he three slain militants killed on Monday night were identified as Waseem Ganaie",  images[8]);
        newsList.add(a);

        a = new News("A police spokesperson said that it was the same group",  images[9]);
        newsList.add(a);

        adapter.notifyDataSetChanged();
    }
    /*******************Code for RecyclerView****************/

}
