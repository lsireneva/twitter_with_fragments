package com.example.luba.twitterwithfragments.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.UserInfo;
import com.example.luba.twitterwithfragments.adapters.TweetsPagerAdapter;
import com.example.luba.twitterwithfragments.fragments.HomeTimeLineFragment;
import com.example.luba.twitterwithfragments.fragments.NewTweetDialogFragment;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;

import org.parceler.Parcels;

public class TwitterActivity extends BaseActivity implements NewTweetDialogFragment.OnNewTweetDialogFragmentListener {

    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;
    LinearLayout tabsContainer;
    FloatingActionButton fabCompose;
    TweetsPagerAdapter adapterViewPager;
    ViewPager vpPager;
    String mTextFilter;
    boolean mFirstLoad;



    @Override
    protected void setupBundle(Bundle extras) {

    }

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
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchView.clearFocus();
                Log.i("DEBUG","query"+query);
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(SearchActivity.QUERY, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem item) {
        User user = UserInfo.getInstance().getUserInfo();
        Intent intent = new Intent (this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER, Parcels.wrap(user));
        startActivity(intent);
    }

    public void onMessagesView(MenuItem item) {
        Intent intent = new Intent (this, MessagesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTimeLineChanged(Tweet tweet) {
        Log.d ("DEBUG", "onTimeLineChanged"+ tweet);
        HomeTimeLineFragment fragmentHomeTimeLine = (HomeTimeLineFragment) adapterViewPager.getRegisteredFragment(0);
        fragmentHomeTimeLine.insertTweetAtTop(tweet);
    }

}
