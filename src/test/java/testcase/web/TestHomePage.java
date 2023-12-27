package testcase.web;

import org.testng.Assert;
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

    @DataProvider(name="homePageTestData")
    private Object[][] homePageTestData(){
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),"HomePageTitle");
    }

    @Test(dataProvider = "homePageTestData")
    public void TC_VerifyPageTitle(String key, String expectedTitle){
        try {
            String currentPageTitle = driver.getTitle();
            //STEP: Validate Page Title
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
