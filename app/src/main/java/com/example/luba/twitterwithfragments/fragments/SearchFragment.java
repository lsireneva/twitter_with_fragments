package com.example.luba.twitterwithfragments.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.network.TimelineRequest;
import com.example.luba.twitterwithfragments.network.callbacks.TimelineCallback;

import java.util.ArrayList;

/**
 * Created by luba on 10/8/17.
 */

public class SearchFragment extends TweetsListFragment {

    public static final String QUERY = "QUERY";
    String query;

    @Override
    protected void setupArguments(Bundle arguments) {
        query = arguments.getString(QUERY);
    }

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void loadTweets(Long sinceID, Long maxID) {

        Toast.makeText(getActivity(), "Loading search result", Toast.LENGTH_SHORT).show();

        TimelineRequest request = new TimelineRequest();
        request.setSinceId(sinceID);
        request.setMaxId(maxID);
        request.setQuery(query);

        mTwitterClient.searchTweets(request, new TimelineCallback() {
            @Override
            public void onSuccess(ArrayList<Tweet> tweets) {
                processTweets(tweets);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                mSwipeToRefresh.setRefreshing(false);
            }
        });

    }
}
