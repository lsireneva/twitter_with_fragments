package com.example.luba.twitterwithfragments.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.TwitterApplication;
import com.example.luba.twitterwithfragments.adapters.UserPhotosAdapter;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.network.CheckNetwork;
import com.example.luba.twitterwithfragments.network.TimelineRequest;
import com.example.luba.twitterwithfragments.network.TwitterClient;
import com.example.luba.twitterwithfragments.network.callbacks.TimelineCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by luba on 10/7/17.
 */

public class UserPhotosFragment extends android.support.v4.app.Fragment {

    protected TwitterClient mTwitterClient;
    private RecyclerView rvUserPhotos;
    View viewFragment;
    protected ArrayList<Tweet> mTweets;
    StaggeredGridLayoutManager mLayoutManager;
    UserPhotosAdapter mAdapter;

    public UserPhotosFragment () {}

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewFragment = inflater.inflate(R.layout.fragment_user_photos,container, false);

        mTwitterClient = TwitterApplication.getRestClient();

        setupUI();
        showTweets();

        return viewFragment;
    }

    private void showTweets() {
        Log.d ("DEBUG", "showTweets()");
        if (!mTweets.isEmpty()) {
            mAdapter.notifyDataSetChanged();
        } else {
            if (CheckNetwork.isOnline()) {
                loadTweets(null, null);
            } else {
                //loadTweetsFromDB();
            }
        }
    }

    protected void processTweets(ArrayList<Tweet> tweets) {
        Log.d ("DEBUG", "processTweets()");
        if (tweets != null) {
            if (mTweets == null) {
                mTweets = new ArrayList<>();
            }
            for (Tweet tweet : tweets) {
                if (mTweets.contains(tweet)) {
                    mTweets.set(mTweets.indexOf(tweet), tweet);
                } else {
                    mTweets.add(tweet);
                }
            }
            Collections.sort(mTweets, new Comparator<Tweet>() {
                public int compare(Tweet t1, Tweet t2) {
                    return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                }
            });
        }
        //Log.d ("DEBUG", "mTweets"+mTweets);
        mAdapter.notifyDataSetChanged();
        // Now we call setRefreshing(false) to signal refresh has finished
        //mSwipeToRefresh.setRefreshing(false);
    }

    protected void loadTweets(Long sinceID, Long maxID) {

        Toast.makeText(getContext(), "Loading tweets", Toast.LENGTH_SHORT).show();
        TimelineRequest request = new TimelineRequest();
        request.setSinceId(sinceID);
        request.setMaxId(maxID);

        mTwitterClient.getUserTimeline(request, new TimelineCallback() {
            @Override
            public void onSuccess(ArrayList<Tweet> tweets) {
                // Save in database
                //saveTweetsToDB(tweets);
                processTweets(tweets);
                Log.d ("DEBUG", "processTweets(tweets);"+tweets);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //mSwipeToRefresh.setRefreshing(false);
            }
        });

    }


    private void setupUI() {
        rvUserPhotos = (RecyclerView) viewFragment.findViewById(R.id.rvUserPhotos);
        //init the ArrayList or data source
        mTweets = new ArrayList<>();

        rvUserPhotos.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvUserPhotos.setLayoutManager(mLayoutManager);

        mAdapter = new UserPhotosAdapter(mTweets);
        rvUserPhotos.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
