package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.TwitterApplication;
import com.example.luba.twitterwithfragments.UserInfo;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.CheckNetwork;
import com.example.luba.twitterwithfragments.network.TwitterClient;
import com.example.luba.twitterwithfragments.network.callbacks.UserCredentialsCallback;

/**
 * Created by luba on 10/3/17.
 */

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {

        if (CheckNetwork.isOnline()) {
            TwitterClient mTwitterClient = TwitterApplication.getRestClient();
            Toast.makeText(LoginActivity.this, "Loading user credentials", Toast.LENGTH_SHORT).show();
            mTwitterClient.getUserCredentials(new UserCredentialsCallback() {
                @Override
                public void onSuccess(User user) {
                    if (user != null) {
                        UserInfo.getInstance().setUserInfo(user);
                    }
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }

            });

        } else {
            // Get profile from shared preferences

        }
        Intent i = new Intent(this, TwitterActivity.class);
        startActivity(i);
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        getClient().connect();
    }

}
