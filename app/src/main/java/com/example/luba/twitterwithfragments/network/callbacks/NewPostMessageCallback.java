package com.example.luba.twitterwithfragments.network.callbacks;

import com.example.luba.twitterwithfragments.models.Message;

/**
 * Created by luba on 10/7/17.
 */

public interface NewPostMessageCallback {

    void onSuccess(Message message);

    void onError(Error error);
}
