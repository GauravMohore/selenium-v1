package practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.checkerframework.checker.units.qual.C;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
        ObjectNode jsonRoot = objectMapper.createObjectNode();

        String pageUrl = "https://shuru.co.in/news";
        stateDriver.get(pageUrl);
        try {
            stateDriver.findElements(By.cssSelector("a[title]")).forEach(stateElement -> {
                ObjectNode stateObject = objectMapper.createObjectNode();
                stateObject.put("locName", stateElement.getText());
                stateObject.put("locLevel", "STATE");
                stateObject.put("locUrl", stateElement.getAttribute("href"));
                stateArray.add(stateObject);
            });
        } finally {
            stateDriver.quit();
        }

        stateArray.forEach(stateNode -> {
            String stateUrl = stateNode.get("locUrl").asText();
            System.out.println(stateUrl);

            ArrayNode districtArray = objectMapper.createArrayNode();
            WebDriver districtDriver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
            try {
                districtDriver.get(stateUrl);
                districtDriver.findElements(By.cssSelector("div[class=list-items]>a")).forEach(districtElement -> {
                    ObjectNode districtObject = objectMapper.createObjectNode();
                    districtObject.put("locName", districtElement.getText());
                    districtObject.put("locLevel", "DISTRICT");
                    districtObject.put("locUrl", districtElement.getAttribute("href"));
                    districtArray.add(districtObject);
                    System.out.println("    " + districtObject.get("locName") + " --> " + districtObject.get("locUrl"));
                });
            } finally {
                districtDriver.quit();
            }
        });

        jsonRoot.put("locName", "India");
        jsonRoot.put("locLevel", "COUNTRY");
        jsonRoot.put("locUrl", pageUrl);
        jsonRoot.set("locList", stateArray);

        // File Creation
        try {
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), jsonRoot);
            System.out.println("success");
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    public static void generateFileJsoup() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFileName = String.format("%s-%d.json", "location", System.currentTimeMillis());
        String jsonFilePath = String.format("%s/src/main/resources/saved/%s", System.getProperty("user.dir"), jsonFileName);
        System.out.println(jsonFileName);

        ObjectNode jsonRoot = objectMapper.createObjectNode();
        ArrayNode stateArray = objectMapper.createArrayNode();

        String pageUrl = "https://shuru.co.in";
        String newsPageUrl = pageUrl + "/news";

        int maxRetries = 3;

        try {
            Document stateDocument = connectWithRetry(newsPageUrl, 20000, maxRetries);
            Elements stateElements = stateDocument.select("a[title]");

            for (Element stateElement : stateElements) {
                ObjectNode stateObject = objectMapper.createObjectNode();
                String stateUrl = pageUrl + stateElement.attr("href");
                stateObject.put("locName", stateElement.text());
                stateObject.put("locLevel", "STATE");
                stateObject.put("locUrl", stateUrl);

                Document districtDocument = connectWithRetry(stateUrl, 20000, maxRetries);
                Elements districtElements = districtDocument.select("div[class=list-items]>a");

                if (!districtElements.isEmpty()) {
                    ArrayNode districtArray = objectMapper.createArrayNode();

                    for (Element districtElement : districtElements) {
                        String districtUrl = pageUrl + districtElement.attr("href");
                        ObjectNode districtObject = objectMapper.createObjectNode();
                        districtObject.put("LocName", districtElement.text());
                        districtObject.put("LocLevel", "DISTRICT");
                        districtObject.put("LocUrl", districtUrl);

                        Document subDistrictDocument = connectWithRetry(districtUrl, 20000, maxRetries);
                        Elements subDistrictElements = subDistrictDocument.select("div[class=list-items]>a");

                        if (!subDistrictElements.isEmpty()) {
                            ArrayNode subDistrictArray = objectMapper.createArrayNode();

                            for (Element subDistrictElement : subDistrictElements) {
                                String subDistrictUrl = pageUrl + subDistrictElement.attr("href");
                                ObjectNode subDistrictObject = objectMapper.createObjectNode();
                                subDistrictObject.put("LocName", subDistrictElement.text());
                                subDistrictObject.put("LocLevel", "SUB-DISTRICT");
                                subDistrictObject.put("LocUrl", subDistrictUrl);

                                subDistrictArray.add(subDistrictObject);
                            }

                            districtObject.set("LocList", subDistrictArray);
                            districtArray.add(districtObject);
                        }
                    }

                    stateObject.set("LocList", districtArray);
                    stateArray.add(stateObject);
                }
            }

            jsonRoot.put("locName", "India");
            jsonRoot.put("locLevel", "COUNTRY");
            jsonRoot.put("locUrl", newsPageUrl);
            jsonRoot.set("locList", stateArray);

            // File Creation
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), jsonRoot);
                System.out.println("Success");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to the website: " + e.getMessage());
        }
    }

    private static Document connectWithRetry(String url, int timeout, int maxRetries) throws IOException, InterruptedException {
        int retries = 0;
        boolean success = false;
        Document document = null;

        while (retries < maxRetries && !success) {
            try {
                document = Jsoup.connect(url).timeout(timeout).get();
                success = true; // Break out of the loop if the connection is successful
            } catch (IOException e) {
                System.out.println("Error connecting to the website: " + e.getMessage());
                retries++;
                // Optionally sleep for a short duration before retrying
                Thread.sleep(1000); // 1 second
            }
        }

        return document;
    }

    public static void main(String[] args) {
        /* Test Methods */
        generateFileJsoup();
    }
}
