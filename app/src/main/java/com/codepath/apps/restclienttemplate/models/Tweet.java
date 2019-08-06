package com.codepath.apps.restclienttemplate.models;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public TimeFormatter timeFormatter;

    public Tweet()
    {}

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

