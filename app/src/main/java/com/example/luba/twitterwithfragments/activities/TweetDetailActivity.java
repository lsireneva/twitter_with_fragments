package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.fragments.NewTweetDialogFragment;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;

import org.parceler.Parcels;

public class TweetDetailActivity extends BaseActivity implements NewTweetDialogFragment.OnNewTweetDialogFragmentListener{

    public static final int REQUEST_CODE =6565;
    public static final String TWEET ="tweet" ;
    public static final java.lang.String REFRESH_TWEETS ="refresh tweets" ;
    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;
    ImageView ivProfileImage;
    TextView tvName, tvScreenname, tvStatus;
    Button btnReply;
    boolean refreshTweets;
    private Tweet mTweet = new Tweet();
    private User mUser = new User();


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

        if (mTweet != null) {

            tvName.setText(mTweet.getUser().getName());
            tvScreenname.setText(mTweet.getUser().getScreennameToShow());
            Glide.with(this).load(mTweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        }
        tvStatus.setText(mTweet.getText());

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                NewTweetDialogFragment fragment = new NewTweetDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", Parcels.wrap(mUser));
                bundle.putParcelable("tweet", Parcels.wrap(mTweet));
                fragment.setArguments(bundle);
                fragment.show(fm, "compose_tweet");
            }
        });

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
        data.putExtra(REFRESH_TWEETS, refreshTweets);
        setResult(RESULT_OK, data);
        finish();
    }
}
