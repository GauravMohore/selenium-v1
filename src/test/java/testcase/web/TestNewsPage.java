package testcase.web;

import org.testng.annotations.BeforeClass;
import pages.BasePage;
import pages.NewsPage;
import testbase.BaseTestWeb;

public class TestNewsPage extends BaseTestWeb {
    NewsPage newsPage;
    @BeforeClass()
    public void setWebPage(){
        driver.get(baseURL+"news");
        newsPage = new NewsPage(driver);
    }
}
