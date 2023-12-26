package testcase.web;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.NewsPage;
import testbase.BaseTest;

public class TestNewsPage extends BaseTest {
    NewsPage newsPage;
    @BeforeClass()
    public void classSetup(){
        driver.get(baseURL+"news");
        newsPage = new NewsPage(driver);
    }
    @Test(dependsOnMethods = "checkNewsPage")
    public void TC_VerifyPageTitle(){
        System.out.println(driver.getTitle());
    }
}
