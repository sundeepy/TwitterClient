package com.yahoo.sundeep.basictwitter.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweets")
public class Tweet extends Model {

	@Column(name = "Body")
	public String body;

	@Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public long tweetId;

	@Column(name = "Created_At")
	public String createdAt;

	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	public User user;

	public String getBody() {
		return body;
	}

	public Tweet() {
		super();
	}

	public long getTweetId() {
		return tweetId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public static Tweet fromJson(JSONObject json) {
		Tweet tweet = new Tweet();
		// extract values form json
		try {
			tweet.body = json.getString("text");
			tweet.createdAt = json.getString("created_at");
			tweet.tweetId = json.getLong("id");
			tweet.user = User.fromJson(json.getJSONObject("user"));

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static List<Tweet> fromJsonArray(JSONArray jsonArray) {
		List<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		// Process each result in json array, decode and convert to business
		// object
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = Tweet.fromJson(json);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}

		return tweets;
	}

	@Override
	public String toString() {
		return "Tweet [body=" + body + ", id=" + tweetId + ", createdAt="
				+ createdAt + ", user=" + user + "]";
	}

	public static List<Tweet> getAllTweets() {
		List<Tweet> tweets = new ArrayList<Tweet>();
		List<Tweet> tableTweet = new Select().from(Tweet.class).execute();
		tweets.addAll(tableTweet);
		return tweets;
	}

}
