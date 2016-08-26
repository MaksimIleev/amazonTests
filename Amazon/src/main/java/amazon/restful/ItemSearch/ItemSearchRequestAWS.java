package amazon.restful.ItemSearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import amazon.PropertyUtils;
import amazon.restful.Request;
import amazon.restful.RequestMethod;

public class ItemSearchRequestAWS extends Request {

	private String service = null;
	private String operation = null;
	private String subscriptionId = null;
	private String associateTag = null;
	private List<String> keywords = null;
	private List<String> responseGroup = null;
	private String sort = null;
	private String format = null;
	private String url;

	private ItemSearchRequestAWS(String url, RequestMethod requestMethod) {
		super(requestMethod);
		this.setUrl(url);
	}// prevent instantiation

	@Override
	public String getRequestUrl() {
		return this.url;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getAssociateTag() {
		return associateTag;
	}

	public void setAssociateTag(String associateTag) {
		this.associateTag = associateTag;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getResponseGroup() {
		return responseGroup;
	}

	public void setResponseGroup(List<String> responseGroup) {
		this.responseGroup = responseGroup;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static class ItemSearchRequestBuilder {

		private RequestMethod requestMethod = null;
		private final String UTF8_CHARSET = "UTF-8";
		private final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
		private String awsAccessKeyId = PropertyUtils.getProperty("aws.access.key");
		private String awsSecretKey = PropertyUtils.getProperty("aws.secret.key");;
		private SecretKeySpec secretKeySpec = null;
		private Mac mac = null;

		private final String DOMAIN = "webservices.amazon.com";
		private final String REQUEST_URI = "/onca/xml";
		private final String SERVICE = "Service";
		private final String OPERATION = "Operation";
		private final String SUBSCRIPTIONID = "SubscriptionId";
		private final String ASSOCIATETAG = "AssociateTag";
		private final String SEARCHINDEX = "SearchIndex";
		private final String KEYWORDS = "Keywords";
		private final String RESPONSEGROUP = "ResponsGroups";
		private final String SORT = "Sort";

		private String service = null;
		private String operation = null;
		private String subscriptionId = null;
		private String associateTag = null;
		private List<String> keywords = null;
		private List<String> responseGroup = null;
		private String sort = null;
		private String format = null;
		Map<String, String> params = new HashMap<String, String>();;

		public ItemSearchRequestBuilder(RequestMethod requestMethod)
				throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
			this.requestMethod = requestMethod;
			byte[] secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
			secretKeySpec = new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
			mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(secretKeySpec);
		}

		public ItemSearchRequestAWS build() {
			// Required parameters
			params.put("Service", "AWSECommerceService");
			params.put("AWSAccessKeyId", awsAccessKeyId);
			params.put("Timestamp", timestamp());
			params.put("Operation", "ItemSearch");

			SortedMap<String, String> sortedParamMap = new TreeMap<String, String>(params);
			String canonicalQS = canonicalize(sortedParamMap);
			String toSign = requestMethod.toString() + "\n" + DOMAIN + "\n" + REQUEST_URI + "\n" + canonicalQS;

			String hmac = hmac(toSign);
			String sig = percentEncodeRfc3986(hmac);
			String url = "http://" + DOMAIN + REQUEST_URI + "?" + canonicalQS + "&Signature=" + sig;

			return new ItemSearchRequestAWS(url, requestMethod);
		}

		public synchronized ItemSearchRequestBuilder addAssociateTag(String associateTag) {
			this.associateTag = associateTag;
			params.put("AssociateTag", associateTag);
			return this;
		}

		public ItemSearchRequestBuilder addSearchIndex(String searchIndex) {
			params.put("SearchIndex", searchIndex);
			return this;
		}

		public ItemSearchRequestBuilder addKeywords(List<String> keywords) {
			StringBuilder sb = null;
			if (keywords != null)
				sb = new StringBuilder();
			for (int i = 0; i < keywords.size(); i++) {
				sb.append(keywords.get(i));
				if (i != keywords.size() - 1)
					sb.append(",");
			}

			params.put("Keywords", sb.toString());
			return this;
		}

		public ItemSearchRequestBuilder addResponseGroup(List<String> responseGroups) {
			StringBuilder sb = null;
			if (responseGroups != null)
				sb = new StringBuilder();
			for (int i = 0; i < responseGroups.size(); i++) {
				sb.append(responseGroups.get(i));
				if (i != responseGroups.size() - 1)
					sb.append(",");
			}

			params.put("ResponseGroup", sb.toString());
			return this;
		}

		public ItemSearchRequestBuilder addSort(String sort) {
			params.put("Sort", sort);
			return this;
		}

		private String hmac(String stringToSign) {
			String signature = null;
			byte[] data;
			byte[] rawHmac;
			try {
				data = stringToSign.getBytes(UTF8_CHARSET);
				rawHmac = mac.doFinal(data);
				Base64 encoder = new Base64();
				signature = new String(encoder.encode(rawHmac));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
			}
			return signature;
		}

		private String timestamp() {
			String timestamp = null;
			Calendar cal = Calendar.getInstance();
			DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dfm.setTimeZone(TimeZone.getTimeZone("UTC"));

			timestamp = dfm.format(cal.getTime());
			return timestamp;
		}

		private String canonicalize(SortedMap<String, String> sortedParamMap) {
			if (sortedParamMap.isEmpty()) {
				return "";
			}

			StringBuffer buffer = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry<String, String> kvpair = iter.next();
				buffer.append(percentEncodeRfc3986(kvpair.getKey()));
				buffer.append("=");
				buffer.append(percentEncodeRfc3986(kvpair.getValue()));
				if (iter.hasNext()) {
					buffer.append("&");
				}
			}
			String canonical = buffer.toString();
			return canonical;
		}

		private String percentEncodeRfc3986(String s) {
			String out;
			try {
				out = URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
			} catch (UnsupportedEncodingException e) {
				out = s;
			}
			return out;
		}

	}

}
