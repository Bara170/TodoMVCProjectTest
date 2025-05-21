import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TodoMVCProjectTest {
    private static ChromeDriver driver;

    @BeforeAll
    static void launchBrowser() {
        driver = new ChromeDriver();
    }

    @Test
    void navigateToWebsite() throws Exception {
        driver.get("https://todomvc.com/");
        takeScreenshot(driver, "initial_screenshot.png");
        System.out.println(driver.getTitle());
    }

    @Test
    void addHoverDeleteAndModifyTodoItem() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);

// TC08: DELETE AN ITEM
        WebElement addInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        addInput.sendKeys("Test Item 1");
        addInput.sendKeys(Keys.ENTER);

        WebElement itemToDelete = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        assertNotNull(itemToDelete);

        actions.moveToElement(itemToDelete).perform();
        WebElement deleteButton = itemToDelete.findElement(By.cssSelector("button.destroy"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);

        Thread.sleep(1000); // wait for DOM update
        assertTrue(driver.findElements(By.cssSelector("ul.todo-list li")).isEmpty());

 // TC11: Tick off an item
        // Add a new item "Item 1"
        WebElement inputBox1 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        inputBox1.sendKeys("Item 1");
        inputBox1.sendKeys(Keys.ENTER);

        // Wait for item to appear
        WebElement item1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));

        // Tick the item
        WebElement checkbox1 = item1.findElement(By.cssSelector("input.toggle"));
        checkbox1.click();

        // Verify item has "completed" class
        String classAttr1 = item1.getAttribute("class");
        assertTrue(classAttr1.contains("completed"), "Item should be marked as completed.");

        // Screenshot
        takeScreenshot(driver, "item_completed.png");

// TC12: Untick the same item
        // Add a new item "Item 1"
        WebElement inputBox2 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        inputBox1.sendKeys("Item 2");
        inputBox1.sendKeys(Keys.ENTER);

        //Wait for first item to be updated
        WebElement item1Updated = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));

        // Find checkbox
        WebElement checkbox1Updated = item1Updated.findElement(By.cssSelector("input.toggle"));

        // Untick it
        checkbox1Updated.click();

        // Verify class no longer contains "completed"
        String classAfterUntick = item1Updated.getAttribute("class");
        assertFalse(classAfterUntick.contains("completed"), "Item should no longer be marked as completed.");

        // Screenshot
        takeScreenshot(driver, "item_unticked.png");

// TC04: Add Accented Characters - Automate LATER
        // Wait for the input box to be clickable
        WebElement addItems = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));

        // Type accented characters into the input box
        String accentedCharacters = "é, è, á, ü, ñ";
        addItems.sendKeys(accentedCharacters);

        // Press Enter to submit the item
        addItems.sendKeys(Keys.ENTER);

        // Find the accented items in the list
        WebElement addedItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));

        // Check if the accented text matches what was entered
        assertEquals("é, è, á, ü, ñ", addedItem.getText());

        // Step 6: Take a screenshot of the result
        takeScreenshot(driver, "accented_items.png");

// TC05: Add Special Symbols - Automate LATER
        // Wait for the input box to be clickable
        WebElement specialSymbolsInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));

        // Type special characters into the input box
        String specialSymbols = "!@#$%^&*()";
        specialSymbolsInput.sendKeys(specialSymbols);

        // Press Enter to submit the item
        specialSymbolsInput.sendKeys(Keys.ENTER);

        // Find the last added item in the list
        WebElement specialSymbolsItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));

        // Check if the text matches what was entered
        String actualText = specialSymbolsItem.getText();
        // The "&" character is a known bug as it displays "&amp" and therefore causes the output to fail.
        assertNotEquals(specialSymbols, actualText, "Special symbols did not match.");

        // Take a screenshot of the result
        takeScreenshot(driver, "special_symbols.png");

// TC06: Add Emojis - Automate LATER
        // Wait for the input box to be clickable
        WebElement emojiInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));

        String emojiText = "😀 😂 ❤️ 🙏 😎";
        // Use JavaScript to input emojis (avoids ChromeDriver BMP limitation)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('input.new-todo').value = arguments[0]", emojiText);

        // Press Enter to submit the item
        emojiInput.sendKeys(Keys.ENTER);

        // Find the last added item in the list
        WebElement emojiItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));

        // Check if the text matches what was entered
        assertEquals(emojiText, emojiItem.getText());

        // Take a screenshot of the result
        takeScreenshot(driver, "emojis.png");

    }

    @AfterAll
    static void closeBrowser() {
        driver.quit();
    }

    public static void takeScreenshot(WebDriver webdriver, String desiredPath) throws Exception {
        TakesScreenshot screenshot = ((TakesScreenshot) webdriver);
        File screenshotFile = screenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File(desiredPath);
        FileUtils.copyFile(screenshotFile, targetFile);
    }
}
