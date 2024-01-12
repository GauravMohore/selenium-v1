package testbase;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.util.ResourceBundle;

public class BaseApiTest {
    protected Response response;
    protected SoftAssert SAssert;
    protected StringBuilder BASE_WEB_PAGE_URL;
    protected String CURRENT_WEB_PAGE_URL;
    protected static final String BASE_STAGING_APP_URI = "http://13.126.136.172:9000";
    protected static final String BASE_PRODUCTION_APP_URI = "https://api.shuru.co.in";

    @BeforeClass
    public void baseSetup() {
        BASE_WEB_PAGE_URL = new StringBuilder(ResourceBundle.getBundle("config.baseConfig").getString("url"));
    }

    protected void failedTest(Exception error) {
        Assert.fail(error.getMessage());
    }

    protected void testResponse(String uri) {
        SAssert = new SoftAssert();
        try {
            response = RestAssured.get(uri);

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", uri, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", uri, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
