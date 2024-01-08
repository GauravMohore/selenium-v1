package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.*;

public class WebScraper {
    private static ResourceBundle resource = ResourceBundle.getBundle("config.baseConfig");
    private static String baseURL = resource.getString("url");
    private static WebDriver driver;
    private static ObjectMapper objectMapper;

    public static void createLocationJson(String jsonFileName) {
        String savePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), jsonFileName);
        try {
            objectMapper = new ObjectMapper();

            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("locLevel", "COUNTRY");

            File outputFile = new File(savePath);
            try {
                objectMapper.writeValue(outputFile, rootNode);
            } catch (Exception error) {
                System.out.println(error);
            }
        } catch (Exception error) {
            System.out.println(error);
        }

    }

    private static void updateStates(String savedJsonFileName, LinkedHashMap<String, String> locationSet) {
        String savePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), savedJsonFileName);
        File file = new File(savePath);
        try {
            objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(file);

            if (rootNode.has("subLocations") && rootNode.get("subLocations").isArray()) {
                ArrayNode subLocationsNode = (ArrayNode) rootNode.get("subLocations");


                locationSet.entrySet().stream()
                        .map(entry -> {
                            ObjectNode stateObject = objectMapper.createObjectNode();
                            stateObject.put("locLevel", "STATE");
                            stateObject.put("locName", entry.getKey());
                            stateObject.put("locUrl", entry.getValue());
                            return stateObject;
                        })
                        .forEach(subLocationsNode::add);

                objectMapper.writeValue(file, rootNode);
                System.out.println("Success");
            } else System.out.println("Failure");

        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }


    public static void main(String[] args) {
        driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        driver.get(baseURL + "news");
        createLocationJson("test");
        try {
            LinkedHashMap<String, String> locationSet = new LinkedHashMap<>();
            List<WebElement> states = driver.findElements(By.cssSelector("div li a[title]"));
            states.forEach(state -> {
                String stateName = state.getText();
                String pageUrl = state.getAttribute("href");
                locationSet.put(stateName, pageUrl);
            });

            updateStates("locationData", locationSet);
        } finally {
            if (driver != null) driver.quit();
        }
    }
}
