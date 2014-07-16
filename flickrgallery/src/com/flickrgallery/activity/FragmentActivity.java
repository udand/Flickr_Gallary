package com.flickrgallery.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.flickrgallery.util.Util;

public class FragmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchViewItem = menu.findItem(R.id.menu_search);
		final SearchView searchView = (SearchView) searchViewItem
				.getActionView();
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				getSearchResult(searchView.getQuery().toString());
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return true;
	}

	private void getSearchResult(String text) {
		String Url = Util.LIST_URL + Util.METHOD_GET_PHOTOS_SEARCH + "&"
				+ Util.API_KEY + "&text=" + text
				+ "&format=json&nojsoncallback=1";
		Util.URL = Url;
		Intent intent = new Intent(this, SearchActivity.class);
		intent.putExtra("Url", Url);
		startActivity(intent);
	}
}
