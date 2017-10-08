package com.example.luba.twitterwithfragments.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by luba on 10/7/17.
 */

@Parcel
public class Entities {

    @SerializedName("media")
    ArrayList<Media> media;

    public Entities() {

    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }



    /*public ArrayList<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }*/

    /* Helpers */


    public boolean hasPhoto() {
        if (media != null && media.size() > 0) {
            for (Media mediaItem : media) {
                if (mediaItem.typePhoto()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Media getPhoto() {
        if (media != null && media.size() > 0) {
            for (Media mediaItem : media) {
                if (mediaItem.typePhoto()) {
                    return mediaItem;
                }
            }
        }
        return null;
    }


}
