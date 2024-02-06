package testcase.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.ApiResult;
import components.RestAssuredUtils;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.WebScarper;
import base.ApiTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FullRegression extends ApiTest {
    ObjectMapper mapper;
    ApiResult apiTest;


    @DataProvider(name = "stateUrls")
    private Object[][] stateUrls() throws IOException {
        String filePath = WebScarper.getResourceFilePath("saved//all-news-location-details.json");
        mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        List<Object[]> list = new ArrayList<>();

        rootNode.get("locList").forEach(state -> {
            List<Object> stateInfo = new ArrayList<>();
            stateInfo.add(state.get("locName").asText());
            stateInfo.add(state.get("locUrl").asText());
            list.add(stateInfo.toArray(new Object[0]));
        });
        return list.toArray(new Object[0][0]);
    }

    @DataProvider(name = "districtUrls")
    private Object[][] districtUrls() throws IOException {
        String filePath = WebScarper.getResourceFilePath("saved//all-news-location-details.json");
        mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        List<Object[]> list = new ArrayList<>();

        rootNode.get("locList").forEach(state -> state.get("LocList")
                .forEach(district -> {
                    List<Object> districtInfo = new ArrayList<>();
                    districtInfo.add(district.get("LocName").asText());
                    districtInfo.add(district.get("LocUrl").asText());
                    list.add(districtInfo.toArray(new Object[0]));
                }));

        return list.toArray(new Object[0][0]);
    }

    @DataProvider(name = "subDistrictUrls")
    private Object[][] subDistrictUrls() throws IOException {
        String filePath = WebScarper.getResourceFilePath("saved//all-news-location-details.json");
        mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        List<Object[]> list = new ArrayList<>();

        rootNode.get("locList").forEach(state -> state.get("LocList")
                .forEach(district -> district
                        .forEach(subDistrict -> {
                            List<Object> subDistrictInfo = new ArrayList<>();
                            subDistrictInfo.add(district.get("LocName").asText());
                            subDistrictInfo.add(district.get("LocUrl").asText());
                            list.add(subDistrictInfo.toArray(new Object[0]));
                        })));

        return list.toArray(new Object[0][0]);
    }

    @DataProvider(name = "mandiStateUrls")
    private Object[] mandiStateUrls() throws IOException {
        String filePath = WebScarper.getResourceFilePath("saved//all-mandi-page-details.json");
        mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        List<Object[]> list = new ArrayList<>();

        rootNode.get("pageRoutes").forEach(state -> {
            List<Object> statePageInfo = new ArrayList<>();
            statePageInfo.add(state.get("pageTitle").asText().split("\\|")[0].trim());
            statePageInfo.add(state.get("pageUrl").asText());
            list.add(statePageInfo.toArray(new Object[0]));
        });
        return list.toArray(new Object[0][0]);

    }

    @DataProvider(name = "mandiDistrictUrls")
    private Object[] mandiDistrictUrls() throws IOException {
        String filePath = WebScarper.getResourceFilePath("saved//all-mandi-page-details.json");
        mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(filePath));

        List<Object[]> list = new ArrayList<>();

        rootNode.get("pageRoutes").forEach(state -> state.forEach(district -> {
            List<Object> statePageInfo = new ArrayList<>();
            statePageInfo.add(state.get("pageTitle").asText().split("\\|")[0].trim());
            statePageInfo.add(state.get("pageUrl").asText());
            list.add(statePageInfo.toArray(new Object[0]));
        }));
        return list.toArray(new Object[0][0]);
    }



    /* Test Cases --------------------------------------------------------*/

    @Test(dataProvider = "stateUrls", threadPoolSize = 4)
    public void testAllStateNews(String stateName, String stateUrl) {
        SAssert = new SoftAssert();
        try {
            apiTest = RestAssuredUtils.testUrl(stateUrl);
            int actualStatusCode = apiTest.getStatusCode();
            long actualResponseTime = apiTest.getResponseTime();
            SAssert.assertEquals(actualStatusCode, 200);
            SAssert.assertTrue(actualResponseTime <= 10000);
            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(dataProvider = "districtUrls", threadPoolSize = 4)
    public void testAllDistrictNews(String districtName, String districtUrl) {
        SAssert = new SoftAssert();
        try {
            apiTest = RestAssuredUtils.testUrl(districtUrl);
            int actualStatusCode = apiTest.getStatusCode();
            long actualResponseTime = apiTest.getResponseTime();
            SAssert.assertEquals(actualStatusCode, 200);
            SAssert.assertTrue(actualResponseTime <= 10000);
            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(dataProvider = "subDistrictUrls", threadPoolSize = 4)
    public void testAllSubDistrictUrls(String subDistrictName, String subDistrictUrl) {
        SAssert = new SoftAssert();
        try {
            apiTest = RestAssuredUtils.testUrl(subDistrictUrl);
            int actualStatusCode = apiTest.getStatusCode();
            long actualResponseTime = apiTest.getResponseTime();
            SAssert.assertEquals(actualStatusCode, 200);
            SAssert.assertTrue(actualResponseTime <= 10000);
            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(dataProvider = "mandiStateUrls", threadPoolSize = 4)
    public void testAllMandiStatePages(String pageTitle, String pageUrl) {
        SAssert = new SoftAssert();
        try {
            apiTest = RestAssuredUtils.testUrl(pageUrl);
            int actualStatusCode = apiTest.getStatusCode();
            long actualResponseTime = apiTest.getResponseTime();
            SAssert.assertEquals(actualStatusCode, 200);
            SAssert.assertTrue(actualResponseTime <= 10000);
            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }

    @Test(dataProvider = "mandiDistrictUrls", threadPoolSize = 4)
    public void testAllMandiDistrictPages(String pageTitle, String pageUrl) {
        SAssert = new SoftAssert();
        try {
            apiTest = RestAssuredUtils.testUrl(pageUrl);
            int actualStatusCode = apiTest.getStatusCode();
            long actualResponseTime = apiTest.getResponseTime();
            SAssert.assertEquals(actualStatusCode, 200);
            SAssert.assertTrue(actualResponseTime <= 10000);
            SAssert.assertAll();
        } catch (Exception error) {
            failedTest(error);
        }
    }
}
