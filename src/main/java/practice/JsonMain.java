package practice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import practice.JsonOps;
import utils.WebScraper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JsonMain {

    public static void main(String[] args) throws IOException {
        String filePath = JsonOps.getResourceFilePath("saved\\locationData.json");
        System.out.println(filePath);

        ObjectMapper objectMapper = new ObjectMapper();

        //reading json file into jsonNode
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        List<String> urls = new ArrayList<>();
        // Access values from nested objects
        rootNode.get("subLocations").forEach(url -> urls.add(url.get("locUrl").toString()));
        urls.forEach(System.out::println);
    }
}
