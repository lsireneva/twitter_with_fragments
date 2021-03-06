package com.example.luba.twitterwithfragments.network;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.models.Message;
import com.example.luba.twitterwithfragments.models.Tweet;
import com.example.luba.twitterwithfragments.models.User;
import com.example.luba.twitterwithfragments.network.callbacks.FavoriteTweetCallback;
import com.example.luba.twitterwithfragments.network.callbacks.MessagesCallback;
import com.example.luba.twitterwithfragments.network.callbacks.NewPostMessageCallback;
import com.example.luba.twitterwithfragments.network.callbacks.NewPostTweetCallback;
import com.example.luba.twitterwithfragments.network.callbacks.RetweetCallback;
import com.example.luba.twitterwithfragments.network.callbacks.TimelineCallback;
import com.example.luba.twitterwithfragments.network.callbacks.UserCredentialsCallback;
import com.example.luba.twitterwithfragments.network.callbacks.UserFollowCallback;
import com.example.luba.twitterwithfragments.network.callbacks.UserFollowResponse;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by luba on 10/3/17.
 */

public class TwitterClient extends OAuthBaseClient {
    public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "JR80nkUQHbor7tcMGFN60M3Jw";       // Change this
    public static final String REST_CONSUMER_SECRET = "2KcTCzXpsnnBbekPcLtqx9PQaZeJvl5PfQHzEWKNHhFFsQg1PZ"; // Change this

    // Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    // See https://developer.chrome.com/multidevice/android/intents
    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public TwitterClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }

    public void getHomeTimeline(TimelineRequest request, final TimelineCallback callback) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            params.put("count", request.getCount());
            if (request.getSinceId() != null) {
                params.put("since_id", request.getSinceId());
            }
            if (request.getMaxId() != null) {
                params.put("max_id", request.getMaxId());
            }
        }
        Log.d ("DEBUG", "params"+params);

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                ArrayList<Tweet> tweets = gson.fromJson(responseString,
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());
                Log.d ("DEBUG", "tweets"+tweets);
                callback.onSuccess(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void getMentionsTimeline(TimelineRequest request, final TimelineCallback callback) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getSinceId() != null) {
                params.put("since_id", request.getSinceId());
            }
            if (request.getMaxId() != null) {
                params.put("max_id", request.getMaxId());
            }
        }
        Log.d ("DEBUG", "params"+params);

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                ArrayList<Tweet> tweets = gson.fromJson(responseString,
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());
                Log.d ("DEBUG", "tweets"+tweets);
                callback.onSuccess(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void getUserCredentials(final UserCredentialsCallback callback) {
        String apiUrl = getApiUrl("account/verify_credentials.json");

        RequestParams params = new RequestParams();

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                User user = gson.fromJson(responseString, User.class);
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });

    }

    public void getUserProfile(Long userId, final UserCredentialsCallback callback) {
        String apiUrl = getApiUrl("users/show.json");

        RequestParams params = new RequestParams();
        params.put("user_id", userId);

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                User user = gson.fromJson(responseString, User.class);
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void getUserTimeline(TimelineRequest request,
                                final TimelineCallback callback) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getSinceId() != null) {
                params.put("since_id", request.getSinceId());
            }
            if (request.getMaxId() != null) {
                params.put("max_id", request.getMaxId());
            }
            if (request.getUserId() != null) {
                params.put("user_id", request.getUserId());
            }
        }

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                ArrayList<Tweet> tweets = gson.fromJson(responseString,
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());
                callback.onSuccess(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void getUserFavorites(TimelineRequest request,
                                final TimelineCallback callback) {
        String apiUrl = getApiUrl("favorites/list.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getSinceId() != null) {
                params.put("since_id", request.getSinceId());
            }
            if (request.getMaxId() != null) {
                params.put("max_id", request.getMaxId());
            }
            if (request.getUserId() != null) {
                params.put("user_id", request.getUserId());
            }
        }

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                ArrayList<Tweet> tweets = gson.fromJson(responseString,
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());
                callback.onSuccess(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }


    public void getMessages(TimelineRequest request, final MessagesCallback callback) {
        String apiUrl = getApiUrl("direct_messages.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            params.put("count", request.getCount());
            if (request.getSinceId() != null) {
                params.put("since_id", request.getSinceId());
            }
            if (request.getMaxId() != null) {
                params.put("max_id", request.getMaxId());
            }
        }

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                ArrayList<Message> messages = gson.fromJson(responseString,
                        new TypeToken<ArrayList<Message>>() {
                        }.getType());
                callback.onSuccess(messages);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void searchTweets(TimelineRequest request,
                             final TimelineCallback callback) {
        String apiUrl = getApiUrl("search/tweets.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getSinceId() != null) {
                params.put("since_id", request.getSinceId());
            }
            if (request.getMaxId() != null) {
                params.put("max_id", request.getMaxId());
            }
            if (request.getQuery() != null) {
                params.put("q", request.getQuery());
            }
        }

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                JsonObject jsonObject = new Gson().fromJson(responseString, JsonObject.class);
                ArrayList<Tweet> tweets = gson.fromJson(jsonObject.get("statuses").getAsJsonArray(),
                        new TypeToken<ArrayList<Tweet>>() {
                        }.getType());
                callback.onSuccess(tweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void getFollowing(UserFollowRequest request, final UserFollowCallback callback) {
        String apiUrl = getApiUrl("friends/list.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getUserId() != null) {
                params.put("user_id", request.getUserId());
            }
            if (request.getCursor() != null) {
                params.put("cursor", request.getCursor());
            }
        }

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                callback.onSuccess(gson.fromJson(responseString, UserFollowResponse.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void getFollowers(UserFollowRequest request, final UserFollowCallback callback) {
        String apiUrl = getApiUrl("followers/list.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getUserId() != null) {
                params.put("user_id", request.getUserId());
            }
            if (request.getCursor() != null) {
                params.put("cursor", request.getCursor());
            }
        }

        client.get(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                callback.onSuccess(gson.fromJson(responseString, UserFollowResponse.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void retweet(final RetweetRequest request, final RetweetCallback callback) {
        String apiUrl;
        if (request.isRetweet()) {
            apiUrl = getApiUrl(String.format("statuses/unretweet/%1$s.json", request.getTweetId().toString()));
        } else {
            apiUrl = getApiUrl(String.format("statuses/retweet/%1$s.json", request.getTweetId().toString()));
        }

        RequestParams params = new RequestParams();
        params.put("id", request.getTweetId());

        client.post(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                Tweet tweet = gson.fromJson(responseString, Tweet.class);
                callback.onSuccess(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    public void markAsFavorite(FavoriteTweetRequest request, final FavoriteTweetCallback callback) {
        String apiUrl;
        if (request.isFavorite()) {
            apiUrl = getApiUrl("favorites/destroy.json");
        } else {
            apiUrl = getApiUrl("favorites/create.json");
        }

        RequestParams params = new RequestParams();
        params.put("id", request.getTweetId());

        client.post(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                Tweet tweet = gson.fromJson(responseString, Tweet.class);
                callback.onSuccess(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }


    public void postMessages(NewMessageRequest request, final NewPostMessageCallback callback) {
        String apiUrl = getApiUrl("direct_messages/new.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getTextOfMessage() != null && !"".equals(request.getTextOfMessage())) {
                params.put("text", request.getTextOfMessage());
            }
            if (request.getScreenName() != null && !"".equals(request.getScreenName())) {
                params.put("screen_name", request.getScreenName());
            }
        }
        Log.d ("DEBUG", "params"+params);

        client.post(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                Message message = gson.fromJson(responseString, Message.class);
                callback.onSuccess(message);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }

    //Create new post
    public void postNewTweet(NewTweetRequest request, final NewPostTweetCallback callback) {
        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        if (request != null) {
            if (request.getStatus() != null && !"".equals(request.getStatus())) {
                params.put("status", request.getStatus());
            }
        }
        Log.d ("DEBUG", "params"+params);

        client.post(apiUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DeserializerDate())
                        .create();
                Tweet tweet = gson.fromJson(responseString, Tweet.class);
                callback.onSuccess(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callback.onError(new Error(throwable != null ? throwable.getMessage() : null));
            }
        });
    }


}

