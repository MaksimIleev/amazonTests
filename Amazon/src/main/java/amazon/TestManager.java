package amazon;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;


public class TestManager implements ITestListener {
	
	private WebDriver driver = null;
	private Browser browserName;
	
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	public void setBrowserName(Browser browserName) {
		this.browserName = browserName;
	}
	
	
	@Override
	public void onTestFailure(ITestResult result) {

		this.driver = ((TestManager)result.getInstance()).driver;
        // create file and dirs to store for our test locally
        TakesScreenshot snapper = (TakesScreenshot)driver;

        File tempScreenshot = snapper.getScreenshotAs(OutputType.FILE);

        System.out.println(tempScreenshot.getAbsolutePath());

        File myScreenshotDirectory = new File(System.getProperty("user.dir") + "/" +PropertyUtils.getProperty("screenshot.dir"));
        myScreenshotDirectory.mkdirs();
        
        String parameters = "";
        if(result.getParameters() != null) {
        	for(Object obj: result.getParameters()) {
        		parameters = obj.toString();
        	}
        }

        File myScreenshot = new File(myScreenshotDirectory, result.getName() + "(" + parameters + ")_" + ((TestManager)result.getInstance()).browserName.toString() + ".png");
        if(myScreenshot.exists()) {
            FileUtils.deleteQuietly(myScreenshot);
        }

        try {
			FileUtils.moveFile(tempScreenshot, myScreenshot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Reporter.log("<a href='"+ myScreenshot.getAbsolutePath() + "'> <img src='" + myScreenshot.getAbsolutePath() + "' height='100' width='100'/> </a>");
        result.getMethod().setDescription("<a href='"+ myScreenshot.getAbsolutePath() + "'> <img src='" + myScreenshot.getAbsolutePath() + "' height='100' width='100'/> </a>");

	}
	
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
	
	private String getExtension(File fileWithExtension) {
        String fileName = fileWithExtension.getName();
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    private File createATempDirectoryForScreenshots() {
        String s = File.separator;
        String ourTestTempPathName = System.getProperty("user.dir") +
                String.format("%ssrc%stest%sresources%stemp%sscreenshots",s,s,s,s,s);

        File testTempDir = new File(ourTestTempPathName);
        if(testTempDir.exists()){
            if(!testTempDir.isDirectory()){
                System.err.println("Test path exists but is not a directory");
            }
        }else{
            testTempDir.mkdirs();
        }

        return testTempDir;
    }

}
