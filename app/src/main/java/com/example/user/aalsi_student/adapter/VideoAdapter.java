package com.example.user.aalsi_student.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.Course;
import com.example.user.aalsi_student.model.Video;

import java.util.List;

/**
 * Created by user on 10/28/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context mContext;
    private List<Video> videoList;
    private int price;
    private int offer;

    //declare interface
    private VideoAdapter.Video_OnItemClicked onClick;

    //make interface like this
    public interface Video_OnItemClicked {
        void Video_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView course_name, teacher_name, count, cut_price, course_offer;
        public RatingBar myRatingBar;
        public ImageView overflow, teacher_image;
        public CardView card_view;
        public LinearLayout video_layout;

        public MyViewHolder(View view) {
            super(view);
            course_name = (TextView) view.findViewById(R.id.course_name);
            teacher_name = (TextView) view.findViewById(R.id.teacher_name);
            count = (TextView) view.findViewById(R.id.count);
            myRatingBar = (RatingBar) view.findViewById(R.id.myRatingBar);
            card_view = (CardView) view.findViewById(R.id.card_view);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            video_layout = (LinearLayout) view.findViewById(R.id.video_layout);
            teacher_image = (ImageView) view.findViewById(R.id.teacher_image);
            course_offer = (TextView) view.findViewById(R.id.course_offer);
            cut_price = (TextView) view.findViewById(R.id.cut_price);
            cut_price.setPaintFlags(cut_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }


    public VideoAdapter(Context mContext, List<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }

    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_video_card_view, parent, false);

        return new VideoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.MyViewHolder holder, final int position) {
        Video video = videoList.get(position);
        holder.course_name.setText(video.getCourse_name());
        holder.teacher_name.setText(video.getTeacher_name());
        holder.cut_price.setText(video.getPrice() + "");
        holder.course_offer.setText(video.getCourse_offer() + "%");
        price = video.getPrice();
        offer = video.getCourse_offer();
        price = price - price * offer / 100;
        holder.count.setText(price + "");
        Glide.with(mContext).load(video.getCourse_logo()).into(holder.teacher_image);

//        video.setVideoPath("http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v");
//        video.start();
//        MediaController mediaController = new MediaController(mContext);
//        mediaController.setAnchorView(video);
//
//        video.setVideo("http://content.hungama.com/music%20video%20track/video%20content/full%20m4v%20640x480%20public/1793008097.m4v");
//        video.set


        //        holder.count.setText(course.getNumOfSongs() +"");

        // loading album cover using Glide library

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        holder.video_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.Video_onItemClick(position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
//         inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_course, popup.getMenu());
        popup.setOnMenuItemClickListener(new VideoAdapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        //
        public MyMenuItemClickListener() {
        }

        //
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
                default:
            }
            return false;
        }
    }

    public void setOnClick(VideoAdapter.Video_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

}
