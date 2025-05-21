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
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TodoMVCProjectPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public TodoMVCProjectPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }
    public void open() {
        driver.get("https://todomvc.com/examples/react/dist/");
    }
    public void addItem(String text) {
        WebElement inputBox = driver.findElement(By.id("todo-input"));
        inputBox.sendKeys(text);
        inputBox.sendKeys(Keys.ENTER);
    }

}
