package com.flickrgallery.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.flickrgallery.data.Data;
import com.flickrgallery.data.Photo;
import com.flickrgallery.observer.Observer;
import com.flickrgallery.request.GetList;
import com.flickrgallery.request.GetPhotos;
import com.flickrgallery.util.Util;
import com.google.gson.Gson;

public class TitleFragment extends Fragment implements Observer {

	private GridView gridView;
	private Data result = null;
	private ImageAdapter imageAdapter;
	private ProgressBar progressBar;
	private LinkedHashSet<String> filePaths = new LinkedHashSet<String>();
	private File file;
	private boolean mDualPane;
	private int index = 0;
	private int length = 0;
	private GetPhotos getPhotos;
	public static String Url = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_main, container, false);

		gridView = (GridView) view.findViewById(R.id.gridView1);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);

		if (Util.URL == null) {
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
			Url = Util.LIST_URL + Util.METHOD_GET_PHOTOS + "&" + Util.API_KEY
					+ "&format=json&nojsoncallback=1";
		} else {
			Url = Util.URL;
		}

		GetList getList = new GetList();
		getList.registerObserver(this);
		getList.execute(Url);

		// TODO: gridview

		int displayWidth = getResources().getDisplayMetrics().widthPixels;

		int imageWidth = displayWidth / 3;
		gridView.setColumnWidth(imageWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		imageAdapter = new ImageAdapter(getActivity());
		gridView.setAdapter(imageAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Photo photo = (Photo) view.getTag();
				if (mDualPane) {
					DetailFragment details = (DetailFragment) getFragmentManager()
							.findFragmentById(R.id.details1);

					details = DetailFragment.newInstance(index);
					Bundle bundle = new Bundle();
					bundle.putString("id", photo.id);
					bundle.putString("farm", photo.farm);
					bundle.putString("server", photo.server);
					bundle.putString("secret", photo.secret);
					bundle.putString("title", photo.title);
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.remove(details);
					details.setArguments(bundle);
					ft.replace(R.id.details1, details);
					ft.addToBackStack(null);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
					// }
				} else {

					Intent intent = new Intent(getActivity(),
							ImageDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("id", photo.id);
					bundle.putString("farm", photo.farm);
					bundle.putString("server", photo.server);
					bundle.putString("secret", photo.secret);
					bundle.putString("title", photo.title);
					intent.putExtra("key", bundle);
					startActivity(intent);
				}
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View detailsFrame = getActivity().findViewById(R.id.details1);
		mDualPane = detailsFrame != null
				&& detailsFrame.getVisibility() == View.VISIBLE;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void update(String string) {
		if (!string.equalsIgnoreCase("DOWNLOAD_COMPLETE")) {
			Gson gson = new Gson();
			result = gson.fromJson(string, Data.class);
			length = result.photosResult.photosList.size();
			if (Url.contains("search")) {
				getPhotos = new GetPhotos(Util.DIR_PATH_SEARCH_IMAGE);
			} else {
				getPhotos = new GetPhotos(Util.DIR_PATH);
			}
			getPhotos.registerObserver(this);
			getPhotos.execute(result);
		} else if (string.equals("DOWNLOAD_COMPLETE")) {
//			System.out.println("Downloaded imagedd");
			// progressBar.setVisibility(View.GONE);
			if (Url.contains("search")) {
				file = new File(Util.DIR_PATH_SEARCH_IMAGE);
			} else{
				file = new File(Util.DIR_PATH);
			}
			for (File files : file.listFiles()) {
				synchronized (filePaths) {
					filePaths.add(files.getAbsolutePath());
				}
			}

			getActivity().runOnUiThread(new Runnable() {

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
				imageView.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_launcher));
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
			synchronized (filePaths) {
				if (filePaths != null && filePaths.size() > position) {
					Bitmap bitmap = BitmapFactory.decodeFile(filePaths
							.toArray()[position].toString());
					imageView.setTag(null);
					String[] str = filePaths.toArray()[position].toString()
							.split("/");
					imageView.setTag(getPhoto(str[str.length - 1],
							result.photosResult.photosList));
					imageView.setImageBitmap(bitmap);
				}
			}
			return imageView;
		}
	}

	private Photo getPhoto(String id, ArrayList<Photo> arrayList) {
		for (Photo photo : arrayList) {
			if (photo.id.equalsIgnoreCase(id)) {
				return photo;
			}
		}
		return null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}