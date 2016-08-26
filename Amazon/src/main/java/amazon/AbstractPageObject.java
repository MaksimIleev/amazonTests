package amazon;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * @author Maksim Ileev
 * Abstract Page Object: consists of generic methods used by 'page object'
 * @param <T>
 */
public abstract class AbstractPageObject<T extends CustomLoadableComponent<T>> extends CustomLoadableComponent<T> {

	private WebDriver driver;
	private static final String BASE_URL = PropertyUtils.getProperty("base.url");//TODO add base.url to properties
	
	public AbstractPageObject(WebDriver driver) {
		// assign driver
		this.driver = driver;
		// initialize the webelements on the page
		PageFactory.initElements(this.driver, this);
	}
	
	/**
	 * ensures to load the specific page, based on the class provided
	 * @param loadClass
	 * @return the object of loaded class
	 */
	public T loadPage(Class<T> loadClass) {
		T page = PageFactory.initElements(driver, loadClass);
		driver.get("https://" + BASE_URL + getPageURI());
		return page.get();
	}
	
	public abstract String getPageURI();
	public abstract void waitForPageToLoad();
	
	public void loadPage(String url) {
		driver.get(url);
	}
	
	/**
	 * Generic method for searching element on page, By locator
	 * @param locator
	 * @return element
	 */
	public WebElement findElement(By locator) {
		WebElement element = null;
		try {
			element = driver.findElement(locator);
		} catch(Exception e) {
			System.err.println("Cannot find the element by locator: " + locator.toString() + "\nException: " + e.getMessage());
		}
		
		return element;
	}
	
	/**
	 * Searches for elements on the page by locator
	 * and returns the them
	 * @param locator e.g By.xpath("//div")
	 * @return List<WebElement>
	 */
	public List<WebElement> findElements(By locator) {
		List<WebElement> list = null;
		try {
			list = driver.findElements(locator);
		} catch(Exception e) {
			System.err.println("Cannot find the elements by locator: " + locator.toString() + "\nException: " + e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * Navigates back from the current page, 
	 * analog of clicking back button in browser
	 */
	public void navigateBack() {
		driver.navigate().back();
	}
	/**
	 * @param input
	 * @param text
	 */
	public void type(WebElement input, String text) {
		input.sendKeys(text);
	}
	
	/**
	 * searches for element then sends the text to it
	 * @param inputLocator
	 * @param text
	 */
	public void type(By inputLocator, String text) {
		((WebElement) findElement(inputLocator)).sendKeys(text);
	}
	
	/**
	 * searches for element by locator then clicks it
	 * @param locator
	 */
	public void click(By locator) {
		((WebElement) findElement(locator)).click();
	}
	
	/**
	 * Clicks on element
	 * @param element
	 */
	public void click(WebElement element) {
		element.click();
	}
	
	/**
	 * Clears the text from element
	 * @param elementToClear
	 */
	public void clear(WebElement elementToClear) {
		if(elementToClear.getText() != null && elementToClear.getText() != "") {
			elementToClear.clear();
		}
	};
	
	/**
	 * Checks if the element is displayed on the page
	 * @param element
	 * @return boolean
	 */
	public boolean isElementDisplayed(WebElement element) {
		try {
			return element.isDisplayed();
		} catch(NoSuchElementException e) {
			return false;
		}
	}
	
	/**
	 * Finds the first visible element then returns it
	 * e.g. selector finds many elements with one locator, but only one is visible
	 * we can iterate through them and check if it is visible, if true then return the first one
	 * 
	 * @param list
	 * @return first element displayed
	 */
	public WebElement returnVisibleElement(List<WebElement> list) {
		for(WebElement el: list) {
			if(isElementDisplayed(el)) {
				return el;
			}
		}
		
		return null;
	}
	
	//TODO add more common methods here
}
