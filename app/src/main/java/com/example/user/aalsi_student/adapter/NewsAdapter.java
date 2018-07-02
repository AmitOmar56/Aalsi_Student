package com.example.user.aalsi_student.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.News;

import java.util.List;

/**
 * Created by user on 11/7/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private Context context;
    private List<News> newsList;

    //declare interface
    private NewsAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView newsName;
        public ImageView imageView ;

        public MyViewHolder(View view) {
            super(view);
            newsName = (TextView) view.findViewById(R.id.newsTeaxt);
            imageView = (ImageView) view.findViewById(R.id.newsImage);
        }
    }
    public NewsAdapter (Context context,List<News>newsList){
        this.context=context;
        this.newsList=newsList;
    }
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_news_card_view, parent, false);

        return new NewsAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final NewsAdapter.MyViewHolder holder, final int position) {
        News news = newsList.get(position);
        holder.newsName.setText(news.getNews_name());

        // loading album cover using Glide library
        Glide.with(context).load(news.getNews_image()).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }
       public void setOnClick(NewsAdapter.News_OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}