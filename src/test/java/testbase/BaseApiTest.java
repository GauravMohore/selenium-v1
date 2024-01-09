package testbase;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.util.ResourceBundle;

public class BaseApiTest {
    protected Response response;
    protected SoftAssert SAssert;
    protected String BASE_WEB_PAGE_URL;
    protected String CURRENT_WEB_PAGE_URL;
    protected static final String BASE_STAGING_APP_URI = "http://13.126.136.172:9000";
    protected static final String BASE_PRODUCTION_APP_URI = "https://api.shuru.co.in";

    @BeforeClass
    public void baseSetup() {
        BASE_WEB_PAGE_URL = ResourceBundle.getBundle("config.baseConfig").getString("url");
    }

    protected void failedTest(Exception error) {
        Assert.fail(error.getMessage());
    }
}
