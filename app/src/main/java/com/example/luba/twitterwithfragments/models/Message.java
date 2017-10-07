package com.example.luba.twitterwithfragments.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luba on 10/5/17.
 */

@Parcel
public class Message {

    @SerializedName("id")
    Long messageId;

    @SerializedName("created_at")
    Date createdAt;

    @SerializedName("recipient")
    User recipient;

    @SerializedName("sender")
    User sender;

    @SerializedName("text")
    String text;


    public Message() {

    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormattedCreatedAtDate() {
        if (getCreatedAt() != null) {
            return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(getCreatedAt());
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof Message) {
            Message message = (Message) object;
            return this.getMessageId().equals(message.getMessageId());
        }
        return false;
    }
}
