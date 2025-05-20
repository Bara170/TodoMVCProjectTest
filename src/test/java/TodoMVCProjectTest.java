import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;

public class TodoMVCProjectTest {
    private static ChromeDriver driver;

    @BeforeAll
    static void launchBrowser() {
        driver = new ChromeDriver();
    }

    @Test
    public void navigateToWebsite() throws Exception {

        driver.get("https://todomvc.com/examples/react/dist/");

        // Take a screenshot of what's currently on the page,
        takeScreenshot(driver, "initial_screenshot.png");
    }
    @Test
    public void addItem() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add item
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertTrue(toDoList.isDisplayed());
        assertEquals("Item 1", toDoList.getText());
    }
    @Test
    public void preventEmptyToDo() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add item
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.click();
        inputBox.sendKeys(Keys.ENTER);
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertFalse(toDoList.isDisplayed());
    }
    @Test
    public void addSingleCharacter() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add item
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("a", Keys.ENTER);
        takeScreenshot(driver, "add_single_char.png");
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertFalse(toDoList.isDisplayed());
    }
    @Test
    // TEST 23
    public void toggleAllItems() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        inputBox.sendKeys("Item 2", Keys.ENTER);
        inputBox.sendKeys("Item 3", Keys.ENTER);
        inputBox.sendKeys("Item 4", Keys.ENTER);

        //toggle all by clicking down arrow
        WebElement toggleAllButton = driver.findElement(By.id("toggle-all"));
        toggleAllButton.click();
        takeScreenshot(driver, "all_items_ticked.png");

        //check all toggles have been ticked off
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input.toggle[data-testid='todo-item-toggle']"));
        for (WebElement checkbox : checkboxes) {
            assertTrue(checkbox.isSelected());
        }
        //uncheck all items
        toggleAllButton.click();
        takeScreenshot(driver, "all_items_unticked.png");
        //check all items have been unticked
        for (WebElement checkbox : checkboxes) {
            assertFalse(checkbox.isSelected());
        }
    }
    @Test
    //TEST 9
    public void clearCompleted() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        inputBox.sendKeys("Item 2", Keys.ENTER);

        WebElement toggleButton = driver.findElement(By.cssSelector("input.toggle[type='checkbox'][data-testid='todo-item-toggle']\n"));
        toggleButton.click();
        takeScreenshot(driver, "item_ticked.png");

        WebElement clearCompleted = driver.findElement(By.cssSelector("button.clear-completed"));
        clearCompleted.click();
        takeScreenshot(driver, "cleared_completed.png");





    }



    // Helper function for taking screenshots using WebDriver
    public static void takeScreenshot(WebDriver webdriver, String desiredPath) throws Exception {
        TakesScreenshot screenshot = ((TakesScreenshot) webdriver);
        File screenshotFile = screenshot.getScreenshotAs(OutputType.FILE);
        File targetFile = new File(desiredPath);
        FileUtils.copyFile(screenshotFile, targetFile);
    }

    @AfterAll
    static void closeBrowser() {
        driver.quit();
    }
}

