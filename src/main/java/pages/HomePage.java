package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends BasePage {

    public URL pageUrl = getPageUrl("home");
    @FindBy(xpath = "//div[@class='h2']/parent::div/preceding-sibling::span/img")
    private WebElement HeaderAppLogo;
    @FindBy(xpath = "//img[@alt='download']/ancestor::a")
    private WebElement HeaderDownloadButton;
    @FindBy(xpath = "//div[text()='Contact Us']/a")
    private WebElement ContactEmailLink;

    @FindBy(xpath = "//div[text()='Follow Shuru app on']/following-sibling::ul")
    List<WebElement> socialMediaLinks;

    /* Constructor */
    public HomePage(WebDriver driver) throws MalformedURLException {
        super(driver);
    }

    /* Page Class Methods */

    public WebElement getHeaderAppLogo() {
        return HeaderAppLogo;
    }

    public WebElement getHeaderDownloadButton() {
        return HeaderDownloadButton;
    }

    public WebElement getContactEmailLink() {
        return ContactEmailLink;
    }

    public HashMap<String, String> getFooterLinks(String sectionName) {
        HashMap<String, String> linkInfo = new HashMap<>();
        WebElement section = driver.findElement(By.xpath(String.format("//div[text()='%s']/following-sibling::div", sectionName)));
        List<WebElement> anchorTags = section.findElements(By.tagName("a"));
        anchorTags.forEach(link -> linkInfo.put(link.getText(), link.getAttribute("href")));
        return linkInfo;
    }

    public List<WebElement> getSocialMediaLinks() {
        return socialMediaLinks.stream()
                .flatMap(li -> li.findElements(By.tagName("a")).stream())
                .collect(Collectors.toList());
    }
}
