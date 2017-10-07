package com.example.luba.twitterwithfragments.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luba.twitterwithfragments.fragments.UserTimelineFragment;

/**
 * Created by luba on 10/4/17.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"TWEETS", "PHOTOS", "FAVORITES"};

    private Context context;

    public ProfilePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserTimelineFragment();
            case 1:
                return new UserTimelineFragment();
            case 2:
                return new UserTimelineFragment();
            default:
                return new UserTimelineFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
