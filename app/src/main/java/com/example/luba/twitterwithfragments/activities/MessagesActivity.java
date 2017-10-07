package com.example.luba.twitterwithfragments.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.adapters.MessagesAdapter;
import com.example.luba.twitterwithfragments.fragments.NewMessageDialogFragment;
import com.example.luba.twitterwithfragments.models.Message;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.CheckNetwork;
import com.example.luba.twitterwithfragments.network.TimelineRequest;
import com.example.luba.twitterwithfragments.network.callbacks.MessagesCallback;
import com.example.luba.twitterwithfragments.utils.EndlessRecyclerViewScrollListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MessagesActivity extends BaseActivity implements MessagesAdapter.OnMessagesAdapterListener, NewMessageDialogFragment.OnNewMessageDialogFragmentListener {


    public SwipeRefreshLayout mSwipeToRefresh;
    RecyclerView rvMessages;

    MessagesAdapter mAdapter;
    private ArrayList<Message> mMessages;
    private LinearLayoutManager mLayoutManager;
    DividerItemDecoration mDividerItemDecoration;
    FloatingActionButton fabNewMessage;


    @Override
    protected void setupUI() {
        setTitle(getString(R.string.title_messages));
        rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        mSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        fabNewMessage = (FloatingActionButton) findViewById(R.id.fabNewMessage);
        rvMessages.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(this, mLayoutManager.getOrientation());
        rvMessages.addItemDecoration(mDividerItemDecoration);


        EndlessRecyclerViewScrollListener endlessListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG", "loadNextPage Messages: " + String.valueOf(page));
                Log.d ("DEBUG", "totalItemCount="+totalItemsCount);
                Log.d ("DEBUG", "tmTweets.get(totalItemsCount - 1).getTweetId()="+mMessages.get(totalItemsCount - 1).getMessageId());
                loadMessages(null, mMessages.get(totalItemsCount - 1).getMessageId());
            }
        };
        rvMessages.addOnScrollListener(endlessListener);

        mAdapter = new MessagesAdapter(mMessages, new MessagesAdapter.OnMessagesAdapterListener() {
            @Override
            public void selectedUser(User user) {
                goToProfile(user);
            }
        });
        rvMessages.setAdapter(mAdapter);
        
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mMessages != null) {
                    loadMessages(mMessages.get(0).getMessageId(), null);
                } else {
                    loadMessages(null, null);
                }
            }
        });
        mSwipeToRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_blue_dark);

        fabNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d ("DEBUG", "fabCompose.setOnClickListener");
                FragmentManager fm = getSupportFragmentManager();
                NewMessageDialogFragment fragment = new NewMessageDialogFragment();
                fragment.show(fm, "compose_message");
            }
        });
    }


    @Override
    protected void loadData() {

        if (mMessages != null) {
            mAdapter.notifyDataSetChanged(mMessages);
        } else {
            if (CheckNetwork.isOnline()) {
                loadMessages(null, null);
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.activity_messages;
    }

    private void loadMessages(Long sinceMessageId, Long maxMessageId) {
        Toast.makeText(this, "Loading messages...", Toast.LENGTH_SHORT).show();

        TimelineRequest request = new TimelineRequest();
        request.setSinceId(sinceMessageId);
        request.setMaxId(maxMessageId);

        mTwitterClient.getMessages(request, new MessagesCallback() {
            @Override
            public void onSuccess(ArrayList<Message> messages) {
                // Process messages
                processMessages(messages);
            }

            @Override
            public void onError(Error error) {
                //Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                mSwipeToRefresh.setRefreshing(false);
            }
        });
    }

    private void processMessages(ArrayList<Message> messages) {
        if (messages != null) {
            if (mMessages == null) {
                mMessages = new ArrayList<>();
            }
            for (Message message : messages) {
                if (mMessages.contains(message)) {
                    mMessages.set(mMessages.indexOf(message), message);
                } else {
                    mMessages.add(message);
                }
            }
            Collections.sort(mMessages, new Comparator<Message>() {
                public int compare(Message t1, Message t2) {
                    return t2.getCreatedAt().compareTo(t1.getCreatedAt());
                }
            });
        }
        mAdapter.notifyDataSetChanged(mMessages);
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void selectedUser(User user) {
        goToProfile(user);

    }

    private void goToProfile(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USER, Parcels.wrap(user));
        startActivityForResult(intent, TweetDetailActivity.REQUEST_CODE);
    }


    @Override
    public void onMessageListChanged(Message message) {
        mMessages.add(0, message);
        mAdapter.notifyDataSetChanged(mMessages);
        mLayoutManager.scrollToPosition(0);
    }
}
