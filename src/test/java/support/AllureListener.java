package support;

import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Allure listener untuk enhanced reporting
 * - Log test start/end dengan timestamps
 * - Capture context information
 * - Add status indicators
 */
public class AllureListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("TEST START: " + result.getName());
        System.out.println("Method: " + result.getMethod().getMethodName());
        System.out.println("Description: " + result.getMethod().getDescription());
        System.out.println("Time: " + new java.util.Date());
        System.out.println("=".repeat(80));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.println("\n✓ TEST PASSED: " + result.getName() + " (" + duration + "ms)");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.println("\n✗ TEST FAILED: " + result.getName() + " (" + duration + "ms)");
        if (result.getThrowable() != null) {
            System.out.println("Error: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("\n⊘ TEST SKIPPED: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("\n⚠ TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n" + "#".repeat(80));
        System.out.println("SUITE START: " + context.getName());
        System.out.println("Total tests: " + context.getAllTestMethods().length);
        System.out.println("#".repeat(80));
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        
        System.out.println("\n" + "#".repeat(80));
        System.out.println("SUITE FINISH: " + context.getName());
        System.out.println("Results: " + passed + " passed, " + failed + " failed, " + skipped + " skipped");
        System.out.println("Duration: " + ((context.getEndDate().getTime() - context.getStartDate().getTime()) / 1000.0) + " seconds");
        System.out.println("#".repeat(80) + "\n");
    }

    @Step("Execute step: {stepName}")
    public void logStep(String stepName) {
        // This method is only for Allure annotation
    }
}
