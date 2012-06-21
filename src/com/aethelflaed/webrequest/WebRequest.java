package com.aethelflaed.webrequest;

import org.apache.http.client.methods.HttpUriRequest;

import android.os.Handler;
import android.os.Message;

/**
 * Basic web request
 * 
 * @author Aethelflaed
 */
public abstract class WebRequest implements Handler.Callback
{
	/**
	 * Interface implemented by object which wants to listen to the request
	 *  
	 * @author Aethelflaed
	 */
	public interface Requester
	{
		public void requestFinished(WebRequest request, String response, int httpCode);

		public void requestFailed(WebRequest request);
	}

	public static final int MSG_CONNECTION_FAIL = 0;
	public static final int MSG_CONNECTION_SUCCESS = 1;

	protected String url;
	protected Requester handler;
	protected String contentType;
	protected boolean async;
	protected int tag;

	private Handler messageHandler;

	private GetHttpResponse response;

	/**
	 * Construct a new WebRequest object
	 * 
	 * @param url The query URL, as a string
	 * @param handler The requester (listener) or the query
	 * @param contentType The contentType of the query
	 */
	public WebRequest(String url, Requester handler, String contentType)
	{
		super();

		this.url = url;
		this.handler = handler;
		this.contentType = contentType;
		this.async = true;

		this.messageHandler = new Handler(this);
	}

	/**
	 * Handle messages received from the underlying thread.
	 */
	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what)
		{
			case MSG_CONNECTION_FAIL:
				if (this.handler != null)
				{
					WebRequest.this.handler
							.requestFailed(WebRequest.this);
				}
				return true;
			case MSG_CONNECTION_SUCCESS:
				if (this.handler != null)
				{
					WebRequest.this.handler.requestFinished(
							WebRequest.this, (String) msg.obj, msg.arg1);
				}
				return true;
		}
		return false;
	}

	/**
	 * Start the request with a HttpUriRequest
	 * 
	 * @param request
	 */
	protected void start(HttpUriRequest request)
	{
		this.response = new GetHttpResponse(request, this.messageHandler);
		if (this.async)
		{
			new Thread(this.response, this.getClass().getSimpleName()).start();
		}
		else
		{
			this.response.run();
		}
	}

	/**
	 * Stop the request
	 */
	public void stop()
	{
		if (this.response != null)
		{
			this.response.stop();
			this.handler = null;
			this.response = null;
		}
	}

	/**
	 * @return The request's url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Set the request's url.
	 * Has no effect once the request is started.
	 * 
	 * @param url
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * @return The request's handler
	 */
	public Requester getHandler()
	{
		return handler;
	}

	/**
	 * Set the request's handler.
	 * May have no effect once started.
	 * 
	 * @param handler
	 */
	public void setHandler(Requester handler)
	{
		this.handler = handler;
	}

	/**
	 * @return The MIME content-type
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * Set the MIME content-type.
	 * Has no effect once the request is started
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * Check if the request is asynchronous or not.
	 * 
	 * @return
	 */
	public boolean isAsync()
	{
		return this.async;
	}

	/**
	 * Set the request as asynchronous or not.
	 * Has no effect once the request is started
	 * 
	 * @param async
	 */
	public void setAsync(boolean async)
	{
		this.async = async;
	}

	/**
	 * @return The identifier tag
	 */
	public int getTag()
	{
		return tag;
	}

	/**
	 * Set the identifier tag.
	 * May be used in case of multiple connections, to differenciate
	 * one request from another.
	 * 
	 * @param tag
	 */
	public void setTag(int tag)
	{
		this.tag = tag;
	}
}
