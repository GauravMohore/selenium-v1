package testcase.web;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.HomePage;
import testbase.BaseTest;
import utils.DataProviders;

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

}
