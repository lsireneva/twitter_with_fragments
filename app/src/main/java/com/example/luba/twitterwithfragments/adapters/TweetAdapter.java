package com.example.luba.twitterwithfragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.models.Media;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;

import java.util.ArrayList;

/**
 * Created by luba on 10/3/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Tweet> mTweets;
    Context context;

    private final int TWEET = 0, PHOTO_TWEET = 1, VIDEO_TWEET=3;

    public interface OnTweetAdapterListener {
        void selectedTweet(Tweet tweet);
        void replySelectedTweet(Tweet tweet);
        void selectedUser(User user);
    }


    private OnTweetAdapterListener mListener;


    //pas in the Tweets array in the constructor
    public TweetAdapter (ArrayList<Tweet> twets, OnTweetAdapterListener listener) {
        this.mTweets = twets;
        this.mListener = listener;
    }

    //for each row. inflate layout and cache references into ViewHolder

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent,false);
        TweetViewHolder viewHolder = new TweetViewHolder(tweetView);
        return viewHolder;*/

        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case TWEET:
                View viewPopularMovie = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
                viewHolder = new TweetViewHolder(viewPopularMovie);
                break;
            case PHOTO_TWEET:
                View viewLessPopularMovie = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_tweet, parent, false);
                viewHolder = new TweetPhotoViewHolder(viewLessPopularMovie);
                break;
            default:
                View viewLessPopularMovieDefault = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
                viewHolder = new TweetViewHolder(viewLessPopularMovieDefault);
                break;
        }
        return viewHolder;
    }

    //bind the values base on the position of the element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case TWEET:
                ((TweetViewHolder) holder).setupTweetView(mTweets.get(position));
                break;
            case PHOTO_TWEET:
                ((TweetPhotoViewHolder) holder).setupTweetView(mTweets.get(position));
                break;
        }

    }

    //create ViewHolder class
    @Override
    public int getItemCount() {
        return this.mTweets != null ? this.mTweets.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = mTweets.get(position);
        Log.d("DEBUG", "tweet"+tweet);
        //if (tweet.hasVideo()) {
          //  return VIDEO_TWEET;
        //} else
        if (tweet.hasPhoto()) {
            Log.d("DEBUG", "tweet.hasPhoto()"+tweet.hasPhoto());
            return PHOTO_TWEET;
        } else {
            return TWEET;
        }

    }

    public void notifyDataSetChanged(ArrayList<Tweet> tweets) {
        this.mTweets = new ArrayList<>(tweets);
        notifyDataSetChanged();
    }


    public class TweetViewHolder extends RecyclerView.ViewHolder{
        protected Tweet tweet;
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public  TextView tvName;
        public ImageView btnReply;

        public TweetViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvTimestamp);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnReply = (ImageView) itemView.findViewById(R.id.btnReply);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.selectedTweet(tweet);
                }

            });

            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.replySelectedTweet(tweet);
                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.selectedUser(tweet.getUser());
                }
            });


        }

        public void setupTweetView(Tweet tweet) {
            this.tweet = tweet;

            //populate the views according to these data
            tvUsername.setText(tweet.getUser().getName());
            tvName.setText(tweet.getUser().getScreennameToShow());
            tvBody.setText(tweet.getText());

            tvCreatedAt.setText(tweet.getRelativeTimeAgo());
            //Log.d("DEBUG", "createdAt"+tweet.getRelativeTimeAgo());

            Glide.with(ivProfileImage.getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);



        }
    }

    public class TweetPhotoViewHolder extends TweetViewHolder {

        ImageView ivPhoto;

        ProgressBar pbImage;

        public TweetPhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            pbImage = itemView.findViewById(R.id.pb_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.selectedTweet(tweet);
                }
            });
        }

        public void setupTweetView(Tweet tweet) {
            super.setupTweetView(tweet);

            pbImage.setVisibility(View.VISIBLE);
            ivPhoto.setImageDrawable(null);

            Media photo = tweet.getPhoto();
            //ivPhoto.setHeightRatio(((double) photo.getSize().getHeight()) / photo.getSize().getWidth());

            Glide.with(ivPhoto.getContext())
                    .load(photo.getMediaUrl())
                    .placeholder(R.drawable.ic_twitter)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            ivPhoto.setImageDrawable(resource);
                            pbImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Log.d("DEBUG", e.getMessage());
                        }
                    });
        }
    }
}

