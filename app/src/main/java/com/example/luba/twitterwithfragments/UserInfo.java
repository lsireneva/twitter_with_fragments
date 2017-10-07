package com.example.luba.twitterwithfragments;

import com.example.luba.twitterwithfragments.models.User;

/**
 * Created by luba on 10/4/17.
 */

public class UserInfo {

    private static UserInfo instance;

    public static UserInfo getInstance() {
        if (null == instance) {
            instance = new UserInfo();
        }
        return instance;
    }

    private User mUser;

    private UserInfo() {

    }

    public User getUserInfo() {
        return mUser;
    }

    public void setUserInfo(User user) {
        this.mUser = user;
    }

}
