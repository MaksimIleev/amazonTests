package amazon;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

/**
 * @author Maksim Ileev
 * Abstract CustomLoadableComponent used by 'Page Object' to assure page loads properly.
 * Consists of generic 'wait' methods used by other components
 * @param <T>
 */
public abstract class CustomLoadableComponent<T extends CustomLoadableComponent<T>> {
	
	private static final int LOAD_TIMEOUT = 30;
	private static final int REFRESH_RATE = 2;
	
	public T get() {
		try {
			isLoaded();
			return (T)this;
		} catch(Error er) {
			System.err.println("Error during the load(): " + er.getMessage());
			load();
		}
		
		isLoaded();
		return(T)this;
	}
	
	/**
	 * Wait for page to load (FluentWait)
	 * @param driver
	 * @param pageLoadCondition
	 */
	protected void waitForPageToLoad(WebDriver driver, ExpectedCondition pageLoadCondition) {
		Wait wait = new FluentWait(driver)
				.withTimeout(LOAD_TIMEOUT, TimeUnit.SECONDS)
				.pollingEvery(REFRESH_RATE, TimeUnit.SECONDS);
		wait.until(pageLoadCondition);
	}
	
	//TODO add more wait implementations
	
	protected abstract void load();
	protected abstract void isLoaded() throws Error;

}
