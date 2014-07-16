package com.flickrgallery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.flickrgallery.util.Util;

/**
 * MainActivity displays the photos from Flickr api in gridview.
 * @author Umang
 */
public class SearchActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		
		Util.URL = getIntent().getExtras().getString("Url").toString();
		Toast.makeText(this, getIntent().getExtras().getString("Url").toString(), Toast.LENGTH_LONG).show();
	}
}
