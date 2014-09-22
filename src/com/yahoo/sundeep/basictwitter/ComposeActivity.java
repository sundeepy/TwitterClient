package com.yahoo.sundeep.basictwitter;

import com.yahoo.sundeep.basictwitter.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ComposeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
	}

	public void onSave(View v) {
		EditText etTweet = (EditText)findViewById(R.id.etTweet);
		Intent i = new Intent();
		i.putExtra("tweet", etTweet.getText().toString());
		// submit my result to parent activity
		setResult(RESULT_OK, i);
		finish();

	}
}
