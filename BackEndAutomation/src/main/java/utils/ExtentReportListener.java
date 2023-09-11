package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest; 
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

 

public class ExtentReportListener implements ITestListener {
	static final Logger logger = LogManager.getLogger(ExtentReportListener.class.getName());
	 
	private static final String OUTPUT_FOLDER = "./ExtentReport/";
	private static final String FILE_NAME = "TestExecutionReport.html";

	private static ExtentReports extent = init();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();

	private static ExtentReports init() {

		Path path = Paths.get(OUTPUT_FOLDER); 
		 
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
		htmlReporter.config().setDocumentTitle("Automation Test Results");
		htmlReporter.config().setReportName("Automation Test Results");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setReportUsesManualConfiguration(true);

		return extent;
	}

	public synchronized void onStart(ITestContext context) {
		 
	}

	public synchronized void onFinish(ITestContext context) {
		 
		extent.flush();
		test.remove();
	}

	public synchronized void onTestStart(ITestResult result) { 
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);  
		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
		result.getMethod().getDescription());

		extentTest.assignCategory(result.getTestContext().getSuite().getName());
		 
		extentTest.assignCategory(className);
		test.set(extentTest);
		test.get().getModel().setStartTime(getTime(result.getStartMillis()));
	}

	public synchronized void onTestSuccess(ITestResult result) {
		  
		 
		test.get().pass(result.getMethod().getMethodName() + " Passed" );
		
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
		
		 
		 
	}

	public synchronized void onTestFailure(ITestResult result)   {
		System.out.println((result.getMethod().getMethodName() + " Failed!")); 
		test.get().fail(result.getMethod().getMethodName() + " Failed   " );
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
		test.get().fail(result.getThrowable());
		logger.info(result.getThrowable());
	}

	public synchronized void onTestSkipped(ITestResult result) {
		System.out.println((result.getMethod().getMethodName() + " skipped!"));
		 
	}

	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}
