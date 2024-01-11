package testcase.smoke;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovyjarjarantlr4.v4.codegen.model.SrcOp;
import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import practice.JsonOps;
import testbase.BaseApiTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestNewsPageResponse extends BaseApiTest {
    ObjectMapper mapper;

    @DataProvider(name = "stateUrls")
    private Object[][] stateUrls() throws IOException {
        String filePath = JsonOps.getResourceFilePath("saved//locationData.json");
        mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        List<Object[]> list = new ArrayList<>();

        rootNode.get("subLocations").forEach(state -> {
            List<Object> stateInfo = new ArrayList<>();
            stateInfo.add(state.get("locName").asText());
            stateInfo.add(state.get("locUrl").asText());
            list.add(stateInfo.toArray(new Object[0]));
        });

        return list.toArray(new Object[0][0]);
    }


    @Test(dataProvider = "stateUrls")
    public void testHomePageResponse(String stateName, String stateUrl) {
        SAssert = new SoftAssert();
        try {
            response = RestAssured.get(stateUrl);

            int statusCode = response.getStatusCode();
            long responseTime = response.getTime();

            //STEP-1: Validate Status Code
            String failMessage1 = String.format("expected status code to be 200, but %s responded with %d", stateUrl, statusCode);
            SAssert.assertEquals(statusCode, 200, failMessage1);

            //STEP-2: Verify page response time to be under 10s
            String failMessage2 = String.format("expected response time(in ms) to be under 10000, but %s responded in %d", stateUrl, responseTime);
            SAssert.assertTrue(responseTime <= 10000, failMessage2);

            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
