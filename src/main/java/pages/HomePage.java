package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(xpath = "//div[@class='h2']/parent::div/preceding-sibling::span/img")
    private WebElement HeaderAppLogo;
    @FindBy(xpath = "//img[@alt='download']/ancestor::a")
    private WebElement HeaderDownloadButton;


    /* Constructor */
    public HomePage(WebDriver driver){
        super(driver);
    }

    /* Page Class Methods */

    public WebElement getHeaderAppLogo() {
        return HeaderAppLogo;
    }

    public WebElement getHeaderDownloadButton() {
        return HeaderDownloadButton;
    }
}
