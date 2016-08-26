package amazon.restful;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ResponseFactory {
	
	private ResponseFactory(){
		// ignore
	}
	
	public static synchronized Document convertResponseToDOM(String resp, String fileName) throws IOException {
		/** Create a XML file from response */
		Document doc = null;
		File temp = new File(System.getProperty("user.dir") + "/src/resource/java/xml/" + fileName);
		if(temp.exists() == true) {
			temp.delete();
		}
		
		temp.createNewFile();
		BufferedWriter bw = null;
		String xml = resp;
		try {
			PrintWriter pw = new PrintWriter(temp);
			bw = new BufferedWriter(pw);
			bw.write(xml);
			bw.flush();

			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = domFactory.newDocumentBuilder();
			doc = docBuilder.parse(temp);

			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	/**
	 * Reads xml file for specific values
	 */
	public static synchronized NodeList executeXPathQuery(String xp, Document doc) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		/** Validate Data From XML */
		domFactory.setNamespaceAware(true);
		XPath xpath = XPathFactory.newInstance().newXPath();
		/** XPath Query for showing all nodes value */
		XPathExpression expr = null;
		try {
			expr = xpath.compile(xp);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		/** Retrieve the result */
		Object result = null;
		try {
			result = expr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		NodeList nodes = (NodeList) result;
		
		return nodes;
	}
	
	public static synchronized String getNodeValue(String response, String xpath, String resonseFile) throws IOException {
		Document document = convertResponseToDOM(response, resonseFile);
		NodeList nodes = executeXPathQuery(xpath, document);
		if(nodes.item(0) == null){
			return null;
		}
		return nodes.item(0).getTextContent();
	}

}
