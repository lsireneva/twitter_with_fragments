package com.example.luba.twitterwithfragments;

import android.app.Application;
import android.content.Context;

import com.example.luba.twitterwithfragments.network.TwitterClient;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by luba on 10/3/17.
 */

public class TwitterApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterApplication.context = this;
        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    public static TwitterClient getRestClient() {
        return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
    }
}