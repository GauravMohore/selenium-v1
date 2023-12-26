package testcase.web;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.NewsPage;
import testbase.BaseTestWeb;

public class TestNewsPage extends BaseTestWeb {
    NewsPage newsPage;
    @BeforeClass()
    public void classSetup(){
        driver.get(baseURL+"news");
        newsPage = new NewsPage(driver);
    }
    @Test
    public void TC_VerifyPageTitle(){
        System.out.println(driver.getTitle());
    }
}
