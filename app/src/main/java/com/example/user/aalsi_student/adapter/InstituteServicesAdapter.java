package com.example.user.aalsi_student.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.model.InstituteServices;

import java.util.List;

/**
 * Created by user on 10/27/2017.
 */

public class InstituteServicesAdapter  extends RecyclerView.Adapter<InstituteServicesAdapter.MyViewHolder> {
    private Context context;
    private List<InstituteServices> instituteServicesList;

    //declare interface
    private InstituteServicesAdapter.InstituteServices_OnItemClicked onClick;

    //make interface like this
    public interface InstituteServices_OnItemClicked {
        void instituteServices_onItemClick(int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.service_title);
            count = (TextView) view.findViewById(R.id.service_count);
            thumbnail = (ImageView) view.findViewById(R.id.service_thumbnail);
            overflow = (ImageView) view.findViewById(R.id.service_overflow);
        }
    }
    public InstituteServicesAdapter (Context context,List<InstituteServices>instituteServicesList){
        this.context=context;
        this.instituteServicesList=instituteServicesList;
    }
    @Override
    public InstituteServicesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.institute_service_card_view, parent, false);

        return new InstituteServicesAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final InstituteServicesAdapter.MyViewHolder holder, final int position) {
        InstituteServices instituteServices = instituteServicesList.get(position);
        holder.title.setText(instituteServices.getInstitute_name());
        holder.count.setText(instituteServices.getInstitute_rating() +"");

        // loading album cover using Glide library
        Glide.with(context).load(instituteServices.getInstitute_image()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.instituteServices_onItemClick(position);
            }
        });
    }
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
//         inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_course, popup.getMenu());
        popup.setOnMenuItemClickListener(new InstituteServicesAdapter.MyMenuItemClickListener());
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
                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
                default:
            }
            return false;
        }
    }
    public void setOnClick(InstituteServicesAdapter.InstituteServices_OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return instituteServicesList.size();
    }
}