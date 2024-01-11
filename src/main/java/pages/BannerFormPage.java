package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BannerFormPage extends BasePage {

    @FindBy(css = "body div form")
    private WebElement bannerForm;
    @FindBy(css = "input#imagePicker")
    private WebElement formImagePicker;
    @FindBy(css = "input#title")
    private WebElement formHeadingInput;
    @FindBy(css = "textarea#description")
    private WebElement formDescriptionInput;
    @FindBy(xpath = "input#WEBSITE")
    private WebElement websiteRadioButton;
    @FindBy(css = "input#WHATSAPP")
    private WebElement whatsappRadioButton;
    @FindBy(css = "input#whatsappInput")
    private WebElement whatsappInput;
    @FindBy(css = "img[alt='banner']")
    private WebElement uploadedImage;
    @FindBy(css = "img[alt='banner']")
    private List<WebElement> uploadedImagesList;
    @FindBy(xpath = "//form/button[last()]")
    private WebElement nextButton;
    @FindBy(xpath = "//h1[text()='Preview']")
    private WebElement previewTitle;
    @FindBy(xpath = "//footer/button[2]")
    private WebElement previewNextButton;

    public BannerFormPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public WebElement getBannerForm() {
        return bannerForm;
    }

    public WebElement getFormImagePicker() {
        return formImagePicker;
    }

    public void enterFormHeadingText(String headingText) {
        formHeadingInput.sendKeys(headingText);
    }

    public void enterFormDescriptionText(String headingText) {
        formDescriptionInput.sendKeys(headingText);
    }

    public String getFormHeadingText() {
        return formHeadingInput.getAttribute("value");
    }

    public String getFormDescriptionText() {
        return formDescriptionInput.getAttribute("value");
    }

    public WebElement getWebsiteRadioButton() {
        return websiteRadioButton;
    }

    public WebElement getWhatsappRadioButton() {
        return whatsappRadioButton;
    }

    public String getWhatsappInputNumber() {
        return whatsappInput.getAttribute("value");
    }

    public void enterWhatsappNumber(String whatsappNumber) {
        whatsappInput.sendKeys(whatsappNumber);
    }

    public String getRadioButtonLabel(WebElement radioButtonElement) {
        return radioButtonElement.findElement(By.xpath("//parent::label//span")).getText();
    }

    public WebElement getUploadedImage() {
        return uploadedImage;
    }

    public List<WebElement> getUploadedImagesList() {
        return uploadedImagesList;
    }

    public void addBannerImage(WebDriverWait wait, String fileNameInImagesWithExtension) throws InterruptedException {
        String filePath = String.format("%s\\src\\main\\resources\\testdata\\images\\%s", System.getProperty("user.dir"), fileNameInImagesWithExtension);
        formImagePicker.sendKeys(filePath);
        wait.until(ExpectedConditions.visibilityOf(uploadedImage));
    }

    public void clickOnNextButton() {
        nextButton.click();
    }

    public WebElement getPreviewTitle(WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOf(previewTitle));
        return previewTitle;
    }

    public void clickNextButtonInPreview(WebDriverWait wait) {
        wait.until(ExpectedConditions.visibilityOf(previewNextButton));
        previewNextButton.click();
    }
}
