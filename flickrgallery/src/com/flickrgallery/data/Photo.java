package com.flickrgallery.data;

import com.google.gson.annotations.SerializedName;

public class Photo {
	@SerializedName("id")
	public String id;
	@SerializedName("owner")
	public String owner;
	@SerializedName("secret")
	public String secret;
	@SerializedName("server")
	public String server;
	@SerializedName("farm")
	public String farm;
	@SerializedName("title")
	public String title;
	@SerializedName("ispublic")
	public int isPublic;
	@SerializedName("isfriend")
	public int isFriend;
	@SerializedName("isfamily")
	public int isFamily;
}
