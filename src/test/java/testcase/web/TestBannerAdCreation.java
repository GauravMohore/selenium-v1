package testcase.web;

import components.TestUtils;
import io.restassured.RestAssured;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.BannerFormPage;
import testbase.BaseTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestBannerAdCreation extends BaseTest {
    String pageURL;
    BannerFormPage bannerFormPage;

    @BeforeClass
    private void classSetup() throws MalformedURLException {
        URL url = new URL(baseURL + "app/promotion/form");
        url = TestUtils.addUrlParams(url, "shop_ad", "true");
        url = TestUtils.addUrlParams(url, "web-2-app", "true");
        url = TestUtils.addUrlParams(url, "lang", "en");
        url = TestUtils.addUrlParams(url, "uid", "3cb5b7e7-f222-4975-a21e-b9b69e5b3043");

        pageURL = url.toString();
        driver.get(pageURL);
        bannerFormPage = new BannerFormPage(driver, wait);
    }

    //    @AfterClass
//    @Override
    public void teardown() {
        // don't close driver
    }

    @Test(priority = 1, groups = {"bannerFormPageResponse", "bannerFormPage"})
    public void TC_101_VerifyBannerCreationFormResponse() {
        SAssert = new SoftAssert();
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("uid", "3cb5b7e7-f222-4975-a21e-b9b69e5b3043");
            requestParams.put("shop_ad", true);
            requestParams.put("web-2-app", true);
            requestParams.put("lang", "en");

            response = RestAssured.given().params(requestParams).get(pageURL);
            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", pageURL, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", pageURL, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_102_VerifyBannerFormElements() {
        try {
            //STEP: Verify presence of banner form
            String failMessage1 = "expected banner form not found";
            Assert.assertTrue(bannerFormPage.getBannerForm().isDisplayed());
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_103_BannerFormHeading() {
        try {
            //STEP-1: Enter 51 character String in heading text input
            int maxBoundaryValue = 51;
            String testString = IntStream.range(0, maxBoundaryValue).mapToObj(i -> "X").collect(Collectors.joining());
            bannerFormPage.enterFormHeadingText(testString);

            //STEP-2: Validate maximum heading text character limit
            String failMessage = String.format("expected word limit to be %s, but was able to enter %s", maxBoundaryValue, bannerFormPage.getFormHeadingText().length());
            Assert.assertEquals(bannerFormPage.getFormHeadingText().length(), 50, failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_104_BannerFormDescription() {
        try {
            //STEP-1: Enter 51 character String in heading text input
            int maxBoundaryValue = 101;
            String testString = IntStream.range(0, maxBoundaryValue).mapToObj(i -> "Y").collect(Collectors.joining());
            bannerFormPage.enterFormDescriptionText(testString);

            //STEP-2: Validate maximum heading text character limit
            String failMessage = String.format("expected word limit to be %s, but was able to enter %s", maxBoundaryValue, bannerFormPage.getFormHeadingText().length());
            Assert.assertEquals(bannerFormPage.getFormDescriptionText().length(), 100, failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_105_BannerFormRadioSelection() {
        try {
            //STEP: Verify that WhatsApp radio button is selected by default
            String failMessage = "expected radio button not selected";
            Assert.assertTrue(bannerFormPage.getWhatsappRadioButton().isSelected(), failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 3, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_106_WhatsAppNumberInput() {
        String expectedWhatsappNumber = "9131122182";
        try {

            //STEP-1: Select WhatsApp radio button (if not selected)
            if (!bannerFormPage.getWhatsappRadioButton().isSelected()) {
                bannerFormPage.getWhatsappRadioButton().click();
            }

            //STEP-2: Enter Phone Number for WhatsApp Message link
            String enteredWhatsappNumber = "+91" + expectedWhatsappNumber; // Currently not implemented
            bannerFormPage.enterWhatsappNumber(expectedWhatsappNumber);

            //STEP-3: Verify entered WhatsApp number
            String actualWhatsappNumber = bannerFormPage.getWhatsappInputNumber();
            String failMessage = String.format("expected number to be %s, but found %s", expectedWhatsappNumber, actualWhatsappNumber);

            Assert.assertEquals(expectedWhatsappNumber, actualWhatsappNumber, failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 4, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_106_BannerAdImageSelection() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            //STEP-1: Add banner image
            String imageFileName = "ImageA.png";
            bannerFormPage.addBannerImage(wait, imageFileName);

            //StEP-2: Validate presence of uploaded image
            String failMessage = String.format("expected image '%s' not displayed", imageFileName);
            Assert.assertTrue(bannerFormPage.getUploadedImage().isDisplayed(), failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 5, groups = "bannerFormPage", dependsOnMethods = "TC_106_BannerAdImageSelection")
    public void TC_107_BannerPreviewFragment() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            //STEP-1: Click on Next Button
            bannerFormPage.clickOnNextButton();

            //STEP-2: Verify Preview Fragment
            String expectedPreviewFragmentTitleEN = "Preview";
            String actualPreviewFragmentTitle = bannerFormPage.getPreviewTitle(wait).getText();

            String failMessage1 = "expected preview fragment not displayed";
            String failMessage2 = String.format("expected preview text to be %s, but found %s", expectedPreviewFragmentTitleEN, actualPreviewFragmentTitle);
            SAssert.assertTrue(bannerFormPage.getPreviewTitle(wait).isDisplayed(), failMessage1);
            SAssert.assertEquals(actualPreviewFragmentTitle, expectedPreviewFragmentTitleEN, failMessage2);

            //STEP-3: Verify banner image is displayed in preview Fragment
            int totalUploadedImages = bannerFormPage.getUploadedImagesList().size();
            String failMessage3 = "expected banner image in preview not found";
            SAssert.assertEquals(totalUploadedImages, 2, failMessage3);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 6, groups = "bannerFormPage", dependsOnMethods = "TC_106_BannerAdImageSelection")
    public void TC_BannerPurchasePage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String purchasePageEndpoint = "https://shuru.co.in/app/promotion/purchase";
        try {
            //STEP-1: Navigate to banner purchase page (if not there)
            bannerFormPage.clickNextButtonInPreview(wait);

        } catch (Exception error) {
            failedTest(error);
        }
    }
}
