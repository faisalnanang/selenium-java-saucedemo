package support;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.testng.IConfigurationListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestFailureListener implements ITestListener, IAnnotationTransformer, IConfigurationListener {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, java.lang.reflect.Constructor testConstructor, java.lang.reflect.Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        captureFailureEvidence(result);
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        captureFailureEvidence(result);
    }

    private void captureFailureEvidence(ITestResult result) {
        Object instance = result.getInstance();
        if (!(instance instanceof BaseTest test)) return;
        if (test.driver == null) {
            String message = result.getThrowable() == null ? "WebDriver session was not created." : result.getThrowable().toString();
            Allure.addAttachment("Failure details", "text/plain", new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8)), ".txt");
            return;
        }
        WebDriver driver = test.driver;
        if (test.videoRecorder != null) {
            test.videoRecorder.stop();
            test.videoRecorder.attach();
        }
        if (driver instanceof TakesScreenshot screenshot) {
            try {
                Allure.addAttachment("Failure screenshot", "image/png", new ByteArrayInputStream(
                        screenshot.getScreenshotAs(OutputType.BYTES)), ".png");
            } catch (RuntimeException ignored) {
            }
        }
        try {
            Allure.addAttachment("Failure page source", "text/html",
                    new ByteArrayInputStream(driver.getPageSource().getBytes(StandardCharsets.UTF_8)), ".html");
        } catch (RuntimeException ignored) {
        }
        try {
            Allure.addAttachment("Failure URL", "text/plain",
                    new ByteArrayInputStream(driver.getCurrentUrl().getBytes(StandardCharsets.UTF_8)), ".txt");
        } catch (RuntimeException ignored) {
        }
        try {
            List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
            String trace = entries.stream().map(LogEntry::toJson).map(Object::toString).reduce("", (left, right) -> left + right + System.lineSeparator());
            Allure.addAttachment("Browser performance trace", "application/json",
                    new ByteArrayInputStream(trace.getBytes(StandardCharsets.UTF_8)), ".json");
        } catch (RuntimeException ignored) {
            // Browser does not expose performance logs.
        }
    }
}