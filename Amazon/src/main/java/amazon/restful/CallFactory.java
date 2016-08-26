package amazon.restful;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

public class CallFactory {
	
	private static Integer responseCode;

	public static<T> Map<String, T> call(Request request) {
		// GET, POST, PUT, DELETE etc
		HttpMethod method = null;
		String myResponse = null;
		
		try {
			HttpClient httpClient = new HttpClient();
			try {
				//URI url = new URI(request.getRequestUrl(), false);
				System.out.println("REQUEST: " + "\nMethod: " + request.getRequestMethod() + "\nUrl: " + request.getRequestUrl());
				switch(request.getRequestMethod().toString()) {
				case "GET" :
					method = new GetMethod(request.getRequestUrl());
					break;
				case "POST" :
					method = new PostMethod(request.getRequestUrl());
					break;
				case "PUT" :
					method = new PutMethod(request.getRequestUrl());
				case "DELETE" : 
					method = new DeleteMethod(request.getRequestUrl());
					break;
				default: throw new RuntimeException("Request method: " + request.getRequestMethod() + " not implemeneted!");
				}
				//method.setURI(url);
				method.setRequestHeader("Accept", "*/*");
				method.getParams().setHttpElementCharset("UTF-8");
				method.getParams().setContentCharset("UTF-8");
				// execute method
				responseCode = httpClient.executeMethod(method);
					InputStream is = null;
					myResponse = null;
					try {
						is = method.getResponseBodyAsStream();
						ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
						byte[] buf = new byte[1024];
						int len;
						while ((len = is.read(buf, 0, buf.length)) != -1) {
							os.write(buf, 0, len);
						}
						myResponse = os.toString("UTF-8");
					} finally {
						if (is != null) {
							is.close();
						}
					}
					
					System.out.println("RESPONSE:\n" + myResponse);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (method != null) {
					method.releaseConnection();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, T> map = new HashMap<String, T>();
		map.put("response", (T) myResponse);
		map.put("responseCode",(T) responseCode);
	
	   return map;
	}
	
	public static String call(String request, RequestMethod requestMethod) {
		// GET, POST, PUT, DELETE etc
		HttpMethod method = null;
		String myResponse = null;
		
		try {
			HttpClient httpClient = new HttpClient();
			try {
				URI url = new URI(request, false);
				System.out.println("REQUEST: " + "\nMethod: " + requestMethod.toString() + "\nUrl: " + request);
				switch(requestMethod.toString()) {
				case "GET" :
					method = new GetMethod(request);
					break;
				case "POST" :
					method = new PostMethod(request);
					break;
				case "PUT" :
					method = new PutMethod(request);
				case "DELETE" : 
					method = new DeleteMethod(request);
					break;
				default: throw new RuntimeException("Request method: " + requestMethod.toString() + " not implemeneted!");
				}
				//method.setURI(url);
				method.setRequestHeader("Accept", "*/*");
				method.getParams().setHttpElementCharset("UTF-8");
				method.getParams().setContentCharset("UTF-8");
				// execute method
				httpClient.executeMethod(method);
					InputStream is = null;
					myResponse = null;
					try {
						is = method.getResponseBodyAsStream();
						ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
						byte[] buf = new byte[1024];
						int len;
						while ((len = is.read(buf, 0, buf.length)) != -1) {
							os.write(buf, 0, len);
						}
						myResponse = os.toString("UTF-8");
					} finally {
						if (is != null) {
							is.close();
						}
					}
					
					System.out.println("RESPONSE:\n" + myResponse);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (method != null) {
					method.releaseConnection();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	   return myResponse;
	}
}
