package com.flickrgallery.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Imagedetail activity for potratit mode.
 * @author Umang
 */
public class ImageDetailActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
		}

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			DetailFragment details = new DetailFragment();
			details.setArguments(getIntent().getExtras().getBundle("key"));
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}
