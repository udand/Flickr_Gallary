package com.flickrgallery.activity;

import java.io.File;
import java.util.ArrayList;

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

import com.flickrgallery.data.Object;
import com.flickrgallery.observer.Observer;
import com.flickrgallery.request.GetList;
import com.flickrgallery.request.GetPhotos;
import com.flickrgallery.util.Util;
import com.google.gson.Gson;

/**
 * MainActivity displays the photos from flciker api in gridview.
 * 
 * @author Umang
 */
public class MainActivity extends Activity implements Observer {

	private GridView gridView;
	private Object result = null;
	private ImageAdapter imageAdapter;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridView = (GridView) findViewById(R.id.gridView1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		String Url = Util.LIST_URL + Util.METHOD_GET_PHOTOS + "&"
				+ Util.API_KEY + "&format=json&nojsoncallback=1";
		System.out.println("Url:" + Url);

		//TODO: work on file save method ,service mechanism to refresh gallery
		File file = new File(Util.DIR_PATH);
		for (File file2 : file.listFiles()) {
			file2.delete();
		}
		File file1 = new File(Util.DIR_PATH_FULL_IMAGE);
		for (File file2 : file1.listFiles()) {
			file2.delete();
		}
		
		
		GetList getList = new GetList();
		getList.registerObserver(this);
		getList.execute(Url);
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
	public void update(String string) {
		if (!string.equalsIgnoreCase("DOWNLOAD_COMPLETE")) {
			System.out.println("Result in update::" + string);
			Gson gson = new Gson();
			result = gson.fromJson(string, Object.class);
			System.out.println("size of the photos list:"
					+ result.photosResult.photosList.size());
			GetPhotos getPhotos = new GetPhotos();
			getPhotos.registerObserver(this);
			getPhotos.execute(result);
		} else if (string.equalsIgnoreCase("DOWNLOAD_COMPLETE")) {
			progressBar.setVisibility(View.GONE);
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
					Intent intent = new Intent(MainActivity.this,
							ImageActivity.class);
					intent.putExtra("id",
							result.photosResult.photosList.get(position).id);
					intent.putExtra("farm",
							result.photosResult.photosList.get(position).farm);
					intent.putExtra("server",
							result.photosResult.photosList.get(position).server);
					intent.putExtra("secret",
							result.photosResult.photosList.get(position).secret);
					intent.putExtra("title",
							result.photosResult.photosList.get(position).title);
					startActivity(intent);
				}
			});
		}
	}

	public class ImageAdapter extends BaseAdapter {

		public Context context;
		private ArrayList<String> filePaths = new ArrayList<String>();
		private File file;

		public ImageAdapter(Context context) {
			this.context = context;
			file = new File(Util.DIR_PATH);
			System.out
					.println("============length::" + file.listFiles().length);
			for (File files : file.listFiles()) {
				filePaths.add(files.getAbsolutePath());
			}
		}

		@Override
		public int getCount() {
			int length = 0;
			if (file.exists())
				length = file.list().length;
			return length;
		}

		@Override
		public java.lang.Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ImageView imageView;
			Bitmap bitmap = BitmapFactory.decodeFile(filePaths.get(position));
			if (view == null) {
				imageView = new ImageView(context);
				int diplaySize = getResources().getDisplayMetrics().widthPixels;

				int imageWidth = diplaySize / 3;
				imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
						imageWidth));
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			} else {
				imageView = (ImageView) view;
			}
			imageView.setImageBitmap(bitmap);
			return imageView;
		}
	}
}
