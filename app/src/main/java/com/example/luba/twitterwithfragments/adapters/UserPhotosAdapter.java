package com.example.luba.twitterwithfragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.models.Tweet;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by luba on 10/7/17.
 */

public class UserPhotosAdapter extends RecyclerView.Adapter<UserPhotosAdapter.UserPhotosViewHolder>  {

    protected ArrayList<Tweet> mTweets;

    public UserPhotosAdapter(ArrayList<Tweet> tweets) {
        this.mTweets = tweets;
    }

    @Override
    public UserPhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_photo, parent, false);
        return new UserPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserPhotosViewHolder holder, int position) {
        holder.setupViewWithPhotos(mTweets.get(position));
    }


    @Override
    public int getItemCount() {
        return this.mTweets != null ? this.mTweets.size() : 0;
    }


    public class UserPhotosViewHolder extends RecyclerView.ViewHolder {

        protected Tweet tweet;

        ImageView ivUserPhoto;


        public UserPhotosViewHolder(View itemView) {
            super(itemView);
            ivUserPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);

        }

        public void setupViewWithPhotos(Tweet tweet) {
            this.tweet = tweet;
            if (tweet.hasPhoto()) {

                Glide.with(ivUserPhoto.getContext())
                        .load(tweet.getEntities().getPhoto())
                        .placeholder(R.drawable.ic_twitter)
                        .bitmapTransform(new CropCircleTransformation(ivUserPhoto.getContext()))
                        .into(ivUserPhoto);
            }
        }
    }
}
