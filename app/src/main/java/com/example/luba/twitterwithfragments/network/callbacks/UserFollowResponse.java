package com.example.luba.twitterwithfragments.network.callbacks;

import com.example.luba.twitterwithfragments.models.User;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by luba on 10/9/17.
 */
@Parcel
public class UserFollowResponse {

    @SerializedName("previous_cursor_str")
    String previousCursor;

    @SerializedName("next_cursor_str")
    String nextCursor;

    @SerializedName("users")
    ArrayList<User> users;

    public UserFollowResponse() {

    }

    public String getPreviousCursor() {
        return previousCursor;
    }

    public void setPreviousCursor(String previousCursor) {
        this.previousCursor = previousCursor;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
