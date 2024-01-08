package testcase.smoke;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testbase.BaseApiTest;


public class TestPageResponse extends BaseApiTest {

    @Test(priority = 0, groups = {"smoke", "homePageResponse"})
    public void testHomePageResponse() {
        System.out.println(1);
        SAssert = new SoftAssert();
        pageUrl = baseURL;
        try {
            response = RestAssured.get(pageUrl);

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", pageUrl, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", pageUrl, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
//            failedTest(error);
            Assert.fail(error.getMessage());
        }
    }

    @Test
    public void hitRestApiAndPrintResponse() {
        String apiUrl = "http://13.126.136.172:9000/v2/feed";

        // Set headers
        RestAssured.given()
                .header("accept-encoding", "gzip")
                .header("content-type", "application/json")
                .header("app-ver", "154")
                .header("applicationid", "com.shuru.nearme.debug")
                .header("cc", "in")
                .header("content-length", "0")
                .header("device", "APP")
                .header("gid", "5d83d622-93ab-46f7-8e10-7b93dd8b644f")
                .header("lang", "en")
                .header("lcnumber", "5")
                .header("os_version", "31")
                .header("platform", "ANDROID")
                .header("req_id", "f92c819f-4f7e-494b-94a4-a87f65a424c3")
                .header("session_id", "1325a049-ba4c-43d1-a734-6cb5f9b9c919")
                .header("token", "749b109a-e99d-46b4-ab26-c4bf0db05788")
                .header("uid", "9da9a32a-a8f3-4cf7-abc9-adb6173b9f2d")
                .queryParam("location", "22.7195687,75.8577258")
                .queryParam("type", "distance")
                .queryParam("allowCompression", "false")
                .queryParam("localitySelectionLevel", "SUB_DISTRICT")
                .queryParam("locationChanged", "true")
                .when()
                .get(apiUrl)
                .then()
                .log().all()
                .statusCode(200) // Assuming you expect a 200 OK response
                .extract().response();

        // Print the JSON response
        String jsonResponse = response.getBody().asString();
        System.out.println("JSON Response:\n" + jsonResponse);
    }
}
