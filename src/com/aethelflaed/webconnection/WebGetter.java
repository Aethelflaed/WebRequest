package com.aethelflaed.webconnection;

import org.apache.http.client.methods.HttpGet;

public class WebGetter extends WebConnection
{
	public WebGetter(String url, Requester handler)
	{
		super(url, handler, "application/json");
	}

	public void start()
	{
		HttpGet httpGet = null;
		try
		{
			httpGet = new HttpGet(this.url);
		}
		catch (IllegalArgumentException ex)
		{
			if (this.handler != null)
			{
				this.handler.requestFailed(this);
			}
			return;
		}
		httpGet.addHeader("Accept", this.contentType);
		httpGet.addHeader("Content-Type", this.contentType);

		this.start(httpGet);
	}
}
