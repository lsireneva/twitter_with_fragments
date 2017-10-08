package com.example.luba.twitterwithfragments.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.TimelineRequest;
import com.example.luba.twitterwithfragments.network.callbacks.TimelineCallback;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by luba on 10/4/17.
 */

public class UserTimelineFragment extends TweetsListFragment {

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable(TweetsListFragment.USER, Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    User mUser;

    @Override
    protected void setupArguments(Bundle arguments) {
        mUser = Parcels.unwrap(arguments.getParcelable(TweetsListFragment.USER));
    }

    @Override
    protected void loadTweets(Long sinceID, Long maxID) {
        Toast.makeText(getContext(), "Loading user timeline", Toast.LENGTH_SHORT).show();

        TimelineRequest request = new TimelineRequest();
        request.setSinceId(sinceID);
        request.setMaxId(maxID);
        request.setUserId(mUser.getUserId());
        Log.d ("DEBUG", "mUserID="+mUser.getUserId());

        mTwitterClient.getUserTimeline(request, new TimelineCallback() {
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
