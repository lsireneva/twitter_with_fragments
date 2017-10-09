package com.example.luba.twitterwithfragments.network.callbacks;

/**
 * Created by luba on 10/9/17.
 */

public interface  UserFollowCallback {

    void onSuccess(UserFollowResponse response);

    void onError(Error error);
}
