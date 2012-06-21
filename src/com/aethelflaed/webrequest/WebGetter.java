package com.aethelflaed.webrequest;

import org.apache.http.client.methods.HttpGet;

/**
 * Specialized WebRequest for HTTP GET queries.
 * @see WebRequest
 * @author Aethelflaed
 */
public class WebGetter extends WebRequest
{
	/**
	 * Construct a new WebGetter object.
	 * 
	 * @param url The query URL, as a string
	 * @param handler The requester (listener) or the query
	 */
	public WebGetter(String url, Requester handler)
	{
		super(url, handler, "application/json");
	}

	/**
	 * Start the query
	 */
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
