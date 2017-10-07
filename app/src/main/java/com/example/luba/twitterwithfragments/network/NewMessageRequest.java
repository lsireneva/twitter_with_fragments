package com.example.luba.twitterwithfragments.network;

/**
 * Created by luba on 10/7/17.
 */

public class NewMessageRequest {

    private String text;
    private String screenName;

    public String getTextOfMessage() {
        return text;
    }

    public void setTextOfMessage(String text) {
        this.text = text;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
