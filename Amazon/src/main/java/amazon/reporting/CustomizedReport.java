package amazon.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

public class CustomizedReport extends TestListenerAdapter implements IReporter {

	private PrintWriter mOut;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		new File(outputDirectory).mkdirs();
		try {
			mOut = new PrintWriter(new BufferedWriter(
					new FileWriter(new File(System.getProperty("user.dir") + "/test-output", "custom-report.html"))));
		} catch (IOException e) {
			System.out.println("Error in creating writer: " + e);
		}
		startHtml();
		print("<div class=\"row\">");
		print("<div class=\"col-md-12\">");
		for (ISuite suite : suites) {
			print("<div class=\"panel panel-default\">");
			print("<div class=\"panel-heading\">" + suite.getName() + "</div>");
			print("<div class=\"panel-body\">");
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (String testName : suiteResults.keySet()) {

				print("<div class=\"panel panel-default\">");
				print("<div class=\"panel-heading\">" + testName + "</div>");
				print("<table class=\"table\">");
				print("<thead>");
				print("<tr>");

				ISuiteResult suiteResult = suiteResults.get(testName);
				ITestContext testContext = suiteResult.getTestContext();

				// Passed Tests Size
				IResultMap passResult = testContext.getPassedTests();
				Set<ITestResult> testsPassed = passResult.getAllResults();
				print("<th id=\"passed\"><h4>Total Passed Tests: " + testsPassed.size() + "</h4></th>");

				// Failed Tests Size
				IResultMap failedResult = testContext.getFailedTests();
				Set<ITestResult> testsFailed = failedResult.getAllResults();
				print("<th id=\"failed\"><h4>Total Failed Tests: " + testsFailed.size() + "</h4></th>");

				// Skipped Tests Size
				IResultMap skippedResult = testContext.getSkippedTests();
				Set<ITestResult> testsSkipped = skippedResult.getAllResults();
				print("<th id=\"skipped\"><h4>Total Skipped Tests: " + testsSkipped.size() + "</h4></th>");
				print("</tr>");
				print("</thead>");
				print("<tbody>");
				print("<tr>");

				// passed test names
				print("<td>");
				print("<table class=\"table\"><thead><tr>");
				print("<th>Test Name</th><th>Time(sec)</th>");
				print("</tr></thead>");
				print("<tbody><tr>");

				for (ITestResult passed : testsPassed) {
					String parameters = "";
					for (Object obj : passed.getParameters()) {
						parameters += obj.toString();
					}
					print("<tr><td>" + passed.getInstanceName() + "(" + parameters + ")" + "</td>" + "<td>"
							+ ((passed.getEndMillis() - passed.getStartMillis()) / 1000.0) + "</td></tr>");
				}

				print("</tr>");
				print("</tbody>");
				print("</table>");
				print("</td>");

				// failed test names
				print("<td>");
				print("<table class=\"table\"><thead><tr>");
				print("<th>Test Name</th><th>Exception</th>");
				print("</tr></thead>");
				print("<tbody><tr>");

				for (ITestResult failed : testsFailed) {
					String parameters = "";
					for (Object obj : failed.getParameters()) {
						parameters += obj.toString();
					}

					String description = "";
					if (failed.getMethod().getDescription() != null) {
						description = failed.getMethod().getDescription();
					}

					print("<tr><td>" + failed.getInstanceName() + "(" + parameters + ")" + "</td>"
							+ "<td><span class=\"plus glyphicon glyphicon-plus\"></span><span id=\"exceptionDescription\" style=\"display: none;\"> "
							+ description + "</br>" + failed.getThrowable().getMessage().replaceAll("\n", "")
									.replaceAll("\t", "").replaceAll("\r", "")
							+ "</span></td></tr>");
				}

				print("</tr>");
				print("</tbody>");
				print("</table>");
				print("</td>");

				// skipped test names
				print("<td>");
				print("<table class=\"table\"><thead><tr>");
				print("<th>Test Name</th>");
				print("</tr></thead>");
				print("<tbody><tr>");

				for (ITestResult skipped : testsSkipped) {
					print("<tr><td>" + skipped.getName() + "</td></tr>");
				}

				print("</tr>");
				print("</tbody>");
				print("</table>");
				print("</td>");

				print("</tr>");
				print("</tbody>");
				print("</table>");
				print("</div><!--/.panel-body-->");
				print("</div><!--/.panel-->");
			}
		}
		
		print("</div><!--/.panel-body-->");
		print("</div><!--/.panel-->");

		print("</div><!--/.col-->");
		print("</div><!--/.row-->");
		endHtml();
		mOut.flush();
		mOut.close();
	}

	private void print(String text) {
		System.out.println(text);
		mOut.println(text + "");
	}

	private void startHtml() {
		mOut.println("<!DOCTYPE html>");
		mOut.println("<html>");
		mOut.println("<head>");
		mOut.println("<title>TestNG Html Report</title>");
		mOut.println(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">");
		mOut.println("<link rel=\"stylesheet\" href=\"../test-output/testng.css\">");
		mOut.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>");
		mOut.println(
				"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>");
		mOut.println("<script src=\"https://d3js.org/d3.v4.min.js\"></script>");
		mOut.println("</head>");
		mOut.println("<body>");

	}

	private void endHtml() {
		mOut.println("<script type=\"text/javascript\">" + "$(document).ready(function(){"
				+ "$(document).on('click','span.plus',function(event){"
				+ "jQuery($(this).siblings('span#exceptionDescription')).show();"
				+ "jQuery($(this)).toggleClass(\"glyphicon-plus glyphicon-minus\");"
				+ "jQuery($(this)).toggleClass(\"plus minus\");" + "});"
				+ "$(document).on('click','span.minus',function(event){"
				+ "jQuery($(this).siblings('span#exceptionDescription')).hide();"
				+ "jQuery($(this)).toggleClass(\"glyphicon-minus glyphicon-plus\");"
				+ "jQuery($(this)).toggleClass(\"minus plus\");"

				+ "});" + "});</script>");
		mOut.println("</body>");
		mOut.println("</html>");
	}

}
