package com.example.luba.twitterwithfragments.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by luba on 10/7/17.
 */

@Parcel
public class Media {

    @SerializedName("id")
    Long mediaId;

    @SerializedName("media_url")
    String mediaUrl;

    @SerializedName("url")
    String url;

    @SerializedName("type")
    String type;


    public Media() {

    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean typePhoto() {
        return type != null && "photo".equals(type);
    }
}
