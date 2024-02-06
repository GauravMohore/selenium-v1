package testcase.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import common.ApiResult;
import components.RestAssuredUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Helper;
import utils.WebScarper;

import java.io.File;
import java.io.IOException;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sanity {
    ObjectMapper mapper;
    SoftAssert SAssert;

    @DataProvider(name = "sanityLocations")
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

    @Test(dataProvider = "sanityLocations", threadPoolSize = 5)
    public void testLink(String locationName, String locationUrl) throws IOException {
        SAssert = new SoftAssert();
        try {
            ApiResult linkResponse = RestAssuredUtils.testUrl(locationUrl);
            Document pageSource = Jsoup.parse(linkResponse.getPageSource());
            List<Element> subLocations = pageSource.select("div[class=list-items]>a");
            subLocations.parallelStream().forEach(link -> System.out.println(link.attr("href")));
            System.out.println();
            SAssert.assertEquals(linkResponse.getStatusCode(), 200);
            SAssert.assertTrue(linkResponse.getResponseTime() <= 10000);
            SAssert.assertNotNull(subLocations);
        } catch (AssertionError error) {
            System.out.println(error.getMessage());
        }
    }

    public static List<String> expList() throws IOException {
        String filePath = WebScarper.getResourceFilePath("saved//all-news-location-details.json");
        ObjectMapper mapper2 = new ObjectMapper();
        List expList = new ArrayList();
        mapper2.readTree(new File(filePath)).get("locList").forEach(obj -> expList.add(obj.get("locUrl").asText()));
        return expList;
    }

    @Test
    public void expTest() throws IOException {
        List<String> urls = expList();
        urls.forEach(url -> {
            System.out.println(url);
            ApiResult res = RestAssuredUtils.testUrl(url.toString());
            System.out.println(res.getStatusCode() + " : " + res.getResponseTime());
        });
    }
}
