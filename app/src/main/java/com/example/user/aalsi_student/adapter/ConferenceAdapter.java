package com.example.user.aalsi_student.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.Conference;
import java.util.List;

/**
 * Created by user on 10/31/2017.
 */

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.MyViewHolder> {

    private Context mContext;
    private List<Conference> conferenceList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView topic,date,time;

        public MyViewHolder(View view) {
            super(view);
            topic = (TextView) view.findViewById(R.id.conference_class_topic);
            date = (TextView) view.findViewById(R.id.conference_class_date);
            time = (TextView) view.findViewById(R.id.conference_class_time);
        }
    }


    public ConferenceAdapter(Context mContext, List<Conference> courseList) {
        this.mContext = mContext;
        this.conferenceList = courseList;
    }

    @Override
    public ConferenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conference_card_view, parent, false);

        return new ConferenceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ConferenceAdapter.MyViewHolder holder, final int position) {
        Conference conference = conferenceList.get(position);
        holder.topic.setText(conference.getTopic());
        holder.date.setText(conference.getDate());
        holder.time.setText(conference.getTime());


    }


    @Override
    public int getItemCount() {
        return conferenceList.size();
    }
}

