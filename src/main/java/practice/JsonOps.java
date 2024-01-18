package practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v120.io.IO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonOps {

    public static String getResourceFilePath(String path) {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\" + path;
    }

    public static JsonNode getEntireJsonFileAsJsonNode(String jsonFileNameInSaved) throws JsonProcessingException {
        try {
            String filePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), jsonFileNameInSaved);
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(filePath);
            return mapper.readTree(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    public static String getEntireJsonFileAsString(String jsonFileNameInSaved) throws JsonProcessingException {
        try {
            String filePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), jsonFileNameInSaved);
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(filePath);
            JsonNode jsonNode = mapper.readTree(jsonFile);
            return jsonNode.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    public static void generateFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFileName = String.format("%s-%o.json", "location", System.currentTimeMillis());
        String jsonFilePath = String.format("%s\\src\\main\\resources\\saved\\%s", System.getProperty("user.dir"), jsonFileName);
        System.out.println(jsonFileName);

        ArrayNode stateArray = objectMapper.createArrayNode();
        WebDriver stateDriver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        try {
            stateDriver.get("https://shuru.co.in/news");
            stateDriver.findElements(By.cssSelector("a[title]")).forEach(stateElement -> {
                ObjectNode stateObject = objectMapper.createObjectNode();
                stateObject.put("locName", stateElement.getText());
                stateObject.put("locLevel", "STATE");
                stateObject.put("locUrl", stateElement.getAttribute("href"));
                stateArray.add(stateObject);
            });
            ObjectNode jsonRoot = objectMapper.createObjectNode();
            jsonRoot.set("locList", stateArray);
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), jsonRoot);
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        } finally {
            stateDriver.quit();
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        /* Test Methods */
        generateFile();
    }
}