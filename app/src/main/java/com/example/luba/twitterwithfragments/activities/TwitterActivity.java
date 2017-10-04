package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.adapters.TweetsPagerAdapter;

public class TwitterActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        setupUI();

    }

    private void setupUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setLogo(R.drawable.ic_twitter);
            /*if (getSupportActionBar() != null) {
                if (!isTaskRoot()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });*/
            appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        }

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
        //set up the TabLayout to use the view pager

        tabLayout.setupWithViewPager(vpPager);

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
}
