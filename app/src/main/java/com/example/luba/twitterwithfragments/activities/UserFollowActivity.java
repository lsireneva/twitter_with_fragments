package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.adapters.UserFollowAdapter;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.CheckNetwork;
import com.example.luba.twitterwithfragments.network.UserFollowRequest;
import com.example.luba.twitterwithfragments.network.callbacks.UserFollowCallback;
import com.example.luba.twitterwithfragments.network.callbacks.UserFollowResponse;
import com.example.luba.twitterwithfragments.utils.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;

public class UserFollowActivity extends BaseActivity implements UserFollowAdapter.OnUserFollowAdapterListener{

    public static final String FOLLOW = "FOLLOW";
    public static final String USER = "USER";

    public enum Follow {
        FOLLOWING,
        FOLLOWERS
    }

    User mUser;
    Follow follow;
    RecyclerView rvUserFollow;
    SwipeRefreshLayout mSwipeToRefresh;
    UserFollowAdapter mAdapter;
    private ArrayList<User> mUsers;
    private LinearLayoutManager mLayoutManager;
    DividerItemDecoration mDividerItemDecoration;
    private String mNextCursor;

    @Override
    protected void setupBundle(Bundle extras) {
        follow = (Follow) extras.getSerializable(FOLLOW);
        mUser = Parcels.unwrap(extras.getParcelable(USER));

    }

    @Override
    protected void setupUI() {

        rvUserFollow = (RecyclerView) findViewById(R.id.rv_user_follow);
        mSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        setTitle(follow == Follow.FOLLOWERS  ? "Followers  "+mUser.getScreennameToShow() : "Following "+mUser.getScreennameToShow());

        rvUserFollow.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvUserFollow.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(this, mLayoutManager.getOrientation());
        rvUserFollow.addItemDecoration(mDividerItemDecoration);



        EndlessRecyclerViewScrollListener endlessListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG", "loadNextPage: " + String.valueOf(page));
                loadUserFollow();
            }
        };
        rvUserFollow.addOnScrollListener(endlessListener);

        mAdapter = new UserFollowAdapter(mUsers, new UserFollowAdapter.OnUserFollowAdapterListener() {
            @Override
            public void selectedUser(User user) {
                openProfile(user);
            }
        });
        rvUserFollow.setAdapter(mAdapter);

        // Swipe to refresh
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserFollow();
            }
        });
        mSwipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_blue_dark);

    }

    private void openProfile(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER, Parcels.wrap(user));
        startActivity(intent);
    }

    @Override
    protected void loadData() {

        if (mUsers != null) {
            mAdapter.notifyDataSetChanged(mUsers);
        } else {
            if (CheckNetwork.isOnline()) {
                loadUserFollow();
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }

    }

    private void loadUserFollow() {

        UserFollowRequest request = new UserFollowRequest();
        request.setUserId(mUser.getUserId());
        request.setCursor(mNextCursor);

        if (follow == Follow.FOLLOWERS) {
            mTwitterClient.getFollowers(request, new UserFollowCallback() {
                @Override
                public void onSuccess(UserFollowResponse response) {
                    processResponse(response);
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(UserFollowActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            mTwitterClient.getFollowing(request, new UserFollowCallback() {
                @Override
                public void onSuccess(UserFollowResponse response) {
                    processResponse(response);
                }

                @Override
                public void onError(Error error) {
                    Toast.makeText(UserFollowActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void processResponse(UserFollowResponse twitterUsersResponse) {
        mNextCursor = twitterUsersResponse.getNextCursor();

        ArrayList<User> users = twitterUsersResponse.getUsers();
        if (users != null) {
            if (mUsers == null) {
                mUsers = new ArrayList<>();
            }
            for (User user : users) {
                if (mUsers.contains(user)) {
                    mUsers.set(mUsers.indexOf(user), user);
                } else {
                    mUsers.add(user);
                }
            }
        }
        mAdapter.notifyDataSetChanged(mUsers);

        mSwipeToRefresh.setRefreshing(false);
    }


    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_user_follow;
    }

    @Override
    public void selectedUser(User user) {

    }
}
