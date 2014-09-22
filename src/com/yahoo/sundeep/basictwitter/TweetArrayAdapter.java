package com.yahoo.sundeep.basictwitter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yahoo.sundeep.basictwitter.R;
import com.yahoo.sundeep.basictwitter.models.Tweet;
import com.yahoo.sundeep.basictwitter.util.TwitterUtil;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		View v;
		if (convertView == null) {
			v = LayoutInflater.from(getContext()).inflate(
					R.layout.tweet_item, parent, false);
		}else{
			v = convertView;
		}
		// Lookup view for data population
		ImageView ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
		TextView tvRelativeTs = (TextView) v.findViewById(R.id.tvRelativeTs);
		// Populate the data into the template view using the data object
		tvUserName.setText(tweet.getUser().getName() + " ");
		tvRelativeTs.setText(TwitterUtil.getTwitterDate(tweet.getCreatedAt()));
		tvScreenName.setText("@"+tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		// Return the completed view to render on screen
		return v;
	}

}
