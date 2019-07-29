package com.codepath.apps.restclienttemplate.models;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public TimeFormatter timeFormatter;

    public Tweet(JSONObject json) {
    }

    public static Tweet fromJSON(JSONObject json) {
        return new Tweet(json);
    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException
    {

        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.uid=jsonObject.getLong("id");
        tweet.createdAt= jsonObject.getString("created_at");
        tweet.user= User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

    public String getFormattedTimestamp()
    {
        final String timeDifference = TimeFormatter.getTimeDifference(createdAt);
        return timeDifference;
    }

    public static List<Tweet> fromJSON(JSONArray tweetsJSON) {
        List<Tweet> tweets = new ArrayList<>(tweetsJSON.length());

        for (int i = 0; i < tweetsJSON.length(); i++) {
            try {
                tweets.add(new Tweet(tweetsJSON.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tweets;
    }
}

