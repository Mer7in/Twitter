package com.codepath.apps.restclienttemplate.models;

import android.text.format.Time;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public TimeFormatter timeFormatter;
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
}

