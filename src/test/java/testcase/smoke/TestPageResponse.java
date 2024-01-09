package testcase.smoke;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testbase.BaseApiTest;
import testbase.BaseTest;


public class TestPageResponse extends BaseApiTest {

    @Test(priority = 0, groups = {"smoke", "homePageResponse"})
    public void testHomePageResponse() {
        System.out.println(1);
        SAssert = new SoftAssert();
        CURRENT_WEB_PAGE_URL = BASE_WEB_PAGE_URL;
        try {
            response = RestAssured.get(CURRENT_WEB_PAGE_URL);

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", CURRENT_WEB_PAGE_URL, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", CURRENT_WEB_PAGE_URL, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
