package com.yahoo.sundeep.basictwitter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.text.format.DateUtils;

public class TwitterUtil {

	private static final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

	public static String getTwitterDate(String date)  {

		SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
		sf.setLenient(true);

		String str = "";
		try {
			str = (String) DateUtils.getRelativeTimeSpanString(sf
					.parse(date).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	
	public static void main(String args[]) throws ParseException{
		System.out.println(TwitterUtil.getTwitterDate("Tue Aug 28 21:16:23 +0000 2012"));
	}
}
