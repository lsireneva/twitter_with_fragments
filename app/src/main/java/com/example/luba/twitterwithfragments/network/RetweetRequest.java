package com.example.luba.twitterwithfragments.network;

/**
 * Created by luba on 10/9/17.
 */

public class RetweetRequest {

    private Long tweetId;

    private boolean retweet;

    public RetweetRequest() {
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public boolean isRetweet() {
        return retweet;
    }

    public void setRetweet(boolean retweet) {
        this.retweet = retweet;
    }
}