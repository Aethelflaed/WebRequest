WebConnection
=============

A very simple android / java utility to handle web requests.

```java
WebGetter getter = new WebGetter("http://api.example.com/products", new WebConnection.Requester()
{
	@Override
	public void requestFinished(WebConnection request, String response)
	{
		// finished !
	}
	@Override
	public void requestFailed(WebConnection request)
	{
		// failed !
	}
});
getter.start();
```

