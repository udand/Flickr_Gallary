package com.flickrgallery.data;

import com.google.gson.annotations.SerializedName;

public class Data {
	@SerializedName("photos")
	public Photos photosResult;
	@SerializedName("stat")
	public String stat;
}
