package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Driver;
import java.sql.SQLRecoverableException;
import java.util.List;
import java.util.ResourceBundle;

public class ServicesPage extends BasePage {

    public URL pageUrl = getPageUrl("services");

    @FindBy(tagName = "h1")
    private List<WebElement> serviceNames;

    public ServicesPage(WebDriver driver) throws MalformedURLException {
        super(driver);
    }

    public List<String> getServiceNames() {
        return serviceNames.stream().map(WebElement::getText).toList();
    }

    public List<WebElement> getServiceLocationElements(String serviceName) {
        return driver.findElements(By.xpath(String.format("//h1[text()='%s']/following-sibling::ul/li/a", serviceName)));
    }

    public List<String> getServiceLocationNames(String serviceName) {
        return getServiceLocationElements(serviceName).stream().map(WebElement::getText).toList();
    }

    public List<String> getServiceLocationLinks(String serviceName) {
        return getServiceLocationElements(serviceName).stream().map(locationElement -> locationElement.getAttribute("href")).toList();

    }
}
