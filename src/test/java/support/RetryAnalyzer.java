package support;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int attempts;

    @Override
    public boolean retry(ITestResult result) {
        int retryCount = Integer.parseInt(System.getProperty("retryCount", "0"));
        return attempts++ < retryCount;
    }
}