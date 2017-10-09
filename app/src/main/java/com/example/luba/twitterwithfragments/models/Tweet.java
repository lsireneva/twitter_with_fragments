package com.example.luba.twitterwithfragments.models;

import android.text.format.DateUtils;

import com.example.luba.twitterwithfragments.MyDatabase;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by luba on 10/3/17.
 */

@Parcel(analyze={Tweet.class})
@Table(database = MyDatabase.class, name = "Tweet_Table")
public class Tweet extends BaseModel {

    @SerializedName("text")
    @Column(name = "text")
    String text;

    @SerializedName("id")
    @Column(name = "tweetId")
    @PrimaryKey
    Long tweetId;

    @SerializedName("user")
    @Column
    @ForeignKey(saveForeignKeyModel = false)
    public User user;


    @SerializedName("created_at")
    @Column(name = "created_at")
    Date createdAt;

    @SerializedName("entities")
    Entities entities;

    @SerializedName("favorited")
    @Column(name = "favorite")
    boolean favorite;

    @SerializedName("retweeted")
    @Column(name = "retweeted")
    boolean retweeted;

    @SerializedName("retweet_count")
    @Column(name = "retweet_count")
    int retweetCount;

    @SerializedName("retweeted_status")
    Tweet retweetedStatus;

    @SerializedName("favorite_count")
    @Column(name = "favorite_count")
    int favoriteCount;




    public Tweet() {
        super();
    }


    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getFormattedCreatedAtDate() {
        if (getCreatedAt() != null) {
            return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault()).format(getCreatedAt());
        }
        return null;
    }

    public String getRelativeTimeAgo() {
        long dateMillis = getCreatedAt().getTime();
        String new_relativeDate=null;
        String relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        if (relativeDate.contains("minutes ago")) {
            new_relativeDate = relativeDate.replace("minutes ago", "m");
        } else if (relativeDate.contains("hours ago")){
            new_relativeDate = relativeDate.replace("hours ago", "h");
        } else if (relativeDate.contains("hour ago")){
            new_relativeDate = relativeDate.replace("hour ago", "h");
        } else if (relativeDate.contains("seconds ago")){
            new_relativeDate = relativeDate.replace("seconds ago", "s");
        }

        return new_relativeDate;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public boolean hasPhoto() {
        return entities != null && entities.hasPhoto();
    }

    public Media getPhoto() {
        return entities.getPhoto();
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Tweet getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Tweet retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    // Record Finders
    public static Tweet byTweetId(Long tweetId) {
        return new Select().from(Tweet.class).where(Tweet_Table.tweetId.eq(tweetId)).querySingle();
    }

    public static List<Tweet> recentTweets() {
        return new Select().from(Tweet.class).orderBy(Tweet_Table.tweetId, false).limit(300).queryList();
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Tweet) {
            Tweet tweet = (Tweet) object;
            return this.getTweetId().equals(tweet.getTweetId());
        }
        return false;
    }



}
