package com.flickrgallery.util;

import android.os.Environment;

public class Util {
	public static final String LIST_URL = "https://api.flickr.com/services/rest/?method=";
	public static final String METHOD_GET_PHOTOS = "flickr.interestingness.getList";
	public static final String API_KEY = "api_key=8892671941ec3dc85025d8eabb09aab4";
//	public static final String PHOTO_URL = "https://farm4.staticflickr.com";
	public static final String DIR_PATH = Environment.getExternalStorageDirectory()
			.getPath() + "/flickrgallery/";
	public static final String DIR_PATH_FULL_IMAGE = Environment.getExternalStorageDirectory()
			.getPath() + "/flickrgalleryoriginal/";
}
