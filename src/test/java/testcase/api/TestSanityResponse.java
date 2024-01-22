package testcase.api;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testbase.ApiTest;
import utils.Helper;


public class TestSanityResponse extends ApiTest {

    @Test(priority = 0, groups = {"smoke", "homePageResponse"})
    public void testHomePageResponse() {
//        System.out.println(1); //Testing order
        SAssert = new SoftAssert();
        try {
            response = RestAssured.get(String.valueOf(new Helper().getPageUrl("home")));

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", BASE_WebUrl, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", BASE_WebUrl, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(priority = 0, groups = {"smoke", "servicesPageResponse"})
    public void testServicesPageResponse() {
        SAssert = new SoftAssert();
        try {
            response = RestAssured.get(String.valueOf(new Helper().getPageUrl("services")));

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", BASE_WebUrl, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", BASE_WebUrl, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
