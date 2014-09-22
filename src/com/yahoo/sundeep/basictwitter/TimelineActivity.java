package com.yahoo.sundeep.basictwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.sundeep.basictwitter.R;
import com.yahoo.sundeep.basictwitter.models.Tweet;
import com.yahoo.sundeep.basictwitter.models.User;
import com.yahoo.sundeep.basictwitter.util.ACTION;
import com.yahoo.sundeep.basictwitter.util.IntentConstants;

public class TimelineActivity extends Activity {
	private TwitterClient twitterClient;
	private List<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;
	private static long maxId = -1;
	private static long sinceId = -1;
	private SwipeRefreshLayout swipeContainer;
	public ACTION action = ACTION.REFRESH;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		twitterClient = TwitterApplication.getRestClient();
		populateTimeline();
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		setUpListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);

		return true;
	}

	public void onRefresh(MenuItem mi) {
		resetAdapterAndMaxIdAndPopulateTimeline();
	}

	private void resetAdapterAndMaxIdAndPopulateTimeline() {
		action = ACTION.REFRESH;
		maxId = -1;
		aTweets.clear();
		populateTimeline();
	}

	public void onCompose(MenuItem mi) {
		Intent iCompose = new Intent(this, ComposeActivity.class);
		startActivityForResult(iCompose, IntentConstants.COMPOSE_INTENT);
	}

	private void setUpListeners() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				action = ACTION.SCROLL;
				populateTimeline();
				// or customLoadMoreDataFromApi(totalItemsCount);
			}
		});

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				resetAdapterAndMaxIdAndPopulateTimeline();

			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

	}

	public void populateTimeline() {

		twitterClient.getHomeClient(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<Tweet> tweetsList = Tweet.fromJsonArray(jsonArray);
				aTweets.addAll(tweetsList);
				System.out.println(jsonArray);
				aTweets.notifyDataSetChanged();
				maxId = tweets.get(tweets.size() - 1).getTweetId() - 1;
				sinceId = tweets.get(0).getTweetId() - 1;
				swipeContainer.setRefreshing(false);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		}, maxId, sinceId, action);

	}

	private void saveToDB(List<Tweet> tweetsList) {
		new Delete().from(Tweet.class).execute();
		new Delete().from(User.class).execute();
		List<User> users = new ArrayList<User>();
		int i = 0;
		for (Tweet tweet : tweetsList) {
			for (i = 0; i < users.size(); i++) {
				if (users.get(i).getUserId() == tweet.getUser().getUserId()) {
					break;
				}
			}
			if (i == users.size()) {
				users.add(tweet.getUser());
			}
		}
		ActiveAndroid.beginTransaction();
		try {

			for (User user : users) {
				user.save();
			}
			for (Tweet tweet : tweetsList) {
				tweet.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IntentConstants.COMPOSE_INTENT) {
			if (resultCode == RESULT_OK) {
				String tweet = data.getStringExtra("tweet");
				twitterClient.postTweetClient(new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject json) {
						resetAdapterAndMaxIdAndPopulateTimeline();
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						Log.d("debug", s.toString());
					}
				}, tweet);
			}
		}
	}
}
