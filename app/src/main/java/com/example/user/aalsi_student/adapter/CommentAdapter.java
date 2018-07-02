package com.example.user.aalsi_student.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.Comment;

import java.util.List;

/**
 * Created by user on 11/27/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context mContext;
    private List<Comment> commentList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name,user_comment,comment_date;

        public MyViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.user_name);
            user_comment = (TextView) view.findViewById(R.id.user_comment);
            comment_date = (TextView) view.findViewById(R.id.comment_date);
        }
    }


    public CommentAdapter(Context mContext, List<Comment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_card_view, parent, false);

        return new CommentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.MyViewHolder holder, final int position) {
        Comment comment = commentList.get(position);
        holder.user_name.setText(comment.getName());
        holder.user_comment.setText(comment.getComment());
        holder.comment_date.setText(comment.getDate());

    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

