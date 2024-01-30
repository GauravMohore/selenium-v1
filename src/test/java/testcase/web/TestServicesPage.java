package testcase.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ServicesPage;
import base.BaseTest;
import utils.DataProviders;

import java.net.MalformedURLException;

public class TestServicesPage extends BaseTest {
    String pageUrl;
    ServicesPage servicesPage;

    @BeforeClass
    private void classSetup() throws MalformedURLException {
        pageUrl = String.valueOf(getPageUrl("services"));
        driver.get(pageUrl);
        servicesPage = new ServicesPage(driver);
    }

    @DataProvider(name = "servicesPageTitle")
    private Object[][] servicesPageTitle() {
        String sheetName = "ServicesPageTitle";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_ServicesPage"), sheetName);
    }

    @DataProvider(name = "servicesList")
    private Object[][] servicesList() {
        String sheetName = "ServicesList";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_ServicesPage"), sheetName);
    }

    /* TESTCASES */
    /*---Testing Page Response------------------------------------*/

    @Test(priority = 1, dataProvider = "servicesPageTitle", groups = {"servicesPageTitle", "servicesPage"})
    public void TC_101_VerifyPageTitle(String key, String expectedTitle) {
        try {
            try {
                String currentPageTitle = driver.getTitle();
                //STEP: Validate Page Title
                String failMessage = String.format("expected page title for %s to be '%s', but found '%s'", pageUrl, expectedTitle, currentPageTitle);
                Assert.assertEquals(currentPageTitle, expectedTitle, failMessage);
            } catch (Exception error) {
                failedTest(error);
            }
        } catch (Exception error) {
            failedTest(error);
        }
    }

    /*---Testing services links ------------------------------------*/

    @Test(priority = 2, dataProvider = "servicesList", groups = "servicesPage")
    public void TC_201_TestServicesLinks(String expectedHeading) {
        SAssert = new SoftAssert();
        try {
            //STEP-1: Verify presence of Service job titles
            String failMessage1 = String.format("expected %s category not found", expectedHeading);
            Assert.assertTrue(servicesPage.getServiceNames().contains(expectedHeading), failMessage1);

            //STEP-2: Verify service locations for each job title
            String failMessage2 = "expected links not found";
            SAssert.assertNotNull(servicesPage.getServiceLocationElements(expectedHeading), failMessage2);

            //STEP-3: Validate links for top 10 <service> list
            servicesPage.getServiceLocationLinks(expectedHeading).forEach(link -> {
                String failMessage3 = "expected url turned out to be invalid";
                SAssert.assertTrue(link.contains(pageUrl), failMessage3);
            });

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
