package com.flickrgallery.data;

import com.google.gson.annotations.SerializedName;

public class Object {
	@SerializedName("photos")
	public Photos photosResult;
	@SerializedName("stat")
	public String stat;
}
