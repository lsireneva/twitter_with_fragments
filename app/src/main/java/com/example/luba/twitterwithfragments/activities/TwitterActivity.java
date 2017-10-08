package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.adapters.TweetsPagerAdapter;
import com.example.luba.twitterwithfragments.fragments.HomeTimeLineFragment;
import com.example.luba.twitterwithfragments.fragments.NewTweetDialogFragment;
import com.example.luba.twitterwithfragments.models.Tweet;

public class TwitterActivity extends BaseActivity implements NewTweetDialogFragment.OnNewTweetDialogFragmentListener {

    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;
    LinearLayout tabsContainer;
    FloatingActionButton fabCompose;
    TweetsPagerAdapter adapterViewPager;
    ViewPager vpPager;


    @Override
    protected void setupUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tabsContainer = findViewById(R.id.tabs_container);
        fabCompose = findViewById(R.id.fabCompose);
        //get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        //set the adapter for the pager
        vpPager.setAdapter(adapterViewPager);
        //set up the TabLayout to use the view pager
        tabLayout.setupWithViewPager(vpPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fabCompose.setVisibility(position == 1 ? View.GONE : View.VISIBLE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "fabCompose.setOnClickListener");
                FragmentManager fm = getSupportFragmentManager();
                NewTweetDialogFragment fragment = new NewTweetDialogFragment();
                fragment.show(fm, "compose_tweet");
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_twitter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        //launch the profile view
        Intent intent = new Intent (this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onMessagesView(MenuItem item) {
        //launch the profile view
        Intent intent = new Intent (this, MessagesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTimeLineChanged(Tweet tweet) {
        Log.d ("DEBUG", "onTimeLineChanged"+ tweet);
        HomeTimeLineFragment fragment = (HomeTimeLineFragment) adapterViewPager.getRegisteredFragment(0);
        fragment.insertTweetAtTop(tweet);
    }

    public void onSearch(MenuItem item) {
    }
}
