WebRequest
==========

A very simple android utility to handle web requests.

```java
WebGetter getter = new WebGetter("http://api.example.com/products", new WebRequest.Requester()
{
	@Override
	public void requestFinished(WebRequest request, String response)
	{
		// finished !
	}
	@Override
	public void requestFailed(WebRequest request)
	{
		// failed !
	}
});
getter.start();
```

