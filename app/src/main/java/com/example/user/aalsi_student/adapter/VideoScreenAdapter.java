package com.example.user.aalsi_student.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.VideoScreen;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

/**
 * Created by user on 11/20/2017.
 */

public class VideoScreenAdapter extends RecyclerView.Adapter<VideoScreenAdapter.MyViewHolder> {

    private Context mContext;
    private List<VideoScreen> videoScreenList;

    //declare interface
    private VideoScreenAdapter.Video_OnItemClicked onClick;

    //make interface like this
    public interface Video_OnItemClicked {
        void Video_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView course_name, teacher_name, likes, discription, no_of_view, video_discription;
        public RatingBar myRatingBar;
        public ImageView overflow;
        public CardView card_view;
        public LinearLayout video_layout;
        private ImageButton start;
        private YouTubeThumbnailView youtube_view;

        public MyViewHolder(View view) {
            super(view);
//            course_name = (TextView) view.findViewById(R.id.course_name);
//            teacher_name = (TextView) view.findViewById(R.id.teacher_name);
            likes = (TextView) view.findViewById(R.id.likes);
            myRatingBar = (RatingBar) view.findViewById(R.id.myRatingBar);
            card_view = (CardView) view.findViewById(R.id.card_view);
            video_layout = (LinearLayout) view.findViewById(R.id.video_layout);
            discription = (TextView) view.findViewById(R.id.disc);
            overflow = (ImageView) view.findViewById(R.id.videoScreenoverflow);
            no_of_view = (TextView) view.findViewById(R.id.view);
            youtube_view = (YouTubeThumbnailView) view.findViewById(R.id.youtube_view);
            video_discription = (TextView) view.findViewById(R.id.video_discription);

        }
    }


    public VideoScreenAdapter(Context mContext, List<VideoScreen> videoScreenList) {
        this.mContext = mContext;
        this.videoScreenList = videoScreenList;
    }

    @Override
    public VideoScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videoscreen_card_view, parent, false);

        return new VideoScreenAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoScreenAdapter.MyViewHolder holder, final int position) {

        final VideoScreen videoScreen = videoScreenList.get(position);
        holder.video_discription.setText(videoScreen.getDiscription());
        holder.discription.setText(videoScreen.getCoursename());
        holder.likes.setText(videoScreen.getLikes() + "");
        holder.no_of_view.setText(videoScreen.getView() + "");

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                //  holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.youtube_view.initialize(com.example.user.aalsi_student.utils.Config.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videoScreen.getVideo());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

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
        popup.setOnMenuItemClickListener(new VideoScreenAdapter.MyMenuItemClickListener());
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

    public void setOnClick(VideoScreenAdapter.Video_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return videoScreenList.size();
    }

}
