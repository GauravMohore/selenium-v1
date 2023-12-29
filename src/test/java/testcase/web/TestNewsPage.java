package testcase.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.NewsPage;
import testbase.BaseTest;

public class TestNewsPage extends BaseTest {
    NewsPage newsPage;
    @BeforeClass()
    public void classSetup(){
//        driver.get(baseURL+"news");
        newsPage = new NewsPage(driver);
    }

    @DataProvider(name = "data")
    private Object[][] data(){
        return new Object[][]{
                {10, 20, 30},
                {1, 2, 3},
                {100, 2, 300},
                {1, 2000, 3000}
        };
    }

    @Test(dataProvider = "data")
    public void test(int first, int second, int third){
        SAssert = new SoftAssert();
        try {
            SAssert.assertEquals(first,1);
            SAssert.assertEquals(second,2);
            SAssert.assertEquals(third,3);
            SAssert.assertAll();
        }catch (Exception error){
            failedTest(error);
        }
    }
}
