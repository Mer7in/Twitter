package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE= 20;
    private TwitterClient client;
    RecyclerView rvTweets;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client=TwitterApp.getRestClient(this);
        swipeContainer =findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //find the recycler view
        rvTweets=findViewById(R.id.rvTweets);
        //Initialize list of tweets and adapter form the data source
        tweets=new ArrayList<>();
        adapter=new TweetsAdapter(this,tweets);
        // Recycler View seetup: layout manager and setting the adapter
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }


        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        rvTweets.setAdapter(adapter);
        populateHomeTimeLine();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TwitterClient", "content is being refreshed");
                populateHomeTimeLine();
            }
        });
    }

    // this is where we will make another API call to get the next page of tweets and add the objects to our current list of tweets
    public void loadMoreData() {
        // 1. Send an API request to retrieve appropriate paginated data
        // 2. Deserialize and construct new model objects from the API response
        // 3. Append the new data objects to the existing set of items inside the array of items
        // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`

        //client.getNextPageOftweets(new JsonHttpResponseHandler());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.compose)
        {
            Intent i=new Intent(this, ComposeActivity.class);
            startActivityForResult(i, REQUEST_CODE);
            //Navigate to a new activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // REQUESTC_DE is defined above
        if(requestCode==REQUEST_CODE && resultCode== RESULT_OK)
        {
            // Pull info out of the data Intent (Tweet)
            //Update the recycler view with this tweet
            Tweet tweet=Parcels.unwrap(data.getParcelableExtra("tweet"));
            //Update the recylcler view with this tweet
            tweets.add(0,tweet);
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infalte the menu; this adds items to the actionbar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void populateHomeTimeLine()
    {
        client.getHomeTimeline(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

               // Log.d("TwitterClient", response.toString());
                //Iterate through the list of tweet
                List<Tweet> tweetsToAdd=new ArrayList<>();
                for(int i=0; i< response.length(); i++)
                {
                    try
                    {
                        // Convert each JSON object into a Tweet object

                        JSONObject jsonTweetObject=response.getJSONObject(i);
                        Tweet tweet=Tweet.fromJson(jsonTweetObject);
                        // Add the tweet into our data source
                        tweetsToAdd.add(tweet);
                        tweets.add(tweet);
                        // Notify adapter
                        adapter.notifyItemInserted(tweets.size() - 1);
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                adapter.clear();
                //SHow the data we just received
                adapter.addTweets(tweetsToAdd);

                //Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TwitterClient", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TwitterClient", errorResponse.toString());
            }
        });
    }
}
