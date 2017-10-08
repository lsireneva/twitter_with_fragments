package com.example.luba.twitterwithfragments.fragments;

import android.util.Log;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.network.TimelineRequest;
import com.example.luba.twitterwithfragments.network.callbacks.TimelineCallback;

import java.util.ArrayList;

/**
 * Created by luba on 10/7/17.
 */

public class UserFavoritesFragment extends TweetsListFragment {
    @Override
    protected void loadTweets(Long sinceID, Long maxID) {

        Toast.makeText(getContext(), "Loading user favorites", Toast.LENGTH_SHORT).show();
        TimelineRequest request = new TimelineRequest();
        request.setSinceId(sinceID);
        request.setMaxId(maxID);

        mTwitterClient.getUserFavorites(request, new TimelineCallback() {
            @Override
            public void onSuccess(ArrayList<Tweet> tweets) {
                processTweets(tweets);
                Log.d ("DEBUG", "processTweets(tweets);"+tweets);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                mSwipeToRefresh.setRefreshing(false);
            }
        });


    }
}
