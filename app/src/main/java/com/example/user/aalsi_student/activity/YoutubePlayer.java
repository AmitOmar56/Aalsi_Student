package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.adapter.CommentAdapter;
import com.example.user.aalsi_student.adapter.VideoScreenAdapter;
import com.example.user.aalsi_student.model.Comment;
import com.example.user.aalsi_student.model.VideoScreen;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.example.user.aalsi_student.utils.utils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.aalsi_student.activity.LoginActivity.S_user_id;
import static com.example.user.aalsi_student.utils.utils.dpToPx;

public class YoutubePlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, VideoScreenAdapter.Video_OnItemClicked {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer player;
    private TextView like;

    //    private String videoLink;
    private int videolikes;

    private RecyclerView youtubeRecyclerView;
    private VideoScreenAdapter adapter = null;
    private List<VideoScreen> videoScreenList;

    private String videoName;
    private int videoId;
    private String discription;
    private String main_videoLink;
    private int likes;
    private ImageView notavilable;
    private VideoScreen videoScreen = null;
//    private ProgressDialog dialog;

    private RecyclerView recyclerView;
    private CommentAdapter Cadapter;
    private List<Comment> commentList;
    private String video_id;
    private EditText comment_Edit;
    private TextView mShowDialog;
    private ImageButton send_comment;
    private String videoLink;
    private TextView video_disc;
    private Button btShowmore;
    private String commentString;
    private Comment comment = null;
    private String Comment_userName;
    private String CommentName;
    private ImageView nocomment;
    private int views;
    private TextView youtube_view;
    private int viewsold;
    private ImageView video_not_found;
    private String video_discription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        like = (TextView) findViewById(R.id.like);
        youtubeRecyclerView = (RecyclerView) findViewById(R.id.youtuberecyclerView);

        video_disc = (TextView) findViewById(R.id.video_disc);
        btShowmore = (Button) findViewById(R.id.btShowmore);
        youtube_view = (TextView) findViewById(R.id.you_view);
        video_not_found = (ImageView) findViewById(R.id.video_not_found);

        getdatafromIntent();

        video_disc.setText(video_discription);
        youtube_view.setText(viewsold + "");

        btShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btShowmore.getText().toString().equalsIgnoreCase("Showmore...")) {
                    video_disc.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btShowmore.setText("Showless");
                } else {
                    video_disc.setMaxLines(1);//your TextView
                    btShowmore.setText("Showmore...");
                }
            }
        });
        videoScreenList = new ArrayList<>();
        adapter = new VideoScreenAdapter(this, videoScreenList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        youtubeRecyclerView.setLayoutManager(mLayoutManager);
        youtubeRecyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        youtubeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        youtubeRecyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        signupRequest();

        like.setText(videolikes + "");


        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(com.example.user.aalsi_student.utils.Config.YOUTUBE_API_KEY, this);

        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();


        mShowDialog = (TextView) findViewById(R.id.comment);
        mShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(YoutubePlayer.this);
                View mView = getLayoutInflater().inflate(R.layout.comment_dialog, null);
                comment_Edit = (EditText) mView.findViewById(R.id.comment_Edittext);
                send_comment = (ImageButton) mView.findViewById(R.id.send_comment);
                nocomment = (ImageView) mView.findViewById(R.id.nocomment);


                comment_Edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence str, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence str, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        commentString = comment_Edit.getText().toString();
                        if (commentString.toString().trim().length() > 0) {
                            send_comment.setVisibility(View.VISIBLE);
                        } else {
                            send_comment.setVisibility(View.GONE);
                        }
                    }

                });

                recyclerView = (RecyclerView) mView.findViewById(R.id.commentRecyclerView);


                commentList = new ArrayList<>();
                Cadapter = new CommentAdapter(YoutubePlayer.this, commentList);

                RecyclerView.LayoutManager CmLayoutManager = new GridLayoutManager(YoutubePlayer.this, 1);
                recyclerView.setLayoutManager(CmLayoutManager);
                recyclerView.addItemDecoration(new utils.GridSpacingItemDecoration(2, dpToPx(YoutubePlayer.this, 10), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(Cadapter);

                commentApi();

                ImageButton mLogin = (ImageButton) mView.findViewById(R.id.send_comment);

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commentString = comment_Edit.getText().toString();
                        commentputApi();
                        Toast.makeText(YoutubePlayer.this, "press", Toast.LENGTH_LONG).show();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        LikeButton likeButton = (LikeButton) findViewById(R.id.youtube_star_button);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                videolikes = videolikes + 1;
                Log.d("likes", videolikes + "");
                like.setText(videolikes + "");
                likeApi(videolikes + "");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                videolikes = videolikes - 1;
                like.setText(videolikes + "");
                Log.d("likes", videolikes + "");
                likeApi(videolikes + "");
            }

        });
    }


    private void likeApi(final String videolikes) {
        Log.d("video_id", video_id);
        Log.d("Innerlikes", videolikes);
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/likes.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(YoutubePlayer.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("likes", videolikes + "");
                params.put("video_id", video_id + "");
                params.put("user_id", S_user_id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void commentApi() {
        commentList.clear();
        MyProgressDialog.showPDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;
        Log.d("amit", "amit");
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/get_comment.php?",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;


                            if (jsonObject.getInt("status") == 1) {
                                JSONArray data = jsonObject.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {

                                    JSONObject profile = data.getJSONObject(i);
                                    Comment_userName = profile.getString("name");

                                    CommentName = profile.getString("comment");
                                    Log.d("Comment_userName", Comment_userName + "");
                                    Log.d("CommentName", CommentName);

                                    comment = new Comment(Comment_userName, CommentName, "12/10/2017");
                                    commentList.add(comment);
                                    Cadapter.notifyDataSetChanged();
                                    comment_Edit.setText("");
                                    nocomment.setVisibility(View.GONE);

                                }
                                MyProgressDialog.hidePDialog();

                            } else {
                                MyProgressDialog.hidePDialog();
                                nocomment.setVisibility(View.VISIBLE);
                                Toast.makeText(YoutubePlayer.this, "No comment", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(YoutubePlayer.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("video_id", video_id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    private void commentputApi() {
        MyProgressDialog.showPDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;
        Log.d("amit", "amit");
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/comment.php?",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pd.hide();
                        //Response
//                        showSnackbar(response);
                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;


                            if (jsonObject.getInt("status") == 1) {

                                Log.d("dataFor Commemnt", "data" + "");
                                commentApi();
                                MyProgressDialog.hidePDialog();

                            } else {
                                MyProgressDialog.hidePDialog();
                                nocomment.setVisibility(View.VISIBLE);
                                Toast.makeText(YoutubePlayer.this, "No comment", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(YoutubePlayer.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("video_id", video_id);
                params.put("comment", commentString);
                params.put("st_id", S_user_id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    private void getdatafromIntent() {
        if (getIntent() != null) {
            main_videoLink = getIntent().getStringExtra("video_url");
            videolikes = Integer.parseInt(getIntent().getStringExtra("like"));
            video_id = getIntent().getStringExtra("v_id");
            viewsold = Integer.parseInt(getIntent().getStringExtra("views"));
            video_discription = getIntent().getStringExtra("disc");

            Log.d("views", views + "");
            Log.d("video_id", video_id + "");


        }
    }

    private void signupRequest() {


        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://aalsistudent.com/admin_panel/users/video.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getInt("status") == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.d("jsonObject", jsonObject + "");
                                Log.d("jsonArray", jsonArray + "");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject profile = jsonArray.getJSONObject(i);
                                    videoName = profile.getString("vi_name");
                                    videoId = profile.getInt("video_id");
                                    likes = profile.getInt("likes");
                                    discription = profile.getString("description");
                                    views = profile.getInt("views");
                                    videoLink = profile.getString("link");
                                    videoLink = videoLink.substring(30);


                                    Log.d("videoName", videoName + "");
                                    Log.d("videoId", videoId + "");
                                    Log.d("likes", likes + "");
                                    Log.d("discription", discription + "");
                                    Log.d("videoLink", "(" + videoLink + ")");
                                    Log.d("main_videoLink", "(" + main_videoLink + ")");

                                    if (!main_videoLink.equals(videoLink)) {
                                        video_not_found.setVisibility(View.GONE);

                                        videoScreen = new VideoScreen(discription, videoName, videoId, videoLink, 3.5, views, likes);
                                        videoScreenList.add(videoScreen);
                                    }
//                                    else {
//                                        video_not_found.setVisibility(View.VISIBLE);
//                                    }
                                    adapter.notifyDataSetChanged();

                                }

                                MyProgressDialog.hidePDialog();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(YoutubePlayer.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);
        Log.d("videoLink233", main_videoLink);

        if (!wasRestored) {
            Log.d("videoLink233", main_videoLink);
            player.cueVideo(main_videoLink); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(com.example.user.aalsi_student.utils.Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void Video_onItemClick(int position) {

        VideoScreen videoScreen = videoScreenList.get(position);
        String videoUrl = videoScreen.getVideo();
        int likes = videoScreen.getLikes();
        String videoid_new = String.valueOf(videoScreen.getVideo_id());
        Log.d("videoId====>>>", videoid_new + "");
        Toast.makeText(this, videoScreen.getVideo(), Toast.LENGTH_LONG).show();
        // startActivity(new Intent(this,VideoPlayerActivity.class));

        Intent intent = new Intent(this, YoutubePlayer.class);

        intent.putExtra("video_url", videoUrl);
        intent.putExtra("like", likes + "");
        intent.putExtra("v_id", videoid_new + "");
        intent.putExtra("views", videoScreen.getView() + "");
        intent.putExtra("disc", videoScreen.getDiscription());
        startActivity(intent);
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            showMessage("Playing");
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().
            showMessage("Paused");
        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.
            showMessage("Stopped");
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {
            // Called when the player is loading a video
            // At this point, it's not ready to accept commands affecting playback such as play() or pause()
        }

        @Override
        public void onLoaded(String s) {
            // Called when a video is done loading.
            // Playback methods such as play(), pause() or seekToMillis(int) may be called after this callback.
        }

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.
        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.
            viewsold = viewsold + 1;
            youtube_view.setText(viewsold + "");
            Log.d("views", views + "");
            viewsApiRequest(viewsold + "");
            Toast.makeText(YoutubePlayer.this, "One time play", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            // Called when an error occurs.
        }
    }

    private void viewsApiRequest(final String views) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;
        Log.d("amit", "amit");
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://aalsistudent.com/admin_panel/users/views.php?",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        pd.hide();
                        //Response
//                        showSnackbar(response);
                        Log.d("amit", response);
                        Object json = null;
                        try {
                            json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(YoutubePlayer.this, "Network Error", Toast.LENGTH_LONG).show();

                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("views", views + "");
                params.put("video_id", video_id);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }

    public void rating(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(YoutubePlayer.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_rating, null);

        final RelativeLayout ratingLayout = (RelativeLayout) mView.findViewById(R.id.ratingLayout);
        final Button thankyouButton = (Button) mView.findViewById(R.id.thankyouButton);
        final TextView thankyouText = (TextView) mView.findViewById(R.id.thankyouText);
        final RelativeLayout rateoptionLayout = (RelativeLayout) mView.findViewById(R.id.rateoptionLayout);
        final TextView quality = (TextView) mView.findViewById(R.id.quality);
        final TextView teacher = (TextView) mView.findViewById(R.id.teacher);
        final TextView course = (TextView) mView.findViewById(R.id.course);
        final TextView subject = (TextView) mView.findViewById(R.id.subject);
        final TextView online = (TextView) mView.findViewById(R.id.online);
        final Button optionNext = (Button) mView.findViewById(R.id.optionNext);
        final ImageView smily = (ImageView) mView.findViewById(R.id.smily);
        Glide.with(YoutubePlayer.this).load("http://aalsistudent.com/Apps/rsz_smily-md").into(smily);


        Button rateusButton = (Button) mView.findViewById(R.id.rateusButton);

        rateusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateoptionLayout.setVisibility(View.VISIBLE);
                ratingLayout.setVisibility(View.GONE);
                Toast.makeText(YoutubePlayer.this, "press", Toast.LENGTH_LONG).show();
            }
        });
//        thankyouButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(YoutubePlayer.this, "submit", Toast.LENGTH_LONG).show();
//
//            }
//        });
        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionNext.setVisibility(View.VISIBLE);
                quality.setBackgroundResource(R.drawable.button_border);

            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionNext.setVisibility(View.VISIBLE);
                teacher.setBackgroundResource(R.drawable.button_border);


            }
        });
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionNext.setVisibility(View.VISIBLE);
                course.setBackgroundResource(R.drawable.button_border);


            }
        });
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionNext.setVisibility(View.VISIBLE);
                subject.setBackgroundResource(R.drawable.button_border);


            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionNext.setVisibility(View.VISIBLE);
                online.setBackgroundResource(R.drawable.button_border);


            }
        });
        optionNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thankyouText.setVisibility(View.VISIBLE);
                rateoptionLayout.setVisibility(View.GONE);
                smily.setVisibility(View.VISIBLE);
//                thankyouButton.setVisibility(View.VISIBLE);


            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
