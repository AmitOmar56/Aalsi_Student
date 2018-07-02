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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.InstituteLayout;

import java.util.List;

/**
 * Created by user on 12/4/2017.
 */

public class InstituteProfile_Adapter extends RecyclerView.Adapter<InstituteProfile_Adapter.MyViewHolder> {
    private Context context;
    private int price;
    private int offer;
    private List<InstituteLayout> instituteLayoutList;

    //declare interface
    private InstituteProfile_Adapter.Instituteprofile_OnItemClicked onClick;

    //make interface like this
    public interface Instituteprofile_OnItemClicked {
        void instituteprofile_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView institue_name, institite_course_name, cross_price, full_price, offer;
        public ImageView institue_image;
        public RatingBar InstituteRatingBar;
        public CardView ins_card_view;

        public MyViewHolder(View view) {
            super(view);
            institue_name = (TextView) view.findViewById(R.id.institue_name);
            institite_course_name = (TextView) view.findViewById(R.id.institite_course_name);
            institue_image = (ImageView) view.findViewById(R.id.institue_image);
            InstituteRatingBar = (RatingBar) view.findViewById(R.id.InstituteRatingBar);
            cross_price = (TextView) view.findViewById(R.id.cross_price);
            full_price = (TextView) view.findViewById(R.id.full_price);
            offer = (TextView) view.findViewById(R.id.offer);
            ins_card_view = (CardView) view.findViewById(R.id.ins_card_view);
            cross_price.setPaintFlags(cross_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }
    }

    public InstituteProfile_Adapter(Context context, List<InstituteLayout> instituteLayoutList) {
        this.context = context;
        this.instituteLayoutList = instituteLayoutList;
    }

    @Override
    public InstituteProfile_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.institute_layout_card_view, parent, false);

        return new InstituteProfile_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InstituteProfile_Adapter.MyViewHolder holder, final int position) {
        InstituteLayout instituteLayout = instituteLayoutList.get(position);

        holder.institue_name.setText(instituteLayout.getInstituteName());
        holder.institite_course_name.setText(instituteLayout.getCourseName());
        holder.full_price.setText(instituteLayout.getInstitute_price() + "");
        holder.offer.setText(instituteLayout.getOffer() + "%");
        price = instituteLayout.getInstitute_price();
        offer = instituteLayout.getOffer();
        price = price + price * offer / 100;
        holder.cross_price.setText(price + "");


        // loading album cover using Glide library
        Glide.with(context).load(instituteLayout.getInstitute_course_image()).into(holder.institue_image);
        holder.ins_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.instituteprofile_onItemClick(position);
            }
        });
    }

    public void setOnClick(InstituteProfile_Adapter.Instituteprofile_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return instituteLayoutList.size();
    }
}