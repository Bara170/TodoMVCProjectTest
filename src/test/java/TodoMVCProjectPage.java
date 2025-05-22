import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class TodoMVCProjectPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public TodoMVCProjectPage(WebDriver driver) {
        this.driver = driver;
        this.wait = wait;
        this.actions = actions;
    }

    public void open() {
        driver.get("https://todomvc.com/examples/react/dist/");
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    public List<WebElement> getTodoItems() {
        return driver.findElements(By.cssSelector("li[data-testid='todo-item']"));
    }

    public void addItem(String itemText) {
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys(itemText);
        inputBox.sendKeys(Keys.ENTER);
    }
    public void toggleItem(String itemToToggle) {
        for (WebElement item : getTodoItems()) {
            if (item.getText().equals(itemToToggle)) {
                item.findElement(By.cssSelector("input.toggle")).click();
            }
        }
    }
    public void toggleAllItems() {
        WebElement toggleAllButton = driver.findElement(By.id("toggle-all"));
        toggleAllButton.click();
    }

    public void clearCompletedItems() {
        WebElement clearCompleted = driver.findElement(By.cssSelector("button.clear-completed"));
        clearCompleted.click();
    }

    public void filterBy(String filterText) {
        String href = switch (filterText.toLowerCase()) {
            case "active" -> "#/active";
            case "completed" -> "#/completed";
            default -> "#/";
        };
        driver.findElement(By.cssSelector("a[href='" + href + "']")).click();
    }

    public String getStatus() {
        WebElement statusBar = driver.findElement(By.cssSelector("span.todo-count"));
        return statusBar.getText();

    }
    public void editItem(String currentItem, String editedItem) {
        for (WebElement item : getTodoItems()) {
            if (item.getText().equals(currentItem)) {
                Actions actions = new Actions(driver);
                //WebElement itemToModify = driver.findElement(By.cssSelector("li[data-testid='todo-item"));
                actions.doubleClick(item).perform();
                WebElement editBox = item.findElement(By.cssSelector("input.new-todo"));
                editBox.sendKeys(Keys.chord(Keys.COMMAND, "a"));
                editBox.sendKeys(Keys.DELETE);
                editBox.sendKeys(editedItem, Keys.ENTER);
            }
        }


    }
}
