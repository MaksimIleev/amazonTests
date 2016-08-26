package test.apis;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import amazon.TestManager;
import amazon.restful.CallFactory;
import amazon.restful.RequestMethod;
import amazon.restful.ItemSearch.ItemSearchRequestAWS;
import aws.Errors;
import aws.ItemSearchResponse;
import aws.Arguments.Argument;

public class ItemSearchTest {
	
	@DataProvider
	public Object[][] requestMethodProvider() {
		return new Object[][] {
			{RequestMethod.GET},
			{RequestMethod.POST}
		};
	}
	
	@Test(dataProvider = "requestMethodProvider")
	public void verifyItemsSize(RequestMethod requestMethod) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        
        List<String> keywords = new ArrayList<String>();
        keywords.add("PRO EVOLUTION SOCCER");
        
        List<String> responseGroups = new ArrayList<String>();
        responseGroups.add("Images");
        responseGroups.add("ItemAttributes");
        responseGroups.add("Offers");
        
        Map<String, Object>responseMap = CallFactory.call(
        		new ItemSearchRequestAWS.ItemSearchRequestBuilder(requestMethod)
        		.addAssociateTag("inspirethel01-20")
        		.addSearchIndex("VideoGames")
        		.addKeywords(keywords)
        		.addResponseGroup(responseGroups)
        		.addSort("-price")
        		.build()
        );
        
        assertEquals(responseMap.get("responseCode"), 200);
       
       ItemSearchResponse itemSearch = null;
       try {

       itemSearch = (ItemSearchResponse) JAXB.unmarshal(new StringReader((String) responseMap.get("response")), ItemSearchResponse.class);
       
       } catch(Exception e) {
    	   e.printStackTrace();  	   
       }
       
       AssertJUnit.assertTrue("List<Item> is empty", itemSearch.getItems().get(0).getItem().size() > 0);
       
	}
	
	@Test(dataProvider = "requestMethodProvider")
	public void verifyItemKeywords(RequestMethod requestMethod) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        
        List<String> keywords = new ArrayList<String>();
        keywords.add("PRO EVOLUTION SOCCER");
        
        List<String> responseGroups = new ArrayList<String>();
        responseGroups.add("Images");
        responseGroups.add("ItemAttributes");
        responseGroups.add("Offers");
        
       Map<String, Object>responseMap = CallFactory.call(
        		new ItemSearchRequestAWS.ItemSearchRequestBuilder(requestMethod)
        		.addAssociateTag("inspirethel01-20")
        		.addSearchIndex("VideoGames")
        		.addKeywords(keywords)
        		.addResponseGroup(responseGroups)
        		.addSort("-price")
        		.build()
        );
       
       assertEquals(responseMap.get("responseCode"), 200);
       
       ItemSearchResponse itemSearch = null;
       try {

       itemSearch = (ItemSearchResponse) JAXB.unmarshal(new StringReader((String) responseMap.get("response")), ItemSearchResponse.class);
       
       } catch(Exception e) {
    	   e.printStackTrace();  	   
       }
       
       AssertJUnit.assertTrue("List<Argument> is null", itemSearch.getOperationRequest().getArguments().getArgument() != null);
       List<Argument> argumentList = itemSearch.getOperationRequest().getArguments().getArgument();
       Argument argument = null;
       for(Argument arg: argumentList) {
    	  if(arg.getName().equalsIgnoreCase("Keywords")) {
    		  AssertJUnit.assertTrue(arg.getValue().equals("PRO EVOLUTION SOCCER"));
    	      argument = arg;
    	  }
       }
       AssertJUnit.assertTrue(argument.getValue().equals("PRO EVOLUTION SOCCER"));
	}
	
	@Test(dataProvider = "requestMethodProvider")
	public void verifyItemAssociateTag(RequestMethod requestMethod) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
        
        List<String> keywords = new ArrayList<String>();
        keywords.add("OVERWATCH");
        
        List<String> responseGroups = new ArrayList<String>();
        responseGroups.add("Images");
        responseGroups.add("ItemAttributes");
        responseGroups.add("Offers");
        
        Map<String, Object>responseMap = CallFactory.call(
        		new ItemSearchRequestAWS.ItemSearchRequestBuilder(requestMethod)
        		.addAssociateTag("inspirethel01-20")
        		.addSearchIndex("VideoGames")
        		.addKeywords(keywords)
        		.addResponseGroup(responseGroups)
        		.addSort("-price")
        		.build()
        );
        
        assertEquals(responseMap.get("responseCode"), 200);
       
       ItemSearchResponse itemSearch = null;
       try {

       itemSearch = (ItemSearchResponse) JAXB.unmarshal(new StringReader((String) responseMap.get("response")), ItemSearchResponse.class);
       
       } catch(Exception e) {
    	   e.printStackTrace();  	   
       }
       
       AssertJUnit.assertTrue("List<Argument> is null", itemSearch.getOperationRequest().getArguments().getArgument() != null);
       List<Argument> argumentList = itemSearch.getOperationRequest().getArguments().getArgument();
       Argument argument = null;
       for(Argument arg: argumentList) {
    	  if( arg.getName().equalsIgnoreCase("AssociateTag")) {
    		  AssertJUnit.assertTrue(arg.getValue().equals("inspirethel01-20"));
    	      argument = arg;
    	  }
       }
       AssertJUnit.assertTrue(argument.getValue().equals("inspirethel01-20"));
	}
	
	
	@Test(dataProvider = "requestMethodProvider")
	public void verifyItemSearchIndex(RequestMethod requestMethod) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
		ItemSearchRequestAWS iteamSearchRequest = null;
        List<String> keywords = new ArrayList<String>();
        keywords.add("OVERWATCH");
        
        List<String> responseGroups = new ArrayList<String>();
        responseGroups.add("Images");
        responseGroups.add("ItemAttributes");
        responseGroups.add("Offers");
        
        iteamSearchRequest = new ItemSearchRequestAWS.ItemSearchRequestBuilder(requestMethod)
        		.addAssociateTag("inspirethel01-20")
        		.addSearchIndex("Games")
        		.addKeywords(keywords)
        		.addResponseGroup(responseGroups)
        		.addSort("-price")
        		.build();
        
        Map<String, Object>responseMap = CallFactory.call(iteamSearchRequest);
        assertEquals(responseMap.get("responseCode"), 200);
        
       ItemSearchResponse itemSearch = null;
       try {
    	   itemSearch = (ItemSearchResponse) JAXB.unmarshal(new StringReader((String) responseMap.get("response")), ItemSearchResponse.class);
       } catch(Exception e) {
    	   e.printStackTrace();  	   
       }
       
       // Assert no error(s)
       List<Errors.Error> errorsList = null;
       Map<Integer,Map<String, String>> map = new HashMap<Integer, Map<String, String>>();
       Map<String, String> errorMap = new HashMap<String, String>();
       if(itemSearch.getItems().get(0).getRequest().getErrors().getError() != null) {
    	   errorsList = itemSearch.getItems().get(0).getRequest().getErrors().getError();
    	   for(Errors.Error error: errorsList) {
    		   errorMap.put(error.getCode(), error.getMessage());
    	   }
       }
       
       Assert.assertTrue(errorsList == null, "\n<b>Request:</b></br> Method: " + iteamSearchRequest.getRequestMethod() + "</br>" + iteamSearchRequest.getRequestUrl() + "\n</br><b>Response:</b></br>" + (String) responseMap.get("response") + "\n</br><b>Errors:</b></br>"+ errorMap.toString());
       
       AssertJUnit.assertTrue("List<Argument> is null", itemSearch.getOperationRequest().getArguments().getArgument() != null);
       List<Argument> argumentList = itemSearch.getOperationRequest().getArguments().getArgument();
       Argument argument = null;
       for(Argument arg: argumentList) {
    	  if( arg.getName().equalsIgnoreCase("SearchIndex")) {
    		  AssertJUnit.assertTrue(arg.getValue().equals("Games"));
    	      argument = arg;
    	  }
       }
       AssertJUnit.assertTrue(argument.getValue().equals("Games"));
	}
	
	public <T,X> void assertEquals(T actual, X expected) {
		AssertJUnit.assertEquals("\nExpected: " + expected + "\nActual: " + actual, actual, expected);
	}
}
