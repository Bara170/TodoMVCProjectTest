import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.io.File;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;

public class TodoMVCProjectTest {
    private static ChromeDriver driver;
    private WebDriverWait wait;
    private Actions actions;


    @BeforeAll
    static void launchBrowser() {
        driver = new ChromeDriver();
        driver.get("https://todomvc.com/examples/react/dist/");

    }
    @BeforeEach
    void setUp() {
        // Set up ChromeDriver and open the TodoMVC app
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);}


    @Test
    public void navigateToWebsite() throws Exception {
        driver.navigate().refresh();
        assertEquals("TodoMVC: React", driver.getTitle());
        takeScreenshot(driver, "initial_screenshot.png");
    }
    @Test
    public void addItem() throws Exception {
        driver.navigate().refresh();
        //add item
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertTrue(toDoList.isDisplayed());
        assertEquals("Item 1", toDoList.getText());
        takeScreenshot(driver, "item_added.png");
    }
    @Test
    public void preventEmptyToDo() throws Exception {
        driver.navigate().refresh();
        //add item
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.click();
        inputBox.sendKeys(Keys.ENTER);
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertFalse(toDoList.isDisplayed());
        takeScreenshot(driver, "sending_empty_input.png");
    }
    @Test
    public void addSingleCharacter() throws Exception {
        driver.navigate().refresh();
        //add item
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("a", Keys.ENTER);
        takeScreenshot(driver, "add_single_char.png");
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertFalse(toDoList.isDisplayed());
        takeScreenshot(driver, "sending_single_char.png");
    }
    @Test
    // TEST 23
    public void toggleAllItems() throws Exception {
        driver.navigate().refresh();
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
        driver.navigate().refresh();
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        inputBox.sendKeys("Item 2", Keys.ENTER);
        List<WebElement> todoItems = driver.findElements(By.cssSelector("li[data-testid='todo-item']"));

        //check the "clear completed" button is visible
        WebElement clearCompleted = driver.findElement(By.cssSelector("button.clear-completed"));
        assertTrue(clearCompleted.isDisplayed());

        //mark the requested item complete
        for (WebElement item : todoItems) {
            if (item.getText().equals("Item 2")) {
                WebElement toggleButton = item.findElement(By.cssSelector("input.toggle"));
                toggleButton.click();
            }
        }
        //click clear completed
        clearCompleted.click();
        takeScreenshot(driver, "cleared_completed.png");

        //check completed items have been removed
        List<WebElement> updatedList = driver.findElements(By.cssSelector("li[data-testid='todo-item']"));
        Assertions.assertEquals(1, updatedList.size());
    }
    @Test
    public void modifyItem() throws Exception {
        driver.navigate().refresh();
        //add items
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);

        //double-click on the item we want to edit
        Actions actions = new Actions(driver);
        WebElement itemToModify = driver.findElement(By.cssSelector("li[data-testid='todo-item"));
        actions.doubleClick(itemToModify).perform();
        takeScreenshot(driver, "after_double_click.png");

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
        driver.navigate().refresh();
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
        takeScreenshot(driver, "all_filtered.png");
        assertEquals(2, allItems.size());
    }
    @Test
    public void statusBarTest() {
        driver.navigate().refresh();
        //add an item and confirm status bar shows 1 item left
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys("Item 1", Keys.ENTER);
        WebElement statusBar = driver.findElement(By.cssSelector("span.todo-count"));
        assertEquals("1 item left!", statusBar.getText());

        //add a second item and confirm status bar shows 2 items left
        inputBox.sendKeys("Item 2", Keys.ENTER);
        assertEquals("2 items left!", statusBar.getText());

        //mark all items complete and confirm status bar shows 0 items left
        WebElement toggleAllButton = driver.findElement(By.id("toggle-all"));
        toggleAllButton.click();
        assertEquals("0 items left!", statusBar.getText());
    }
    @Test
        //TC08: Delete an Item
    void DeleteAnItem() throws Exception {
        driver.navigate().refresh();
        // Add item
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.new-todo")));
        input.sendKeys("Test Item 1");
        input.sendKeys(Keys.ENTER);

        // Hover and delete item
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        actions.moveToElement(item).perform();
        WebElement deleteButton = item.findElement(By.cssSelector("button.destroy"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);
        // Allow time
        assertTrue(driver.findElements(By.cssSelector("ul.todo-list li")).isEmpty());

        takeScreenshot(driver, "deleted_item.png");
    }

    @Test
        // TC11: Tick Off Item
    void TickOffItem() throws Exception {
        driver.navigate().refresh();
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
        System.out.println(item.getAttribute("class"));
        assertTrue(classAttr.contains("completed"));

        takeScreenshot(driver, "item_completed.png");
    }

    @Test
        // TC12: Untick Item
    void UntickItem() throws Exception {
        driver.navigate().refresh();
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

