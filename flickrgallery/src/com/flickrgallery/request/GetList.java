package com.flickrgallery.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.flickrgallery.observer.Observable;
import com.flickrgallery.observer.Observer;

import android.os.AsyncTask;

/**
 * Download the list of the of the data for images. 
 * @author Umang
 */
public class GetList extends AsyncTask<String, Void, String> implements Observable{
	
	private String result = null;
	private Observer observer;
	
	@Override
	protected String doInBackground(String... arg0) {
		String result = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(arg0[0]));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				result = out.toString();
				System.out.println("Asyn result:"+result);
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		this.result = result;
		notifyObservers();
	}

	@Override
	public void registerObserver(Observer observer) {
		this.observer = observer;
	}

	@Override
	public void notifyObservers() {
		this.observer.update(result);
	}
}
