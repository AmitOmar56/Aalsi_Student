package com.example.user.aalsi_student.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.aalsi_student.R;
import com.example.user.aalsi_student.viewPager.DynamicViewPager;


public class HomeScreenActivity extends AppCompatActivity {
    private static final int MAX_PAGES = 4;
    private DynamicViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        /********************code for hash code******************/

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.user.aalsi_student", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.e("MY KEY HASH:", sign);
//                Log.d("sign->>>>>>>>>>>>>>>>>", sign);
//                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//        } catch (NoSuchAlgorithmException e) {
//        }
        /********************code for hash code******************/

        init_id();


        viewPager.setMaxPages(MAX_PAGES);
        viewPager.setBackgroundAsset(R.mipmap.poster);
        viewPager.setAdapter(new MyPagerAdapter());

        /*if (savedInstanceState != null) {
            num_pages = savedInstanceState.getInt("num_pages");
            viewPager.setCurrentItem(savedInstanceState.getInt("current_page"), false);
        }*/
    }


    private void init_id() {
        viewPager = (DynamicViewPager) findViewById(R.id.pager);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putInt("num_pages", num_pages);
        outState.putInt("current_page", viewPager.getCurrentItem());*/
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return MAX_PAGES;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_page, null);
            TextView num = (TextView) view.findViewById(R.id.page_number);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            String pos = "";
            int image = 0;

            //String pos = "This is page " + (position + 1);
            if (position == 0) {
                pos = "            " + "Take Video Courses" + "\n" + "        " + "From cooking to coding" + "\n" + "     " + "and everything in between";
                image = R.drawable.postar_img1;
            }
            if (position == 1) {
                pos = "              " + "Go at Your Own Pace" + "\n" + "         " + "Lifetime access to courses," + "\n" + "       " + "watch them anytime,anywhere";
                image = R.drawable.poster_img2;
            }
            if (position == 2) {
                pos = "            " + "Take Video Courses" + "\n" + "      " + "From cooking to coding" + "\n" + "     " + "and everything in between";
                image = R.drawable.poster_img5;
            }
            if (position == 3) {
                pos = "              " + "Go at Your Own Pace" + "\n" + "         " + "Lifetime access to courses," + "\n" + "       " + "watch them anytime,anywhere";
                image = R.drawable.postar_img3;
            }
            img.setImageResource(image);
            num.setText(pos);

            container.addView(view);

            return view;
        }
    }

    public void register(View v) {
        startActivity(new Intent(HomeScreenActivity.this, RegisterActivity.class));

    }

    public void login(View v) {
        startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class));
    }

}


