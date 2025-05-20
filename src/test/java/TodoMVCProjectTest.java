import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoMVCProjectTest {
    private static ChromeDriver driver;

    @BeforeAll
    static void launchBrowser() {
        driver = new ChromeDriver();
    }

    @Test
    void navigateToWebsite() throws Exception {
        WebDriver driver = new ChromeDriver();

        // Instruct the driver to browse to the TODOMVC website
        driver.get("https://todomvc.com/");


        // Take a screenshot of what's currently on the page,
        // and store it in a file 'initial_screenshot.png' in your project root
        takeScreenshot(driver, "initial_screenshot.png");

        // Find the title of the webpage (the value inside the HTML
        // <title> element) and print it to the terminal
        System.out.println(driver.getTitle());

    }

    // Helper function for taking screenshots using WebDriver
    public static void takeScreenshot(WebDriver webdriver,String desiredPath) throws Exception{
        TakesScreenshot screenshot = ((TakesScreenshot)webdriver);
        File screenshotFile = screenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File(desiredPath);
        FileUtils.copyFile(screenshotFile, targetFile);
    }
    @AfterAll
        static void closeBrowser() {
        driver.quit();
    }}

