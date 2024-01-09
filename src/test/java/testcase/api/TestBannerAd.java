package testcase.api;

import components.TestUtils;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import testbase.BaseApiTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class TestBannerAd extends BaseApiTest {

    @BeforeClass
    public void setup() {
        // Configure RestAssured base URI for all tests
        RestAssured.baseURI = "http://13.126.136.172:9000/v1/user/device";
    }

    @Test
    public void testApiRequest() throws IOException {
        // Store headers in a Map for better organization
        HashMap<String, String> headers = TestUtils.getRequestHeaders("RequestHeaders");

        // Generate random GID using TestUtils
        String gid = TestUtils.getRandomGID();
        String requestBody = String.format("{\"gid\":\"%s\"}", gid);
        System.out.println(requestBody);

        // Send the request
        response = RestAssured.given()
                .headers(headers)
                .body(requestBody)
                .post();// Use base URI from setup()

        // Validate response status code
        Assert.assertEquals(response.getStatusCode(), 200); // Assuming expected status code is 200

        // Validate response body (example)
        String responseBody = response.asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains(gid)); // Example assertion
    }
}

