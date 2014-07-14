package com.flickrgallery.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.flickrgallery.data.Data;
import com.flickrgallery.data.Photo;
import com.flickrgallery.observer.Observer;
import com.flickrgallery.request.GetList;
import com.flickrgallery.request.GetPhotos;
import com.flickrgallery.util.Util;
import com.google.gson.Gson;

/**
 * MainActivity displays the photos from flciker api in gridview.
 * @author Umang
 */
public class MainActivity extends Activity implements Observer {

	private GridView gridView;
	private Data result = null;
	private ImageAdapter imageAdapter;
	private ProgressBar progressBar;
	private HashSet<String> filePaths = new HashSet<String>();
	private File file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);

		String Url = Util.LIST_URL + Util.METHOD_GET_PHOTOS + "&"
				+ Util.API_KEY + "&format=json&nojsoncallback=1";
		System.out.println("Url:" + Url);

		// TODO: work on file save method ,service mechanism to refresh gallery
		File file = new File(Util.DIR_PATH);
		if (file.exists())
			for (File file2 : file.listFiles()) {
				file2.delete();
			}
		File file1 = new File(Util.DIR_PATH_FULL_IMAGE);
		if (file1.exists())
			for (File file2 : file1.listFiles()) {
				file2.delete();
			}

		GetList getList = new GetList();
		getList.registerObserver(this);
		getList.execute(Url);

		// TODO: gridview

		int displayWidth = getResources().getDisplayMetrics().widthPixels;

		int imageWidth = displayWidth / 3;
		gridView.setColumnWidth(imageWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		imageAdapter = new ImageAdapter(this);
		gridView.setAdapter(imageAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

//				 Photo photo = (Photo) parent.getItemAtPosition(position);
				
				 Photo photo = (Photo) view.getTag();
				 Intent intent = new Intent(MainActivity.this,
				 ImageActivity.class);
				 intent.putExtra("id", photo.id);
				 intent.putExtra("farm", photo.farm);
				 intent.putExtra("server", photo.server);
				 intent.putExtra("secret", photo.secret);
				 intent.putExtra("title", photo.title);
				 startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem searchViewItem = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView) searchViewItem.getActionView();
		searchView.setIconifiedByDefault(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void update(String string) {
		if (!string.equalsIgnoreCase("DOWNLOAD_COMPLETE")) {
			System.out.println("Result in update::" + string);
			Gson gson = new Gson();
			result = gson.fromJson(string, Data.class);
			System.out.println("size of the photos list:"
					+ result.photosResult.photosList.size());
			GetPhotos getPhotos = new GetPhotos();
			getPhotos.registerObserver(this);
			getPhotos.execute(result);
		} else if (string.equals("DOWNLOAD_COMPLETE")) {
			System.out.println("Downloaded imagedd");
			// progressBar.setVisibility(View.GONE);
			file = new File(Util.DIR_PATH);
			System.out
					.println("============length::" + file.listFiles().length);
			for (File files : file.listFiles()) {
				filePaths.add(files.getAbsolutePath());
			}

			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					imageAdapter.notifyDataSetChanged();
				}
			});

		}
	}

	public class ImageAdapter extends BaseAdapter {

		public Context context;

		public ImageAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			int length = 100;
			return length;
		}

		@Override
		public java.lang.Object getItem(int position) {
			return result.photosResult.photosList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ImageView imageView = null;
			
			if (view != null) {
				imageView = (ImageView) view;
				imageView.setTag(null);
			} else {
				imageView = new ImageView(context);
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_launcher));
				int diplaySize = getResources().getDisplayMetrics().widthPixels;

				int imageWidth = diplaySize / 3;
				imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
						imageWidth));
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setTag(null);
			}
			if (filePaths != null && filePaths.size() > position) {
				Bitmap bitmap = BitmapFactory
						.decodeFile(filePaths.toArray()[position].toString());
				imageView.setTag(null);
				String [] str = filePaths.toArray()[position].toString().split("/");
				imageView.setTag(getPhoto(str[str.length-1],result.photosResult.photosList));
				imageView.setImageBitmap(bitmap);
			}
			return imageView;
		}
	}
	
	public Photo getPhoto(String  id, ArrayList<Photo> arrayList) {
		for (Photo photo : arrayList) {
			if (photo.id.equalsIgnoreCase(id)) {
				return photo;
			}
		}
		return null;
	}
}
