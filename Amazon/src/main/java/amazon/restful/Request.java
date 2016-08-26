package amazon.restful;


public abstract class Request {
	
	private RequestMethod requestMethod = null;
	
	public Request(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	abstract public String getRequestUrl();
	
	public RequestMethod getRequestMethod() {
		return this.requestMethod;
	}
	
}
