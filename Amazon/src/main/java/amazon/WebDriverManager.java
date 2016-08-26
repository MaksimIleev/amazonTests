package amazon;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * @author Maksim Ileev
 *
 */
public class WebDriverManager extends Thread {

	private WebDriverManager() {}//prevent instantiation
	
	private static WebDriver firefoxDriver;
	private static WebDriver chromeDriver;
	private static WebDriver ieDriver;
	
	private static final ThreadLocal<WebDriver> firefoxDriverThread = new ThreadLocal<WebDriver>(){
		
		@Override
		protected WebDriver initialValue() {
			FirefoxProfile profile = new FirefoxProfile();
			profile.setEnableNativeEvents(true);
			return  new FirefoxDriver(profile);
		}
	};
	
	private static final ThreadLocal<WebDriver> chromeDriverThread = new ThreadLocal<WebDriver>(){
		
		@Override
		protected WebDriver initialValue() {
			File chrome = null;
			String src = System.getProperty("user.dir") + "\\src\\resource\\java\\chromedriver.exe";
			if(Utils.isWindows()) {
				chrome = new File(src);
			} else if(Utils.isMac()) {
				chrome = new File(src);
			} 
			System.setProperty("webdriver.chrome.driver", chrome.getAbsolutePath());
            return new ChromeDriver();
		}
	};
	
	private static final ThreadLocal<WebDriver> ieDriverThread = new ThreadLocal<WebDriver>(){
		
		@Override
		protected WebDriver initialValue() {
			File chrome = null;
			String src = System.getProperty("user.dir") + "\\src\\resource\\java\\IEDriverServer.exe";
			if(Utils.isWindows()) {
				chrome = new File(src);
			} else if(Utils.isMac()) {
				chrome = new File(src);
			} 
			System.setProperty("webdriver.ie.driver", chrome.getAbsolutePath());
			return new InternetExplorerDriver();
		}
	};

	public static WebDriver getInstance(Browser browserType) {
			
	switch (browserType) {
		case CHROME: {
			chromeDriver = chromeDriverThread.get();
			if(chromeDriver == null) {
				File chrome = null;
				String src = System.getProperty("user.dir") + "\\src\\resource\\java\\chromedriver.exe";
				if(Utils.isWindows()) {
					chrome = new File(src);
				} else if(Utils.isMac()) {
					chrome = new File(src);
				} 
				System.setProperty("webdriver.chrome.driver", chrome.getAbsolutePath());
				chromeDriver = new ChromeDriver();
                chromeDriverThread.set(chromeDriver);
				}
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					chromeDriver.close();
					chromeDriverThread.remove();
				}
			});
			
			return chromeDriverThread.get();
		}

		case FIREFOX: {
			firefoxDriver = firefoxDriverThread.get();
			if(firefoxDriver == null) {
				FirefoxProfile profile = new FirefoxProfile();
				profile.setEnableNativeEvents(true);
				firefoxDriver = new FirefoxDriver(profile);
				firefoxDriverThread.set(firefoxDriver);
			}
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					firefoxDriver.close();
					firefoxDriverThread.remove();
				}
			});
			
			return firefoxDriverThread.get();
		}

		case IE: {
			ieDriver = ieDriverThread.get();
			if(ieDriver == null) {
				File chrome = null;
				String src = System.getProperty("user.dir") + "\\src\\resource\\java\\IEDriverServer.exe";
				if(Utils.isWindows()) {
					chrome = new File(src);
				} else if(Utils.isMac()) {
					chrome = new File(src);
				} 
				System.setProperty("webdriver.ie.driver", chrome.getAbsolutePath());
				ieDriver = new InternetExplorerDriver();
				ieDriverThread.set(ieDriver);
			}
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					ieDriver.close();
					ieDriverThread.remove();
				}
			});
			
			return ieDriverThread.get();
		}

		default:
			throw new RuntimeException("Browser Type " + browserType + " not implemented!");
		}// end switch

	}	
	
}



