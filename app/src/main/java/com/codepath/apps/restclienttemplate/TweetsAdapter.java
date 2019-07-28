package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> tweets;

    // Pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets)
    {
        this.context=context;
        this.tweets=tweets;
    }
    //for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent, false);
        return new ViewHolder(view);

    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet=tweets.get(position);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(tweet.createdAt);
        holder.tvScreenName.setText(tweet.user.screenName);
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //Clear all items of the recyclerView
    public void clear(){

        tweets.clear();
        notifyDataSetChanged();
    }

    //add a list of item
    public void addTweets(List<Tweet> tweetlist)
    {
        tweets.addAll(tweetlist);
        notifyDataSetChanged();
    }


    // define the ViewHoler
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView ivProfileImage;
        public TextView tvScreenName;
        public TextView tvBody;
        public TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage=itemView.findViewById(R.id.ivProfileImage);
            tvScreenName= itemView.findViewById(R.id.tvScreenName);
            tvBody= itemView.findViewById(R.id.tvBody);
        }
    }
}
