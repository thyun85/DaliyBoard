package com.thy.daliyboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alofo on 2018-03-21.
 */

public class Adapter_Main extends FragmentPagerAdapter {

    String[] titles = {"날씨", "팁", "리뷰", "장소", "My"};

    public Adapter_Main(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Fragment_Weather();
                break;

            case 1:
                fragment = new Fragment_Tips();
                break;

            case 2:
                fragment = new Fragment_Review();
                break;

            case 3:
                fragment = new Fragment_Spot();;
                break;

            case 4:
                fragment = new Fragment_MyPage();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
