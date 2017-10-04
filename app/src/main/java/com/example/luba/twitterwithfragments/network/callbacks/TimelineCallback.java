package com.example.luba.twitterwithfragments.network.callbacks;

import com.example.luba.twitterwithfragments.models.Tweet;

import java.util.ArrayList;

/**
 * Created by luba on 10/3/17.
 */

public interface TimelineCallback {

    void onSuccess(ArrayList<Tweet> tweets);

    void onError(Error error);
}
