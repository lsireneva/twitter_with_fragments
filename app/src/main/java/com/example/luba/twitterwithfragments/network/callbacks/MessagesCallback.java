package com.example.luba.twitterwithfragments.network.callbacks;

import com.example.luba.twitterwithfragments.models.Message;

import java.util.ArrayList;

/**
 * Created by luba on 10/6/17.
 */

public interface MessagesCallback {

    void onSuccess(ArrayList<Message> messages);

    void onError(Error error);
}
