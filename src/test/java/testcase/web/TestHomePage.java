package testcase.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import testbase.BaseTestWeb;
import utils.DataProviders;

import java.util.Map;

public class TestHomePage extends BaseTestWeb {
    HomePage homePage;
    @BeforeClass
    private void setWebPage(){
        driver.get(baseURL);
        homePage = new HomePage(driver);
    }

    @DataProvider(name="homePageTestData")
    private Object[][] homePageTestData(){
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),"HomePageTitle");
    }

    @Test(dataProvider = "homePageTestData")
    public void TC_VerifyPageTitle(String key, String expectedTitle){
        try {
            String currentPageTitle = driver.getTitle();
            Assert.assertEquals(currentPageTitle,expectedTitle);
        }catch (Exception error){
                failedTest(error);
        }
    }

    @Test
    public void TC_ValidateHeaderElements(){
        try {
            //STEP-1: Validate app logo
            SAssert.assertTrue(homePage.getHeaderAppLogo().isDisplayed());

            //STEP-2 : Validate app download button
            SAssert.assertTrue(homePage.getHeaderDownloadButton().isDisplayed());
        }catch (Exception error){
            failedTest(error);
        }finally {
            SAssert.assertAll();
        }
    }
}
