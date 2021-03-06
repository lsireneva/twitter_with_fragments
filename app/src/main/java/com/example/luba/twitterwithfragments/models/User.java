package com.example.luba.twitterwithfragments.models;

import com.example.luba.twitterwithfragments.MyDatabase;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

/**
 * Created by luba on 10/3/17.
 */

@Parcel(analyze={User.class})
@Table(database = MyDatabase.class, name = "User_Table")
public class User extends BaseModel {

    //list the attributes
    @SerializedName("name")
    @Column(name = "name")
    public String name;

    @SerializedName("id")
    @Column(name = "userId")
    @PrimaryKey
    public long userId;

    @SerializedName("screen_name")
    @Column(name = "screen_name")
    public String screenName;

    @SerializedName("profile_image_url")
    @Column(name = "profile_image_url")
    public String profileImageUrl;


    @SerializedName("profile_background_image_url")
    @Column(name = "profile_background_image_url")
    public String profileBackgroundImageUrl;

    @SerializedName("profile_banner_url")
    @Column(name = "profile_banner_url")
    String profileBannerUrl;


    @SerializedName("followers_count")
    @Column(name = "followers_count")
    int followersCount;

    @SerializedName("friends_count")
    @Column(name = "friends_count")
    int friendsCount;

    @SerializedName("description")
    @Column(name = "description")
    String description;

    @SerializedName("created_at")
    Date createdAt;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreennameToShow() {
        return "@" + screenName;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getprofileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setprofileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public void setProfileBannerUrl(String profileBannerUrl) {
        this.profileBannerUrl = profileBannerUrl;
    }

    public boolean hasProfileBackgroundImage() {
        return profileBackgroundImageUrl != null && !"".equals(profileBackgroundImageUrl);
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Record Finders
    public static User byUserId(Long userId) {
        return new Select().from(User.class).where(User_Table.userId.eq(userId)).querySingle();
    }

    public static List<User> recentItems() {
        return new Select().from(User.class).orderBy(User_Table.userId, false).limit(300).queryList();
    }

}

