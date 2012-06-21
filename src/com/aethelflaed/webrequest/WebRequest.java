package com.aethelflaed.webrequest;

import org.apache.http.client.methods.HttpUriRequest;

import android.os.Handler;
import android.os.Message;

public abstract class WebRequest implements Handler.Callback
{
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

	public WebRequest(String url, Requester handler, String contentType)
	{
		super();

		this.url = url;
		this.handler = handler;
		this.contentType = contentType;
		this.async = true;

		this.messageHandler = new Handler(this);
	}

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

	public void stop()
	{
		if (this.response != null)
		{
			this.response.stop();
			this.handler = null;
			this.response = null;
		}
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Requester getHandler()
	{
		return handler;
	}

	public void setHandler(Requester handler)
	{
		this.handler = handler;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public boolean isAsync()
	{
		return this.async;
	}

	public void setAsync(boolean async)
	{
		this.async = async;
	}

	public int getTag()
	{
		return tag;
	}

	public void setTag(int tag)
	{
		this.tag = tag;
	}
}
