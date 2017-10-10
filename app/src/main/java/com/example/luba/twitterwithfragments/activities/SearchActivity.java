package com.example.luba.twitterwithfragments.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.fragments.NewTweetDialogFragment;
import com.example.luba.twitterwithfragments.fragments.SearchFragment;
import com.example.luba.twitterwithfragments.models.Tweet;

/**
 * Created by luba on 10/8/17.
 */

public class SearchActivity extends BaseActivity implements NewTweetDialogFragment.OnNewTweetDialogFragmentListener {

    public static final String QUERY = "QUERY";
    String query;
    FrameLayout fragmentContainer;

    @Override
    protected void setupBundle(Bundle extras) {
        query = extras.getString(QUERY);

    }

    @Override
    protected void setupUI() {
        setTitle(query);
        fragmentContainer = findViewById(R.id.fragment_container);
    }

    @Override
    protected void loadData() {

        SearchFragment fragment = SearchFragment.newInstance(query);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_search;
    }

    @Override
    public void onTimeLineChanged(Tweet tweet) {

    }
}
