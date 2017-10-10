package com.example.luba.twitterwithfragments.utils;

import com.example.luba.twitterwithfragments.models.Tweet;

/**
 * Created by luba on 10/9/17.
 */

public class TweetEvent {

    private Tweet tweet;

    public TweetEvent(Tweet tweet) {
        this.tweet = tweet;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
