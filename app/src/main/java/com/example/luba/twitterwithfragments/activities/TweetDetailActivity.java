package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.fragments.NewTweetDialogFragment;
import com.example.luba.twitterwithfragments.models.Media;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.CheckNetwork;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailActivity extends BaseActivity implements NewTweetDialogFragment.OnNewTweetDialogFragmentListener{

    public static final int REQUEST_CODE =1221;
    public static final String TWEET ="TWEET" ;
    public static final java.lang.String REFRESH_TWEETS ="refresh tweets" ;
    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;
    ImageView ivProfileImage, ivPhoto;
    TextView tvName, tvScreenname, tvStatus;
    Button btnReply;
    boolean refreshTweets;
    private Tweet mTweet = new Tweet();
    private User mUser = new User();
    RelativeLayout photoFrame;
    ProgressBar progressBar;



    @Override
    protected void setupBundle(Bundle extras) {
        //mTweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        mTweet = Parcels.unwrap(extras.getParcelable(TWEET));

    }

    @Override
    protected void setupUI() {
        setTitle(R.string.tweet);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenname = (TextView) findViewById(R.id.tvScreenname);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnReply = (Button) findViewById(R.id.btnReply);
        photoFrame = (RelativeLayout) findViewById(R.id.photo_frame);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);

        if (mTweet != null) {

            tvName.setText(mTweet.getUser().getName());
            tvScreenname.setText(mTweet.getUser().getScreennameToShow());
            Glide.with(this)
                    .load(mTweet.getUser().getProfileImageUrl())
                    .placeholder(R.drawable.ic_twitter)
                    .bitmapTransform(new RoundedCornersTransformation(ivProfileImage.getContext(), 3, 3))
                    .into(ivProfileImage);

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProfile(mTweet.getUser());
                }
            });
        }
        tvStatus.setText(mTweet.getText());
        photoFrame.setVisibility(View.GONE);

        if (mTweet.hasPhoto()) {
            photoFrame.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.VISIBLE);
            ivPhoto.setImageDrawable(null);

            Media photo = mTweet.getPhoto();
            Glide.with(this).load(photo.getMediaUrl())
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            ivPhoto.setImageDrawable(resource);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            Log.d("DEBUG", e.getMessage());
                        }
                    });
        }

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                NewTweetDialogFragment fragment = new NewTweetDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("tweet", Parcels.wrap(mTweet));
                fragment.setArguments(bundle);
                fragment.show(fm, "compose_tweet");
            }
        });

    }

    private void openProfile(User user) {

        if (!CheckNetwork.isOnline()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER, Parcels.wrap(user));
        startActivity(intent);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_tweet_detail;
    }

    @Override
    public void onTimeLineChanged(Tweet tweet) {
        refreshTweets = true;

    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(TWEET, Parcels.wrap(mTweet));
        Log.d("DEBUG", "mTweet"+mTweet);
        data.putExtra(REFRESH_TWEETS, refreshTweets);
        setResult(RESULT_OK, data);
        finish();
    }
}
