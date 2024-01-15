package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.MalformedURLException;
import java.net.URL;

public class NewsPage extends BasePage {

    public URL pageUrl = getPageUrl("news");
    @FindBy(tagName = "h1")
    private WebElement ContentHeading;

    /* Constructor */
    public NewsPage(WebDriver driver) throws MalformedURLException {
        super(driver);
    }

    /* Page Methods */
    public String getContentHeading() {
        return ContentHeading.getText();
    }
}
