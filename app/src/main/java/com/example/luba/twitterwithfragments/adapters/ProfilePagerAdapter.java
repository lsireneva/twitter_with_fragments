package com.example.luba.twitterwithfragments.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luba.twitterwithfragments.fragments.UserFavoritesFragment;
import com.example.luba.twitterwithfragments.fragments.UserTimelineFragment;
import com.example.luba.twitterwithfragments.models.User;

/**
 * Created by luba on 10/4/17.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_ITEMS = 2;
    private String tabTitles[] = new String[] {"TWEETS", "FAVORITES"};

    private Context context;
    private User user;

    public ProfilePagerAdapter(FragmentManager fm, Context context, User user) {
        super(fm);
        this.context = context;
        this.user = user;
    }


    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserTimelineFragment.newInstance(user);
            case 1:
                return UserFavoritesFragment.newInstance(user);
            default:
                return UserTimelineFragment.newInstance(user);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
