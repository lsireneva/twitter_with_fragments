package com.example.luba.twitterwithfragments.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.TwitterApplication;
import com.example.luba.twitterwithfragments.network.TwitterClient;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;

    protected TwitterClient mTwitterClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceID());
        mTwitterClient = TwitterApplication.getRestClient();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setupBundle(extras);
        }
        
        setupUI();
        loadData();
    }

    protected abstract void setupBundle(Bundle extras);

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        setupToolbar();
    }

    protected abstract void setupUI();

    protected abstract void loadData();


    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setLogo(R.drawable.ic_twitter);
            setTitle("");
            if (getSupportActionBar() != null) {
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
            });
            appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        }
    }

    protected abstract int getLayoutResourceID();
}
