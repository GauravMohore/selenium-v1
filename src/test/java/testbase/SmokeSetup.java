package testbase;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.util.ResourceBundle;

public class SmokeSetup {
    protected Response response;
    protected SoftAssert SAssert;
    protected String baseURL;
    protected String pageUrl;

    @BeforeClass
    public void baseSetup() {
        baseURL = ResourceBundle.getBundle("config.baseConfig").getString("url");
        pageUrl = baseURL;
    }

    protected void failedTest(Exception error) {
        Assert.fail(error.getMessage());
    }
}
