package com.yahoo.sundeep.basictwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {

	@Column(name = "Name")
	public String name;

	@Column(name = "user_id", unique = true)
	public long userId;

	@Column(name = "ScreenName")
	public String screenName;

	@Column(name = "ProfileImageUrl")
	public String profileImageUrl;

	public String getName() {
		return name;
	}

	public long getUserId() {
		return userId;
	}

	public User() {
		super();
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static User fromJson(JSONObject json) {
		User user = new User();
		// extract values form json
		try {
			user.name = json.getString("name");
			user.userId = json.getLong("id");
			user.screenName = json.getString("screen_name");
			user.profileImageUrl = json.getString("profile_image_url");

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", id=" + userId + ", screenName="
				+ screenName + ", profileImageUrl=" + profileImageUrl + "]";
	}

}
