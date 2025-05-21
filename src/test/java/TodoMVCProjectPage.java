import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class TodoMVCProjectPage {
    private WebDriver driver;
    //private WebDriverWait wait;

    public TodoMVCProjectPage(WebDriver driver) {
        this.driver = driver;
        //this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
        return "status";

    }

    public void editItem(String currentItem, String editedItem) {

    }
}
