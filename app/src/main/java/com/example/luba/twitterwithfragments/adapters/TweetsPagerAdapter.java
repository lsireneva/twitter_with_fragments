package com.example.luba.twitterwithfragments.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luba.twitterwithfragments.fragments.HomeTimeLineFragment;
import com.example.luba.twitterwithfragments.fragments.MentionsTimeLineFragment;

/**
 * Created by luba on 10/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;

    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //return total number of fragments

    @Override
    public int getCount() {
        return 2;
    }

    //return fragment to use depending to the position

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new HomeTimeLineFragment();
        } else if (position==1){
            return new MentionsTimeLineFragment();
        } else {
            return null;
        }
    }

    //return fragment title

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
