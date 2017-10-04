package com.example.luba.twitterwithfragments.network.callbacks;

import com.example.luba.twitterwithfragments.models.User;

/**
 * Created by luba on 10/3/17.
 */

public interface UserCredentialsCallback {

    void onSuccess(User user);

    void onError(Error error);
}