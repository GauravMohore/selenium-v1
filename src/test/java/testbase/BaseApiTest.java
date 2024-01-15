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
    protected StringBuilder BASE_WebUrl;
    protected static StringBuilder BAST_StageUri;
    protected static StringBuilder BASE_ProdUri;

    @BeforeClass
    public void baseSetup() {
        BASE_WebUrl = new StringBuilder(ResourceBundle.getBundle("config.base-config").getString("url"));
        BAST_StageUri = new StringBuilder("http://13.126.136.172:9000");
        BASE_ProdUri = new StringBuilder("https://api.shuru.co.in");
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
