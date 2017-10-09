package com.example.luba.twitterwithfragments.network;

/**
 * Created by luba on 10/9/17.
 */

public class UserFollowRequest {

    private Long userId;

    private String cursor;

    public UserFollowRequest() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
