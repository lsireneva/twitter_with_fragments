package com.example.luba.twitterwithfragments.network.callbacks;

import com.example.luba.twitterwithfragments.models.Tweet;

/**
 * Created by luba on 10/9/17.
 */

public interface FavoriteTweetCallback {

    void onSuccess(Tweet tweet);

    void onError(Error error);
}
