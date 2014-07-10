package com.flickrgallery.data;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Photos {
	@SerializedName("page")
	public int page;
	@SerializedName("pages")
	public int pages;
	@SerializedName("perpage")
	public int perpage;
	@SerializedName("total")
	public int total;
	@SerializedName("photo")
	public ArrayList<Photo> photosList;
}
