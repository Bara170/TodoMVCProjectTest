import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
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
    public void clearCompleted() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        inputBox.sendKeys("Item 2", Keys.ENTER);
        List<WebElement> todoItems = driver.findElements(By.cssSelector("li[data-testid='todo-item']"));

        //mark the requested item complete
        for (WebElement item : todoItems) {
            System.out.println(item.getText());
            if (item.getText().equals("Item 2")) {
                WebElement toggleButton = item.findElement(By.cssSelector("input.toggle"));
                toggleButton.click();
            }
        }
        //click clear completed
        WebElement clearCompleted = driver.findElement(By.cssSelector("button.clear-completed"));
        clearCompleted.click();
        takeScreenshot(driver, "cleared_completed.png");
        //check completed items have been removed
        List<WebElement> updatedList = driver.findElements(By.cssSelector("li[data-testid='todo-item']"));
        Assertions.assertEquals(updatedList.size(), 1);
    }
    @Test
    public void modifyItem() throws Exception {
        driver.get("https://todomvc.com/examples/react/dist/");
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);

        //double click on the item we want to edit
        Actions actions = new Actions(driver);
        WebElement itemToModify = driver.findElement(By.cssSelector("li[data-testid='todo-item"));
        actions.doubleClick(itemToModify).perform();
        takeScreenshot(driver, "After_double_click.png");

        //locate the edit box and edit text
        WebElement editBox = itemToModify.findElement(By.cssSelector("input.new-todo"));
        editBox.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        editBox.sendKeys(Keys.DELETE);
        editBox.sendKeys("Item edited", Keys.ENTER);
        takeScreenshot(driver, "item_edited.png");

        //assert text has been edited
    }
    @Test
    public void filterItems() throws Exception{
        driver.get("https://todomvc.com/examples/react/dist/");
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        inputBox.sendKeys("Item 2", Keys.ENTER);

        //locate the item in the list and tick completed
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement item1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        WebElement checkbox1 = item1.findElement(By.cssSelector("input.toggle"));
        checkbox1.click();

        //filter active
        WebElement filteredActive = driver.findElement(By.cssSelector("ul.todo-list"));
        WebElement showActive = driver.findElement(By.cssSelector("a[href=\"#/active\"] "));
        showActive.click();
        takeScreenshot(driver, "active_filtered.png");

        List<WebElement> checkboxesActive = filteredActive.findElements(By.cssSelector("input.toggle[data-testid='todo-item-toggle']"));
        for (WebElement checkbox : checkboxesActive) {
           assertFalse(checkbox.isSelected());
        }

        //filter completed
        WebElement filteredCompleted = driver.findElement(By.cssSelector("ul.todo-list"));
        WebElement showCompleted = driver.findElement(By.cssSelector("a[href=\"#/completed\"] "));
        showCompleted.click();
        takeScreenshot(driver, "completed_filtered.png");

        List<WebElement> checkboxesCompleted = filteredCompleted.findElements(By.cssSelector("input.toggle[data-testid='todo-item-toggle']"));
        for (WebElement checkbox : checkboxesCompleted) {
           assertTrue(checkbox.isSelected());
        }
        //filter all
        WebElement showAll = driver.findElement(By.cssSelector("a[href=\"#/\"] "));
        showAll.click();
        List<WebElement> allItems = driver.findElements(By.cssSelector("li[data-testid = 'todo-item']"));
        Thread.sleep(1000);
        takeScreenshot(driver, "all_filtered.png");

        assertEquals(2, allItems.size());
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

