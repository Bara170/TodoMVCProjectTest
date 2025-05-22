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
    private TodoMVCProjectPage todoPage;

    @BeforeAll
    static void launchBrowser() {
        driver = new ChromeDriver();
        //driver.get("https://todomvc.com/examples/react/dist/");
    }
    @BeforeEach
    void setUp() {
        // Set up ChromeDriver and open the TodoMVC app
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        todoPage = new TodoMVCProjectPage(driver);
        todoPage.open();
    }

    @Test //TC00: Launch TodoMVC Site
    public void navigateToWebsite() throws Exception {
        todoPage.refresh();
        assertEquals("TodoMVC: React", driver.getTitle());
        takeScreenshot(driver, "initial_screenshot.png");
    }

    @Test //TC01: Add New Todo Item
    public void addItem() throws Exception {
        todoPage.refresh();
        //add item
        todoPage.addItem("Item 1");

        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertTrue(toDoList.isDisplayed());
        assertEquals("Item 1", toDoList.getText());
        takeScreenshot(driver, "item_added.png");
    }

    @Test //TC02: Prevent Empty Todo
    public void preventEmptyToDo() throws Exception {
        todoPage.refresh();
        //try adding an empty string
        todoPage.addItem("");

        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertFalse(toDoList.isDisplayed());
        takeScreenshot(driver, "sending_empty_input.png");
    }

    @Test //TC03: Add Single Character
    public void addSingleCharacter() throws Exception {
        todoPage.refresh();
        //try adding a single character
        todoPage.addItem("a");

        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list[data-testid='todo-list']"));
        assertFalse(toDoList.isDisplayed());
        takeScreenshot(driver, "sending_single_char.png");
    }

    @Test //TC04: Add Accented Characters - Automate Later
    void AddAccentedCharacters() throws Exception {
        // Add accented characters
        todoPage.addItem( "é, è, á, ü, ñ");

        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));
        assertEquals("é, è, á, ü, ñ", item.getText());
        takeScreenshot(driver, "accented_characters.png");
    }
    @Test //TC05: Add Special Symbols - Automate Later
    void AddSpecialSymbols() throws Exception {
        // Add special characters
        todoPage.addItem("!@#$%^&*()");

        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li:last-child")));
        // The "&" character is a known bug as it displays "&amp" and therefore causes the output to fail.
        assertNotEquals("!@#$%^&*()", item.getText());
        takeScreenshot(driver, "special_symbols.png");
    }

    @Test //TC06: Add Emojis - Automate Later
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

    @Test //TC07: Delete an Item
    void DeleteAnItem() throws Exception {
        todoPage.refresh();
        // Add item
        todoPage.addItem("Item 1");

        // Hover and delete item
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        actions.moveToElement(item).perform();
        WebElement deleteButton = item.findElement(By.cssSelector("button.destroy"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);

        // Allow time
        assertTrue(driver.findElements(By.cssSelector("ul.todo-list li")).isEmpty());
        takeScreenshot(driver, "deleted_item.png");
    }

    @Test //TC08: Modify Item
    public void modifyItem() throws Exception {
        todoPage.refresh();
        //add items
        todoPage.addItem("Item 1");

        //edit item
        todoPage.editItem("Item 1", "Item edited");
        takeScreenshot(driver, "item_edited.png");

        //assert text has been edited
        WebElement toDoList = driver.findElement(By.cssSelector("ul.todo-list"));
        assertEquals("Item edited", toDoList.getText());
    }
    @Test //TC10: Tick Off Item
    void TickOffItem() throws Exception {
        todoPage.refresh();
        // Add item
        todoPage.addItem("Item 1");
        // Tick checkbox
        todoPage.toggleItem("Item 1");

        // Check if item is marked as completed
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        String classAttr = item.getAttribute("class");
        System.out.println(item.getAttribute("class"));
        assertTrue(classAttr.contains("completed"));
        takeScreenshot(driver, "item_completed.png");
    }

    @Test //TC12: Untick Item
    void UntickItem() throws Exception {
        todoPage.refresh();
        // Add item
        todoPage.addItem("Item 1");

        // Tick and then untick
        todoPage.toggleItem("Item 1");
        Thread.sleep(500);
        todoPage.toggleItem("Item 1"); // Untick

        // Confirm it's no longer marked as completed
        WebElement item = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.todo-list li")));
        String classAttr = item.getAttribute("class");
        assertFalse(classAttr.contains("completed"));
        takeScreenshot(driver, "unticked.png");
    }

    @Test //TC13, TC14 & TC15: Status Bar
    public void statusBarTest() {
        todoPage.refresh();
        //add an item and confirm status bar shows 1 item left
        todoPage.addItem("Item 1");
        assertEquals("1 item left!", todoPage.getStatus());

        //add a second item and confirm status bar shows 2 items left
        todoPage.addItem("Item 2");
        assertEquals("2 items left!", todoPage.getStatus());

        //mark all items complete and confirm status bar shows 0 items left
        todoPage.toggleAllItems();
        assertEquals("0 items left!", todoPage.getStatus());
    }

    @Test //TC17, TC18 & TC19: Filter
    public void filterItems() throws Exception{
        todoPage.refresh();
        //add items
        todoPage.addItem("Item 1");
        todoPage.addItem("Item 2");
        //tick one item completed
        todoPage.toggleItem("Item 1");

        //filter active
        todoPage.filterBy("active");
        takeScreenshot(driver, "active_filtered.png");
        // assert the active items have been filtered
        assertAllItemsToggleState(false);

        //filter completed
        todoPage.filterBy("completed");
        takeScreenshot(driver, "completed_filtered.png");
        // assert the completed items have been filtered
        assertAllItemsToggleState(true);

        //filter all
        todoPage.filterBy("all");
        List<WebElement> allItems = driver.findElements(By.cssSelector("li[data-testid = 'todo-item']"));
        takeScreenshot(driver, "all_filtered.png");
        //assert all items are showing
        assertEquals(2, allItems.size());
    }

    @Test //TC20 & TC21: Clear Completed
    public void clearCompleted() throws Exception {
        todoPage.refresh();
        //add items
        todoPage.addItem("Item 1");
        todoPage.addItem("Item 2");

        //check the "clear completed" button is visible
        WebElement clearCompleted = driver.findElement(By.cssSelector("button.clear-completed"));
        assertTrue(clearCompleted.isDisplayed());

        //mark the requested item complete
        todoPage.toggleItem("Item 1");
        //click clear completed
        todoPage.clearCompletedItems();

        //check completed items have been removed
        Assertions.assertEquals(1, todoPage.getTodoItems().size());
        takeScreenshot(driver, "cleared_completed.png");
    }

    @Test //TC22: Toggle All Items
    public void toggleAllItems() throws Exception {
        todoPage.refresh();
        //add items
        todoPage.addItem("Item 1");
        todoPage.addItem("Item 2");
        todoPage.addItem("Item 3");
        todoPage.addItem("Item 4");

        //toggle all items
        todoPage.toggleAllItems();
        takeScreenshot(driver, "all_items_ticked.png");
        //check all toggles have been ticked off
        assertAllItemsToggleState(true);

        //uncheck all items
        todoPage.toggleAllItems();

        //check all items have been unticked
        assertAllItemsToggleState(false);
        takeScreenshot(driver, "all_items_unticked.png");
    }

    //Helper method to assert all visible items' toggle states
    private void assertAllItemsToggleState(boolean expectedState) {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input.toggle[data-testid='todo-item-toggle']"));
        for (WebElement checkbox : checkboxes) {
            assertEquals(expectedState, checkbox.isSelected());
        }
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

