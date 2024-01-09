package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ResourceBundle;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    ResourceBundle resourceBundle = ResourceBundle.getBundle("config.baseConfig");
    String baseURL = resourceBundle.getString("url");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }
}
