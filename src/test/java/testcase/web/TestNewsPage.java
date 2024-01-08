package testcase.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.NewsPage;
import testbase.BaseTest;

public class TestNewsPage extends BaseTest {

    @DataProvider(name = "data")
    private Object[][] data() {
        Object[][] values = {
                {3, "FirstValue"},
                {7, "SecondValue"},
                {9, "ThirdValue"}
        };
        return values;
    }

    @Test(dataProvider = "data")
    public void test1(int index, String valueName) {
        Assert.assertEquals(index % 3, 0);
    }

    @Test(dependsOnMethods = "test1")
    private void test2() {
        System.out.println("I am passed");
    }
}
