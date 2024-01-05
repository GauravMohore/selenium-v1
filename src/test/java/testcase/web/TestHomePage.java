package testcase.web;

import io.restassured.RestAssured;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import testbase.BaseTest;
import utils.DataProviders;

import java.util.*;
import java.util.stream.Collectors;

public class TestHomePage extends BaseTest {
    String pageURL;
    HomePage homePage;

    @BeforeClass
    private void classSetup() {
        pageURL = baseURL;
        driver.get(pageURL);
        homePage = new HomePage(driver);
    }

    @DataProvider(name = "homePageTitle")
    private Object[][] homePageTitle() {
        String sheetName = "HomePageTitle";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"), sheetName);
    }

    @DataProvider(name = "appDownloadLink")
    private Object[][] appDownloadLink() {
        String sheetName = "AppDownloadLink";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"), sheetName);
    }

    @DataProvider(name = "contactEmailAddress")
    private Object[][] contactEmailAddress() {
        String sheetName = "ContactEmailAddress";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"), sheetName);
    }

    @DataProvider(name = "footerLinks")
    private Object[][] footerLinks() {
        String sheetName = "FooterLinks";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"), sheetName);
    }

    @DataProvider(name = "socialMedia")
    private Object[][] socialMedia() {
        String sheetName = "SocialMedia";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"), sheetName);
    }

    /* TESTCASES */
    /*---Testing Page Response------------------------------------*/

    @Test(priority = 1, groups = "response")
    public void TC_101_ValidatePageResponse() {
        SAssert = new SoftAssert();
        System.out.println(1);
        try {
            response = RestAssured.get(baseURL);

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", pageURL, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", pageURL, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);
            SAssert.assertAll();

        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 2, dataProvider = "homePageTitle", groups = "response")
    public void TC_102_VerifyPageTitle(String key, String expectedTitle) {
        System.out.println(2);
        try {
            String currentPageTitle = driver.getTitle();
            //STEP: Validate Page Title
            String failMessage = String.format("expected page title for %s to be '%s', but found '%s'", pageURL, expectedTitle, currentPageTitle);
            Assert.assertEquals(currentPageTitle, expectedTitle, failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    /*---Testing Header Elements------------------------------------*/

    @Test(priority = 3, dependsOnGroups = "response")
    public void TC_201_VerifyHeaderAppLogo() {
        System.out.println(3);
        try {
            //STEP: Verify presence of app logo
            String failMessage = "expected app logo in header not found";
            Assert.assertTrue(homePage.getHeaderAppLogo().isDisplayed(), failMessage);
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 3, dataProvider = "appDownloadLink", dependsOnGroups = "response")
    public void TC_202_VerifyHeaderDownloadLink(String key, String expectedDownloadLink) {
        System.out.println(4);
        try {
            //STEP-1: Verify presence of app download link
            String failMessage1 = "expected app download button not found";
            SAssert.assertTrue(homePage.getHeaderDownloadButton().isDisplayed(), failMessage1);

            //STEP-2: Validate app download link
            String actualDownloadLink = homePage.getHeaderDownloadButton().getAttribute("href");
            String failMessage2 = String.format("expected download link to be %s, but found %s", expectedDownloadLink, actualDownloadLink);
            SAssert.assertEquals(expectedDownloadLink, actualDownloadLink, failMessage2);

            SAssert.assertAll();

        } catch (Exception error) {
            failedTest(error);
        }
    }

    /*---Testing Footer Elements------------------------------------*/

    @Test(priority = 3, dataProvider = "contactEmailAddress", dependsOnGroups = "response")
    public void TC_301_VerifyContactEmail(String key, String expectedContactAddress) {
        System.out.println(5);
        try {
            //STEP-1: Verify presence of Contact Us link
            String failMessage1 = "expected 'Contact Us' email not found";
            SAssert.assertTrue(homePage.getContactEmailLink().isDisplayed(), failMessage1);

            //STEP-2: Validate Contact email address
            String actualContactAddress = homePage.getContactEmailLink().getAttribute("href").replace("mailto:", "");
            String failMessage2 = String.format("expected contact email address to be '%s', but found '%s'", expectedContactAddress, actualContactAddress);
            SAssert.assertEquals(expectedContactAddress, actualContactAddress, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 3, dataProvider = "footerLinks", dependsOnGroups = "response")
    public void TC_302_VerifyFooterLinksLinks(double index, String title, String linkText, String linkRoute) {
        System.out.println(String.format("6(%s)", Math.round(index * 10) / 10.0));
        SAssert = new SoftAssert();
        try {
            HashMap<String, String> map = homePage.getFooterLinks(title);

            //STEP-1: Verify presence of link text
            String failMessage1 = String.format("expected '%s' link not found under '%s' section", linkText, title);
            SAssert.assertTrue(map.containsKey(linkText), failMessage1);

            //STEP-2: Verify link route
            String failMessage2 = String.format("expected link '%s' not found in %s", linkRoute, linkText);
            SAssert.assertTrue(map.get(linkText).contains(linkRoute), failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 3, dataProvider = "socialMedia", dependsOnGroups = "response")
    public void TC_303_VerifySocialMediaIntegration(double Index, String title, String expectedLink) {
        System.out.println(String.format("7(%d)", (int) Index));
        SAssert = new SoftAssert();
        try {
            List<WebElement> links = homePage.getSocialMediaLinks();
            //STEP-1: Verify presence of Social Media element
            links.forEach(link -> {
                String failMessage1 = "expected link not found";
                SAssert.assertTrue(link.isDisplayed(), failMessage1);
            });

            //STEP-2: Validate Social Media link
            boolean isLinkPresent = links.stream()
                    .map(link -> link.getAttribute("href"))
                    .toList()
                    .contains(expectedLink);
            String failMessage2 = String.format("expected link %s not found on %s", expectedLink, pageURL);
            SAssert.assertTrue(isLinkPresent, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}

