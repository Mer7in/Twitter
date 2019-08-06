package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private EditText etCompose;
    private Button btnTweet;
    private  TwitterClient client;
    private TextView counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client=TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet=findViewById(R.id.btnTweet);
        counter=findViewById(R.id.counter);
        TextWatcher mTextEditorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1)
                    counter.setText("You typed : "+String.valueOf(s.length()+" letters"));
                else
                    counter.setText("You typed : "+String.valueOf(s.length()+"  letter"));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etCompose.addTextChangedListener(mTextEditorWatcher);
        // Set Click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent=etCompose.getText().toString();
                if(tweetContent.isEmpty())
                {
                    Toast.makeText(ComposeActivity.this, "Your Tweet is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(tweetContent.length()>140)
                {
                    Toast.makeText(ComposeActivity.this, "", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(ComposeActivity.this,tweetContent,Toast.LENGTH_LONG);
                client.composeTweet(tweetContent, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("TwitterClient", "Successfully posted tweet! "+ response.toString());
                        try {
                            Tweet tweet= Tweet.fromJson(response);
                            Intent data=new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK,data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("TwitterClient", "Failed to post tweet: "+ responseString);
                    }
                });
            }
        });
        // Make api call to Twitter to publish the content in edit text
    }
}
