package com.example.luba.twitterwithfragments.network;

/**
 * Created by luba on 10/9/17.
 */

public class FavoriteTweetRequest {

    private Long tweetId;

    private boolean favorite;

    public FavoriteTweetRequest() {

    }

    public FavoriteTweetRequest(Long tweetId, boolean favorite) {
        this.tweetId = tweetId;
        this.favorite = favorite;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
