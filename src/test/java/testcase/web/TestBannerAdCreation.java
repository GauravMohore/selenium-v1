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
import pages.PromotionPurchasePage;
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
    BannerFormPage formPage;
    PromotionPurchasePage purchasePage;

    @BeforeClass
    private void classSetup() throws MalformedURLException {
        URL url = new URL(baseURL + "app/promotion/form");
        url = TestUtils.addUrlParams(url, "shop_ad", "true");
        url = TestUtils.addUrlParams(url, "web-2-app", "true");
        url = TestUtils.addUrlParams(url, "lang", "en");
        url = TestUtils.addUrlParams(url, "uid", "3cb5b7e7-f222-4975-a21e-b9b69e5b3043");

        pageURL = url.toString();
        driver.get(pageURL);
        formPage = new BannerFormPage(driver, wait);
        purchasePage = new PromotionPurchasePage(driver);
    }

    //    @AfterClass
//    @Override
//    public void teardown() {
//        // don't close driver
//    }

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
            Assert.assertTrue(formPage.getBannerForm().isDisplayed());
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
            formPage.enterFormHeadingText(testString);

            //STEP-2: Validate maximum heading text character limit
            String failMessage = String.format("expected word limit to be %s, but was able to enter %s", maxBoundaryValue, formPage.getFormHeadingText().length());
            Assert.assertEquals(formPage.getFormHeadingText().length(), 50, failMessage);
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
            formPage.enterFormDescriptionText(testString);

            //STEP-2: Validate maximum heading text character limit
            String failMessage = String.format("expected word limit to be %s, but was able to enter %s", maxBoundaryValue, formPage.getFormHeadingText().length());
            Assert.assertEquals(formPage.getFormDescriptionText().length(), 100, failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2, groups = "bannerFormPage", dependsOnGroups = "bannerFormPageResponse")
    public void TC_105_BannerFormRadioSelection() {
        try {
            //STEP: Verify that WhatsApp radio button is selected by default
            String failMessage = "expected radio button not selected";
            Assert.assertTrue(formPage.getWhatsappRadioButton().isSelected(), failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 3, groups = {"bannerFormPage", "bannerFormCritical"}, dependsOnGroups = "bannerFormPageResponse")
    public void TC_106_WhatsAppNumberInput() {
        String expectedWhatsappNumber = "9131122182";
        try {

            //STEP-1: Select WhatsApp radio button (if not selected)
            if (!formPage.getWhatsappRadioButton().isSelected()) {
                formPage.getWhatsappRadioButton().click();
            }

            //STEP-2: Enter Phone Number for WhatsApp Message link
            String enteredWhatsappNumber = "+91" + expectedWhatsappNumber; // Currently not implemented
            formPage.enterWhatsappNumber(expectedWhatsappNumber);

            //STEP-3: Verify entered WhatsApp number
            String actualWhatsappNumber = formPage.getWhatsappInputNumber();
            String failMessage = String.format("expected number to be %s, but found %s", expectedWhatsappNumber, actualWhatsappNumber);

            Assert.assertEquals(expectedWhatsappNumber, actualWhatsappNumber, failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 4, groups = {"bannerFormPage", "bannerFormCritical"}, dependsOnGroups = "bannerFormPageResponse")
    public void TC_107_BannerAdImageSelection() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            //STEP-1: Add banner image
            String imageFileName = "ImageA.png";
            formPage.addBannerImage(wait, imageFileName);

            //StEP-2: Validate presence of uploaded image
            String failMessage = String.format("expected image '%s' not displayed", imageFileName);
            Assert.assertTrue(formPage.getUploadedImage().isDisplayed(), failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 5, groups = {"bannerFormPage", "bannerFormCritical"}, dependsOnGroups = {"bannerFormPageResponse"})
    public void TC_108_BannerPreviewFragment() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String purchasePageEndpoint = "app/promotion/purchase";
        try {
            //STEP-1: Click on Next Button
            formPage.clickOnNextButton();

            //STEP-2: Verify Preview Fragment
            String expectedPreviewFragmentTitleEN = "Preview";
            String actualPreviewFragmentTitle = formPage.getPreviewTitle(wait).getText();

            String failMessage1 = "expected preview fragment not displayed";
            String failMessage2 = String.format("expected preview text to be %s, but found %s", expectedPreviewFragmentTitleEN, actualPreviewFragmentTitle);
            SAssert.assertTrue(formPage.getPreviewTitle(wait).isDisplayed(), failMessage1);
            SAssert.assertEquals(actualPreviewFragmentTitle, expectedPreviewFragmentTitleEN, failMessage2);

            //STEP-3: Verify banner image is displayed in preview Fragment
            int totalUploadedImages = formPage.getUploadedImagesList().size();
            String failMessage3 = "expected banner image in preview not found";
            SAssert.assertEquals(totalUploadedImages, 2, failMessage3);

            //STEP-4: Click on 'Next' to navigate to purchase page
            formPage.clickNextButtonInPreview(wait);

            //STEP-5: Validate URL for purchase page
            try {
                wait.until(ExpectedConditions.urlContains(purchasePageEndpoint));
            } finally {
                String currentUrl = driver.getCurrentUrl();
                String failMessage4 = String.format("expected %s to be %s", currentUrl, baseURL + purchasePageEndpoint + "/*");
                SAssert.assertTrue(currentUrl.startsWith(baseURL + purchasePageEndpoint), failMessage4);
            }

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 6, groups = "bannerPurchasePage", dependsOnGroups = {"bannerFormPageResponse", "bannerFormCritical"})
    public void TC_109_PromotionLocalityLevelPlans() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            //STEP-1: Verify presence of District, State & Country level plans
            int expectedLocalityLevelCount = 3;
            int actualLocalityLevelCount = purchasePage.getLocLevelList().size();
            String failMessage1 = String.format("expected displayed location levels to be %d, but found %d", expectedLocalityLevelCount, actualLocalityLevelCount);
            SAssert.assertEquals(expectedLocalityLevelCount, actualLocalityLevelCount, failMessage1);

            //STEP-2: Verify that first(District) option is selected by default
            int districtOptionIndex = 0;
            String failMessage2 = String.format("expected option %d to be selected", districtOptionIndex);
            SAssert.assertTrue(purchasePage.isLocLevelOptionSelected(districtOptionIndex), failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 6, groups = "bannerPurchasePage", dependsOnMethods = "TC_109_PromotionLocalityLevelPlans")
    public void TC_110_PaymentGatewayRedirection() {
        try {
            String pgBaseUrl = "https://razorpay.com/payment-link";
            purchasePage.clickBuyPromotionButton();
            wait.until(ExpectedConditions.urlContains(pgBaseUrl));
            System.out.println(driver.getCurrentUrl());
        } catch (Exception error) {
            failedTest(error);
        }
    }

}
