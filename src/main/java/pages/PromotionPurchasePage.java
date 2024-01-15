package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.MalformedURLException;
import java.util.List;

public class PromotionPurchasePage extends BasePage {

    @FindBy(css = "img[alt='banner']")
    private WebElement uploadedImage;
    @FindBy(xpath = "//input[@name='place']/ancestor::label")
    private List<WebElement> locLevelList;
    @FindBy(xpath = "//input[@name='plan']/ancestor::label")
    private List<WebElement> locLevelPlanList;
    @FindBy(css = "img[alt='confetti']")
    private WebElement animatedAddOnImage;
    @FindBy(css = "input[name='upgrade']")
    private WebElement addOnCheckbox;

    @FindBy(css = "footer>button")
    private WebElement butPromotionButton;

    public PromotionPurchasePage(WebDriver driver) throws MalformedURLException {
        super(driver);
    }

    public List<WebElement> getLocLevelList() {
        return locLevelList;
    }

    public List<WebElement> getLocLevelRadioButtonList() {
        return locLevelList.stream().map(level -> level.findElement(By.tagName("input"))).toList();
    }

    public boolean isLocLevelOptionSelected(int locLevelOptionIndex) {
        Object selection = getLocLevelRadioButtonList().get(locLevelOptionIndex).getAttribute("checked");
        return selection != null;
    }

    public void clickBuyPromotionButton() {
        butPromotionButton.click();
    }
}
