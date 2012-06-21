package com.aethelflaed.webrequest;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class WebPoster extends WebRequest
{
	public static final String TAG = WebRequest.class.getSimpleName();

	private String data;

	public WebPoster(String url, String data, Requester handler)
	{
		super(url, handler, "application/json");
		this.data = data;
	}

	public void start()
	{
		HttpPost httpPost = null;
		try
		{
			httpPost = new HttpPost(this.url);
		}
		catch (IllegalArgumentException ex)
		{
			if (this.handler != null)
			{
				this.handler.requestFailed(this);
			}
			return;
		}
		
		httpPost.addHeader("Accept", this.contentType);
		httpPost.addHeader("Content-Type", this.contentType);

		try
		{
			StringEntity entity = new StringEntity(this.data, "UTF-8");
			entity.setContentType(this.contentType);
			httpPost.setEntity(entity);
		}
		catch (UnsupportedEncodingException ex)
		{
			if (this.handler != null)
			{
				this.handler.requestFailed(this);
			}
			return;
		}

		this.start(httpPost);
	}
}
