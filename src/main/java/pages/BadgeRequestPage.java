package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BadgeRequestPage extends BasePage {
    public URL pageUrl = new URL(getPageUrl("home").toString() + "app/badge-request");

    @FindBy(xpath = "//img[@alt='badge-icon']/ancestor::button")
    private WebElement getBadgeButton;

    @FindBy(xpath = "//*[name()='svg'][@id='verified-icon']/parent::a")
    private WebElement getVerifiedTickLink;

    @FindBy(xpath = "//div[starts-with(@class,'ScrollableBadges')]/div/ul/li/div")
    private List<WebElement> scrollableBadges;

    public BadgeRequestPage(WebDriver driver) throws MalformedURLException {
        super(driver);
    }

    public WebElement getGetBadgeButton() {
        return getBadgeButton;
    }

    public WebElement getGetVerifiedTickLink() {
        return getVerifiedTickLink;
    }

    public List<WebElement> getScrollableBadges() {
        return scrollableBadges;
    }
}
