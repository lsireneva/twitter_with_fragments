package com.example.luba.twitterwithfragments.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.TwitterApplication;
import com.example.luba.twitterwithfragments.activities.ProfileActivity;
import com.example.luba.twitterwithfragments.activities.TweetDetailActivity;
import com.example.luba.twitterwithfragments.activities.TwitterActivity;
import com.example.luba.twitterwithfragments.adapters.TweetAdapter;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.CheckNetwork;
import com.example.luba.twitterwithfragments.network.FavoriteTweetRequest;
import com.example.luba.twitterwithfragments.network.RetweetRequest;
import com.example.luba.twitterwithfragments.network.TwitterClient;
import com.example.luba.twitterwithfragments.network.callbacks.FavoriteTweetCallback;
import com.example.luba.twitterwithfragments.network.callbacks.RetweetCallback;
import com.example.luba.twitterwithfragments.utils.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by luba on 10/3/17.
 */

public abstract class TweetsListFragment extends Fragment {

    public static final String USER = "USER";

    protected TwitterClient mTwitterClient;
    protected TweetAdapter tweetAdapter;
    protected ArrayList<Tweet> mTweets;
    RecyclerView rvTweets;
    private LinearLayoutManager mLayoutManager;
    public SwipeRefreshLayout mSwipeToRefresh;
    View viewFragment;
    DividerItemDecoration mDividerItemDecoration;
    private TwitterActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewFragment = inflater.inflate(getLayout(),container, false);
        mTwitterClient = TwitterApplication.getRestClient();

        if (getArguments() != null) {
            setupArguments(getArguments());
        }


        setupUI();
        showTweets();

        return viewFragment;

    }

    protected abstract void setupArguments(Bundle arguments);

    private void setupUI() {
        //find the RecyclerView
        rvTweets = (RecyclerView) viewFragment.findViewById(R.id.rvTweet);
        //init the ArrayList or data source
        mTweets = new ArrayList<>();
        //setup RecyclerView (layout manager, use adapter)
        mLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(mLayoutManager);
        rvTweets.setHasFixedSize(true);
        mDividerItemDecoration = new DividerItemDecoration(getContext(), mLayoutManager.getOrientation());
        rvTweets.addItemDecoration(mDividerItemDecoration);


        EndlessRecyclerViewScrollListener endlessListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (CheckNetwork.isOnline()) {
                    Log.d("DEBUG", "loading page=: " + String.valueOf(page));
                    Log.d ("DEBUG", "totalItemCount="+totalItemsCount);
                    Log.d ("DEBUG", "tmTweets.get(totalItemsCount - 1).getTweetId()="+mTweets.get(totalItemsCount - 1).getTweetId());
                    loadTweets(null, mTweets.get(totalItemsCount - 1).getTweetId());
                }
            }

        };
        rvTweets.addOnScrollListener(endlessListener);

        //construct the adapter from the data source
        tweetAdapter = new TweetAdapter(mTweets, new TweetAdapter.OnTweetAdapterListener() {
            @Override
            public void selectedTweet(Tweet tweet) {
                openTweetDetails(tweet);

            }

            @Override
            public void replySelectedTweet(Tweet tweet) {
                Log.d("DEBUG", "replyselectedtweet"+tweet);

                if (!CheckNetwork.isOnline()) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                NewTweetDialogFragment editNameDialogFragment = new NewTweetDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("tweet", Parcels.wrap(tweet));
                editNameDialogFragment.setArguments(bundle);
                editNameDialogFragment.show(fm, "compose_tweet");

            }

            @Override
            public void selectedUser(User user) {
                openProfile(user);
            }

            @Override
            public void selectedRetweet(final Tweet tweet, TextView tvRetweetCount, ImageView btnRetweet) {

                if (!CheckNetwork.isOnline()) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                final RetweetRequest request = new RetweetRequest();
                if (tweet.isRetweeted()) {
                    // Undo retweet
                    request.setRetweet(true);
                    request.setTweetId(tweet.getRetweetedStatus() != null ? tweet.getRetweetedStatus().getTweetId() : tweet.getTweetId());
                } else {
                    // Retweet
                    request.setRetweet(false);
                    request.setTweetId(tweet.getTweetId());
                }

                mTwitterClient.retweet(request, new RetweetCallback() {
                    @Override
                    public void onSuccess(Tweet tweet) {
                        // Weird bug ... this request does not return the favorites information
                        // Fix
                        int favoriteCount = tweet.getRetweetedStatus() != null ? tweet.getRetweetedStatus().getFavoriteCount() : tweet.getFavoriteCount();

                        if (request.isRetweet()) {
                            tweet.setRetweetCount(tweet.getRetweetCount() - 1);
                            tweet.setRetweetedStatus(null);
                            tweet.setRetweeted(false);
                        } else {
                            tweet.setRetweetCount(tweet.getRetweetCount() + 1);
                            tweet.setRetweetedStatus(tweet);
                            tweet.setRetweeted(true);
                        }
                        tweet.setFavoriteCount(favoriteCount);
                        if (tweet.getRetweetedStatus() != null)
                            tweet.getRetweetedStatus().setFavoriteCount(favoriteCount);
                        updateTweetInAdapter(tweet);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getActivity(), "Can't retweet", Toast.LENGTH_SHORT).show();
                        updateTweetInAdapter(tweet);
                    }
                });

            }

            @Override
            public void selectedAsFavorite(final Tweet tweet, final TextView tvFavoriteCount, final ImageView btnFavorite) {

                if (!CheckNetwork.isOnline()) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                final FavoriteTweetRequest request = new FavoriteTweetRequest(tweet.getTweetId(), tweet.isFavorite());

                mTwitterClient.markAsFavorite(request, new FavoriteTweetCallback() {
                    @Override
                    public void onSuccess(Tweet tweet) {
                        tweet.setFavorite(tweet.isFavorite());
                        tweet.setFavoriteCount(tweet.getFavoriteCount());
                        tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
                        btnFavorite.setBackground(getContext().getResources().getDrawable(R.drawable.ic_favorite_clicked));
                        if (tweet.getRetweetedStatus() != null) {
                            tweet.getRetweetedStatus().setFavoriteCount(tweet.getFavoriteCount());
                        }
                        updateTweetInAdapter(tweet);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getActivity(), "Can't do favorite", Toast.LENGTH_LONG).show();
                        updateTweetInAdapter(tweet);
                    }
                });

            }
        });
        //set adapter
        rvTweets.setAdapter(tweetAdapter);
        Log.d ("DEBUG", "mTweets"+mTweets);


        // Swipe to refresh
        mSwipeToRefresh = (SwipeRefreshLayout) viewFragment.findViewById(R.id.swipeContainer);
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mTweets.isEmpty()) {
                    loadTweets(mTweets.get(0).getTweetId(), null);
                    Log.d ("DEBUG", "!mTweets.isEmpty()"+mTweets.get(0).getTweetId());
                } else {
                    Log.d ("DEBUG", "empty");
                    loadTweets(null, null);
                }
            }
        });
        // Configure the refreshing colors
        mSwipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_blue_dark);
    }

    private void openProfile(User user) {
        if (!CheckNetwork.isOnline()) {
            Toast.makeText(getActivity(), "No internet conntection", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER, Parcels.wrap(user));
        startActivity(intent);
    }

    private void openTweetDetails(Tweet tweet) {
        if (!CheckNetwork.isOnline()) {
            Toast.makeText(getActivity(), "No internet conntection", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
        intent.putExtra(TweetDetailActivity.TWEET, Parcels.wrap(tweet));
        getActivity().startActivityForResult(intent, TweetDetailActivity.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d ("DEBUG", "onActivityResult()");
        // Check which request we're responding to
        if (requestCode == TweetDetailActivity.REQUEST_CODE) {
            Log.d ("DEBUG", "requestCode"+requestCode);
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Tweet tweet = Parcels.unwrap(data.getExtras().getParcelable(TweetDetailActivity.TWEET));
                if (tweet != null) updateTweetInAdapter(tweet);

                boolean refreshTweets = data.getExtras().getBoolean(TweetDetailActivity.REFRESH_TWEETS);
                if (refreshTweets) {
                    loadTweets(null, null);
                }
            }
        }
    }


    protected abstract void loadTweets(Long sinceID, Long maxID);

    protected void showTweets() {
        Log.d ("DEBUG", "showTweets()");
        if (!mTweets.isEmpty()) {
            tweetAdapter.notifyDataSetChanged(mTweets);
        } else {
            if (CheckNetwork.isOnline()) {
                loadTweets(null, null);
            } else {
                loadTweetsFromDB();
            }
        }
    }

    protected void processTweets(ArrayList<Tweet> tweets) {
        Log.d ("DEBUG", "processTweets()");
        if (tweets != null) {
            if (mTweets == null) {
                mTweets = new ArrayList<>();
            }
            for (Tweet tweet : tweets) {
                if (mTweets.contains(tweet)) {
                    mTweets.set(mTweets.indexOf(tweet), tweet);
                } else {
                    mTweets.add(tweet);
                }
            }
            Collections.sort(mTweets, new Comparator<Tweet>() {
                public int compare(Tweet t1, Tweet t2) {
                    return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                }
            });
        }
        //Log.d ("DEBUG", "mTweets"+mTweets);
        tweetAdapter.notifyDataSetChanged(mTweets);
        // Now we call setRefreshing(false) to signal refresh has finished
        mSwipeToRefresh.setRefreshing(false);
    }



    private void loadTweetsFromDB() {
        Toast.makeText(getContext(), "Loading tweets from DB", Toast.LENGTH_SHORT).show();

        processTweets(new ArrayList<>(Tweet.recentTweets()));

    }

    public void updateTweetInAdapter(Tweet tweet) {
        Log.d ("DEBUG", "updateTweetInAdapter()");
        if (mTweets != null) {
            if (mTweets.indexOf(tweet) != -1) {
                int index = mTweets.indexOf(tweet);
                mTweets.set(index, tweet);
                tweetAdapter.notifyItemChanged(index);
            }
            rvTweets.scrollToPosition(0);

        }
    }


    protected void saveTweetsToDB(ArrayList<Tweet> tweets) {
        if (!tweets.isEmpty() && tweets.size() > 0) {
            for (Tweet tweet : tweets) {
                // User table
                User tbUser = User.byUserId(tweet.getUser().getUserId());
                if (tbUser != null) {
                    // updated existing tweet
                    tbUser.setProfileImageUrl(tweet.getUser().getProfileImageUrl());
                    tbUser.setName(tweet.getUser().getName());
                    tbUser.setScreenName(tweet.getUser().getScreenName());
                    tbUser.save();
                    Log.d("DEBUG", "user updated: " + tweet.getUser().getUserId());
                } else {
                    // write new tweet
                    tbUser = tweet.getUser();
                    tbUser.save();
                    Log.d("DEBUG", "user inserted: " + tweet.getUser().getUserId());
                }
                tweet.setUser(tbUser);

                // Tweet table
                Tweet tbTweet = Tweet.byTweetId(tweet.getTweetId());
                if (tbTweet != null) {
                    // updated existing tweet
                    tbTweet.setText(tweet.getText());
                    tbTweet.setCreatedAt(tweet.getCreatedAt());
                    //tbTweet.setFavorite(tweet.isFavorite());
                    //tbTweet.setRetweeted(tweet.isRetweeted());
                    tbTweet.save();
                    Log.d("DEBUG", "tweet updated: " + tweet.getTweetId());
                } else {
                    // write new tweet
                    tbTweet = tweet;
                    tbTweet.save();
                    Log.d("DEBUG", "tweet inserted: " + tweet.getTweetId());
                }
            }
        }

    }

    public void insertTweetAtTop (Tweet tweet) {
        mTweets.add(0, tweet);
        tweetAdapter.notifyDataSetChanged(mTweets);
        mLayoutManager.scrollToPosition(0);

    }


    public int getLayout() {
        return R.layout.fragments_tweets_list;
    }
}


