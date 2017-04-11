package unimelb.snapchat.LoginAdnRegi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import unimelb.snapchat.R;


public class GuideActivity  extends Activity {

    private ViewPager mViewPager;
    private ImageView mPage0;
    private ImageView mPage1;
    private ImageView mPage2;
    private ImageView mPage3;
    private int currIndex = 0;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_main);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());


        mPage0 = (ImageView)findViewById(R.id.page0);
        mPage1 = (ImageView)findViewById(R.id.page1);
        mPage2 = (ImageView)findViewById(R.id.page2);
        mPage3 = (ImageView)findViewById(R.id.page3);

        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.view1, null);
        View view2 = mLi.inflate(R.layout.view2, null);
        View view3 = mLi.inflate(R.layout.view3, null);
        View view4 = mLi.inflate(R.layout.view4, null);

        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);

        PagerAdapter mPagerAdapter = new PagerAdapter() {

            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            public int getCount() {
                return views.size();
            }

            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }

            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(views.get(position));
                return views.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    break;
                case 1:
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    break;
                case 2:
                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
                    break;
                case 3:
                    mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
                    mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));

            }

            currIndex = arg0;
        }


        public void onPageScrollStateChanged(int state) {

        }
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    }
    public void startbutton(View v) {
        Intent intent = new Intent();
        intent.setClass(GuideActivity.this,Viewdoor.class);
        startActivity(intent);
        this.finish();
    }


}