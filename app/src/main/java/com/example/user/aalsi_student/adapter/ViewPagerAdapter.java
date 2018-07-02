package com.example.user.aalsi_student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.aalsi_student.fragment.Institute_Fragment;
import com.example.user.aalsi_student.fragment.JobFragment;
import com.example.user.aalsi_student.fragment.NotificationFragment;
import com.example.user.aalsi_student.fragment.ProfileFragment;
import com.example.user.aalsi_student.fragment.Teacher_Fragment;

/**
 * Created by user on 10/24/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Institute_Fragment();
        }
        else if (position == 1)
        {
            fragment = new Teacher_Fragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Institute";
        }
        else if (position == 1)
        {
            title = "Teacher";
        }
        return title;
    }
}