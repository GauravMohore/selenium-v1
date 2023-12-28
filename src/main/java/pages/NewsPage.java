package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NewsPage extends BasePage{

    @FindBy(tagName = "h1")
    private WebElement ContentHeading;

    /* Constructor */
    public NewsPage(WebDriver driver){
        super(driver);
    }

    /* Page Methods */
    public String getContentHeading(){
        return ContentHeading.getText();
    }
}
