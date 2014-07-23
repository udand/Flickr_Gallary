package com.flickrgallery.request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.flickrgallery.observer.Observable;
import com.flickrgallery.observer.Observer;
import com.flickrgallery.util.Util;

public class GetOrigianlPhoto extends AsyncTask<String, Void, String> implements
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
	protected String doInBackground(String... params) {
		String url = params[0];
		String[] param = url.split(":");
		
//		System.out.println("Image --------- Url:"+"https://farm" + param[0]
//						+ ".staticflickr.com/" + param[2] + "/"
//						+ param[1] + "_" + param[3] + "_" + "b" + ".jpg");
		
		downloadImage("https://farm" + param[0]
						+ ".staticflickr.com/" + param[2] + "/"
						+ param[1] + "_" + param[3] + "_" + "b" + ".jpg", param[1]);
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
			File file = new File(Util.DIR_PATH_FULL_IMAGE);
			if (!file.exists())
				file.mkdir();
			File file2 = new File(Util.DIR_PATH_FULL_IMAGE + id);
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
