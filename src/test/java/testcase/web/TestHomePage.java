package testcase.web;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testbase.BaseTestWeb;
import utils.DataProviders;

import java.util.Map;

public class TestHomePage extends BaseTestWeb {

    @DataProvider(name="homePageTestData")
    private Object[][] homePageTestData(){
        return DataProviders.readFromExcel(getTestDataFilePath("TD_HomePage"),"HomePageTitle");
    }

    @Test(dataProvider = "homePageTestData")
    public void TC_testPageTitle(String key, String expectedTitle){
       driver.get(baseURL);
        try {
            String currentPageTitle = driver.getTitle();
            Assert.assertEquals(currentPageTitle,expectedTitle);
        }catch (Exception error){
                failedTest(error);
        }
    }
}
