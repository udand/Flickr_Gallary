package com.flickrgallery.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;

import com.flickrgallery.data.Object;
import com.flickrgallery.data.Photo;
import com.flickrgallery.observer.Observable;
import com.flickrgallery.observer.Observer;
import com.flickrgallery.util.Util;

/**
 * Download Thumblin list of photos/
 * @author Umang
 */
public class GetPhotos extends AsyncTask<Object, Void, String> implements
		Observable {
	private Observer observer;
	
	@Override
	protected void onPreExecute() {
		File file = new File(Util.DIR_PATH);
		if (!file.exists())
			file.mkdir();
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Object... params) {
		Object object = params[0];
		if (object != null && object.photosResult != null
				&& object.photosResult.photosList != null
				&& object.photosResult.photosList.size() > 0) {
			for (Photo photos : object.photosResult.photosList) {
				// https://farm4.staticflickr.com/3898/14418693540_26d39b34d8_t.jpg"
				
				File file2 = new File(Util.DIR_PATH + photos.id); 
				if (!file2.exists()) {
					downloadImage("https://farm" + photos.farm
							+ ".staticflickr.com/" + photos.server + "/"
							+ photos.id + "_" + photos.secret + "_" + "q" + ".jpg", photos.id);
				}
			}
		}
		return null;
	}

	@Override
	public void registerObserver(Observer observer) {
		this.observer = observer;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyObservers();
	}

	@Override
	public void notifyObservers() {
		this.observer.update("DOWNLOAD_COMPLETE");
	}

	private void downloadImage(String Url, String id) {
		try {
			URL url2 = new URL(Url);
			Bitmap bitmap = BitmapFactory.decodeStream(url2.openConnection()
					.getInputStream());
			File file = new File(Util.DIR_PATH);
			if (!file.exists())
				file.mkdir();
			File file2 = new File(Util.DIR_PATH + id);
			OutputStream outputStream = new FileOutputStream(file2);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
