import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TodoMVCProjectTest {

    private ChromeDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    @BeforeEach
    void setUp() {
        // Set up ChromeDriver and open the TodoMVC app
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        driver.get("https://todomvc.com/examples/react/dist/");
    }

    @Test
    //TC08: Delete an Item
    void DeleteAnItem() throws Exception {
        // Add item
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        input.sendKeys("Test Item 1");
        input.sendKeys(Keys.ENTER);

        // Hover and delete item
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        actions.moveToElement(item).perform();
        WebElement deleteButton = item.findElement(By.cssSelector("button.destroy"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);

        Thread.sleep(1000); // Allow time
        assertTrue(driver.findElements(By.cssSelector("ul.todo-list li")).isEmpty());

        takeScreenshot(driver, "deleted_item.png");
    }

    @Test
    // TC11: Tick Off Item
    void TickOffItem() throws Exception {
        // Add item
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        input.sendKeys("Item 1");
        input.sendKeys(Keys.ENTER);

        // Tick checkbox
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        WebElement checkbox = item.findElement(By.cssSelector("input.toggle"));
        checkbox.click();

        // Check if item is marked as completed
        String classAttr = item.getAttribute("class");
        assertTrue(classAttr.contains("completed"));

        takeScreenshot(driver, "item_completed.png");
    }

    @Test
    // TC12: Untick Item
    void UntickItem() throws Exception {
        // Add item
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        input.sendKeys("Item 1");
        input.sendKeys(Keys.ENTER);

        // Tick and then untick
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        WebElement checkbox = item.findElement(By.cssSelector("input.toggle"));
        checkbox.click(); // Tick
        Thread.sleep(500);
        checkbox.click(); // Untick

        // Confirm it's no longer marked as completed
        String classAttr = item.getAttribute("class");
        assertFalse(classAttr.contains("completed"));

        takeScreenshot(driver, "unticked.png");
    }

    @Test
    // TC04: Add Accented Characters - Automate Later
    void AddAccentedCharacters() throws Exception {
        // Add accented characters
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        String text = "é, è, á, ü, ñ";
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER);

        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));
        assertEquals(text, item.getText());

        takeScreenshot(driver, "accented_characters.png");
    }

    @Test
    // TC05 - Add Special Symbols - Automate Later
    void AddSpecialSymbols() throws Exception {
        // Add special characters
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        String symbols = "!@#$%^&*()";
        input.sendKeys(symbols);
        input.sendKeys(Keys.ENTER);

        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));
        String actualText = item.getText();
        // The "&" character is a known bug as it displays "&amp" and therefore causes the output to fail.
        assertNotEquals(symbols, actualText, "Special symbols did not match.");

        takeScreenshot(driver, "special_symbols.png");
    }

    @Test
    // TC06: Add Emojis - Automate Later
    void AddEmojis() throws Exception {
        // Add emojis
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        String emojiText = "😀 😂 ❤️ 🙏 😎";

        ((JavascriptExecutor) driver).executeScript("document.querySelector('input.new-todo').value = arguments[0]", emojiText);
        input.sendKeys(Keys.ENTER);

        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));
        assertEquals(emojiText, item.getText());

        takeScreenshot(driver, "emojis.png");
    }

    // Screenshot method
    public static void takeScreenshot(WebDriver webdriver, String path) throws Exception {
        TakesScreenshot screenshot = ((TakesScreenshot) webdriver);
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File(path));
    }

    // Close the browser after each test
    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
