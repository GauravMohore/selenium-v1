package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) throws MalformedURLException {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public BasePage(WebDriver driver, WebDriverWait wait) throws MalformedURLException {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    protected URL getPageUrl(String pageName) throws MalformedURLException {
        String pageUrl = ResourceBundle.getBundle("config.url-config").getString(pageName);
        return new URL(pageUrl);
    }
}
