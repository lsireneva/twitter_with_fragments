package com.example.luba.twitterwithfragments.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.UserInfo;
import com.example.luba.twitterwithfragments.adapters.ProfilePagerAdapter;
import com.example.luba.twitterwithfragments.models.User;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    public static final String USER = "USER";

    User mUser;
    TextView tvUserName, tvScreenName, tvFollowing, tvFollowers, tvDesciption;
    ImageView ivBackgroundPicture, ivProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupUI();

        //get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.profile_view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_profile_tabs);
        //set the adapter for the pager
        vpPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), this));
        //set up the TabLayout to use the view pager
        tabLayout.setupWithViewPager(vpPager);


    }

    public void setupUI () {
        mUser = UserInfo.getInstance().getUserInfo();
        tvUserName = findViewById(R.id.tv_user_name);
        tvScreenName = findViewById(R.id.tv_screen_name);
        ivBackgroundPicture = findViewById(R.id.iv_background);
        ivProfilePicture = findViewById(R.id.iv_profile_picture);
        tvFollowing = findViewById(R.id.tv_following);
        tvFollowers = findViewById(R.id.tv_followers);
        tvDesciption = findViewById(R.id.tv_description);

        tvUserName.setText(mUser.getName());
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
        tvFollowers.setText(String.valueOf(mUser.getFollowersCount()));

    }
}
