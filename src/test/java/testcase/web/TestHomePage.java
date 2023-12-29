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
    HomePage homePage;
    @BeforeClass
    private void classSetup(){
        driver.get(baseURL);
        homePage = new HomePage(driver);
    }

    @DataProvider(name="homePageTitle")
    private Object[][] homePageTitle(){
        String sheetName = "HomePageTitle";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),sheetName);
    }

    @DataProvider(name="appDownloadLink")
    private Object[][] appDownloadLink(){
        String sheetName = "AppDownloadLink";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),sheetName);
    }

    @DataProvider(name="contactEmailAddress")
    private Object[][] contactEmailAddress(){
        String sheetName = "ContactEmailAddress";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),sheetName);
    }

    @DataProvider(name="footerLinks")
    private Object[][] footerLinks(){
        String sheetName = "FooterLinks";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),sheetName);
    }

    /* TESTCASES */
    /*---Testing Page Response------------------------------------*/

    @Test(priority = 1, groups = "response")
    public void TC_101_ValidatePageResponse(){
        System.out.println(1);
        try{
            response = RestAssured.get(baseURL);

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            SAssert.assertEquals(statusCode, 200);

            //STEP-2: Verify page response time to be under 10s
            SAssert.assertTrue(responseTime <= 10000);
            SAssert.assertAll();

        }catch (Exception error){
            failedTest(error);
        }
    }

    @Test(priority = 2, dataProvider = "homePageTitle", groups = "response", dependsOnMethods = "TC_101_ValidatePageResponse")
    public void TC_102_VerifyPageTitle(String key, String expectedTitle){
        System.out.println(2);
        try {
            String currentPageTitle = driver.getTitle();
            //STEP: Validate Page Title
            Assert.assertEquals(currentPageTitle,expectedTitle);
        }catch (Exception error){
                failedTest(error);
        }
    }

    /*---Testing Header Elements------------------------------------*/

    @Test(priority = 3, dependsOnGroups = "response")
    public void TC_201_VerifyHeaderAppLogo(){
        System.out.println(3);
        try {
            //STEP: Verify presence of app logo
            Assert.assertTrue(homePage.getHeaderAppLogo().isDisplayed());
        }catch (Exception error){
            failedTest(error);
        }
    }

    @Test(priority = 3, dataProvider = "appDownloadLink", dependsOnGroups = "response")
    public void TC_202_VerifyHeaderDownloadLink(String key, String expectedDownloadLink){
        System.out.println(4);
        try {
            //STEP-1: Verify presence of app download link
            SAssert.assertTrue(homePage.getHeaderDownloadButton().isDisplayed());

            //STEP-2: Validate app download link
            String actualDownloadLink = homePage.getHeaderDownloadButton().getAttribute("href");
            SAssert.assertEquals(expectedDownloadLink,actualDownloadLink);

            SAssert.assertAll();

        }catch (Exception error){
            failedTest(error);
        }
    }

    /*---Testing Footer Elements------------------------------------*/
    @Test(priority = 3,dataProvider = "contactEmailAddress", dependsOnGroups = "response")
    public void TC_301_VerifyContactEmail(String key, String expectedContactAddress){
        System.out.println(5);
        try {
            //STEP-1: Verify presence of Contact Us link
            SAssert.assertTrue(homePage.getContactEmailLink().isDisplayed());

            //STEP-2: Validate Contact email address
            String actualContactAddress = homePage.getContactEmailLink().getAttribute("href").replace("mailto:","");
            SAssert.assertEquals(expectedContactAddress,actualContactAddress);

            SAssert.assertAll();
        }catch (Exception error){
            failedTest(error);
        }
    }

    @Test(priority = 3, dataProvider = "footerLinks",dependsOnGroups = "response")
    public void TC_302_VerifyFooterLinksLinks(double index, String title, String linkText, String linkRoute) {
        System.out.printf("%d -> %f%n",6,index);
        SAssert = new SoftAssert();
        try {
            HashMap<String,String> map = homePage.getFooterLinks(title);

            //STEP-1: Verify presence of link text
            SAssert.assertTrue(map.containsKey(linkText));

            //STEP-2: Verify link route
            SAssert.assertTrue(map.get(linkText).contains(linkRoute));

            SAssert.assertAll();
        }catch (Exception error){
            failedTest(error);
        }
    }
}
