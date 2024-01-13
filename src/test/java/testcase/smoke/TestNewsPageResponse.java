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

    @Test(dataProvider = "stateUrls", threadPoolSize = 5)
    public void testHomePageResponse(String stateName, String stateUrl) {
        testResponse(stateUrl);
    }
}
