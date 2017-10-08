package com.example.luba.twitterwithfragments.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.adapters.ProfilePagerAdapter;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.callbacks.UserCredentialsCallback;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends BaseActivity {

    public static final String USER = "USER";

    User mUser;
    TextView tvUserName, tvScreenName, tvFollowing, tvFollowers, tvDesciption;
    ImageView ivBackgroundPicture, ivProfilePicture;

    @Override
    protected void setupBundle(Bundle extras) {
        mUser = Parcels.unwrap(extras.getParcelable(USER));

    }

    @Override
    protected void setupUI() {

        //mUser = UserInfo.getInstance().getUserInfo();
        tvUserName = findViewById(R.id.tv_user_name);
        tvScreenName = findViewById(R.id.tv_screen_name);
        ivBackgroundPicture = findViewById(R.id.iv_background);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        tvFollowing = findViewById(R.id.tv_following);
        tvFollowers = findViewById(R.id.tv_followers);
        tvDesciption = findViewById(R.id.tv_description);

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.profile_view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_profile_tabs);
        //set the adapter for the pager
        vpPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), this, mUser));
        //set up the TabLayout to use the view pager
        tabLayout.setupWithViewPager(vpPager);


        /*tvUserName.setText(mUser.getName());
        tvScreenName.setText(mUser.getScreennameToShow());
        tvDesciption.setText(mUser.getDescription());

        if (mUser.hasProfileBackgroundImage()) {
            Glide.with(this).load(mUser.getProfileBannerUrl()).centerCrop().into(ivBackgroundPicture);
        } else {
            ivBackgroundPicture.setBackgroundColor(getResources().getColor(R.color.blue));
        }


        Glide.with(this)
                .load(mUser.getProfileImageUrl())
                .placeholder(R.drawable.ic_twitter)
                .bitmapTransform(new RoundedCornersTransformation(this, 3, 3))
                .into(ivProfilePicture);

        tvFollowing.setText(String.valueOf(mUser.getFriendsCount()));
        tvFollowers.setText(String.valueOf(mUser.getFollowersCount()));*/


    }


    @Override
    protected void loadData() {
        setTitle(mUser.getName());
        //getSupportActionBar().setTitle(mUser.getName());
        //getSupportActionBar().setSubtitle(mUser.getName());

        if (mUser.getCreatedAt() != null) {
            showUserInfo();
        } else {
            Log.d("DEBUG", "profile is incomplete, request it");
            mTwitterClient.getUserProfile(mUser.getUserId(), new UserCredentialsCallback() {
                @Override
                public void onSuccess(User user) {
                    mUser = user;
                    showUserInfo();
                }

                @Override
                public void onError(Error error) {

                }
            });
        }

    }

    private void showUserInfo() {
        /*if (mUser.equals(UserInfo.getInstance().getUserInfo())
                || mUser.isFollowRequestSent()) {
            btFollowUnfollow.setVisibility(View.GONE);
        } else {
            updateFollowUnfollow();
        }*/

        if (mUser.hasProfileBackgroundImage()) {
            Glide.with(this)
                    .load(mUser.getProfileBannerUrl())
                    .centerCrop()
                    .into(ivBackgroundPicture);
        }

        Glide.with(this)
                .load(mUser.getProfileImageUrl())
                .placeholder(R.drawable.ic_twitter)
                .bitmapTransform(new RoundedCornersTransformation(this, 3, 3))
                .into(ivProfilePicture);

        if (mUser.getDescription() != null && !"".equals(mUser.getDescription())) {
            tvDesciption.setVisibility(View.VISIBLE);
            tvDesciption.setText(mUser.getDescription());
        } else {
            tvDesciption.setVisibility(View.GONE);
        }

        tvFollowing.setText(String.valueOf(mUser.getFriendsCount()));
        tvFollowers.setText(String.valueOf(mUser.getFollowersCount()));
    }


    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_profile;
    }
}
