package com.aethelflaed.webconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.os.Message;

class GetHttpResponse implements Runnable
{
	private HttpClient client;
	private HttpUriRequest request;
	private AtomicBoolean cancelled = new AtomicBoolean(false);
	private Handler handler;

	public GetHttpResponse(HttpUriRequest request, Handler handler)
	{
		this.client = new DefaultHttpClient();
		this.request = request;
		this.handler = handler;
	}

	public void run()
	{
		try
		{
			if (this.handler != null)
			{
				HttpResponse response = this.client.execute(this.request);
				if (this.cancelled.get() == false)
				{
					InputStream is = response.getEntity().getContent();
					if (this.cancelled.get() == false)
					{
						String result = this.convertStreamToString(is);

						if (this.cancelled.get() == false)
						{
							this.handler.sendMessage(this.handler.obtainMessage(
									WebConnection.MSG_CONNECTION_SUCCESS, result));
						}
					}
				}
			}
			else
			{
				this.client.execute(this.request);
			}
		}
		catch (Exception e)
		{
			this.handler.sendEmptyMessage(WebConnection.MSG_CONNECTION_FAIL);
		}
	}

	public String convertStreamToString(InputStream is)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				if (this.cancelled.get())
				{
					break;
				}
				sb.append(line + "\n");
			}
		}
		catch (IOException e)
		{
			Message msg = new Message();
			msg.what = WebConnection.MSG_CONNECTION_FAIL;
			this.handler.sendMessage(msg);
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				Message msg = new Message();
				msg.what = WebConnection.MSG_CONNECTION_FAIL;
				this.handler.sendMessage(msg);
			}
		}
		return sb.toString();
	}

	public void stop()
	{
		this.cancelled.set(true);
		this.handler = null;
	}
}
