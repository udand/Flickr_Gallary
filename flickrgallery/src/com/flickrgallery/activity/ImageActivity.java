package com.flickrgallery.activity;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.flickrgallery.observer.Observer;
import com.flickrgallery.request.GetOrigianlPhoto;
import com.flickrgallery.util.Util;

/**
 * Download the larger image and display in Imageview.
 * 
 * @author Umang
 */
public class ImageActivity extends Activity implements Observer {

	private ProgressBar progressBar;
	private TextView imageTitle;
	private ImageView imageView;
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.full_image);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		imageView = (ImageView) findViewById(R.id.imageView1);
		imageTitle = (TextView) findViewById(R.id.imageTitle);

		String farm = getIntent().getExtras().getString("farm");
		id = getIntent().getExtras().getString("id");
		String server = getIntent().getExtras().getString("server");
		String secret = getIntent().getExtras().getString("secret");
		String title = getIntent().getExtras().getString("title");
		imageTitle.setText(title);

		File file = new File(Util.DIR_PATH_FULL_IMAGE + id);
		if (file.exists()) {
			displayImage(file);
		} else {
			GetOrigianlPhoto getOrigianlPhoto = new GetOrigianlPhoto();
			getOrigianlPhoto.registerObserver(this);
			System.out.println("URI::" + farm + ":" + id + ":" + server + ":"
					+ secret);
			getOrigianlPhoto.execute(farm + ":" + id + ":" + server + ":"
					+ secret);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void update(String string) {
		if (string.equalsIgnoreCase("DOWNLOAD_COMPLETE")) {
			File file = new File(Util.DIR_PATH_FULL_IMAGE + id);
			displayImage(file);
		}
	}

	public void displayImage(File file) {
		System.out.println("File exsits:" + file.exists());
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		imageView.setImageBitmap(bitmap);
		progressBar.setVisibility(View.GONE);
	}
}
