package com.thy.daliyboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alofo on 2018-03-21.
 */

public class MainAdapter extends FragmentPagerAdapter {

    String[] titles = {"Boards", "Skills", "스팟", "Show us"};

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Fragment_KindOfBoards();
                break;

            case 1:
                fragment = new Fragment_KindOfSkills();
                break;

            case 2:
                fragment = new Fragment_Spot();
                break;

            case 3:
                fragment = new Fragment_Howto();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
