package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WebScarper {
    static ObjectMapper objectMapper;
    ResourceBundle commonUrl = ResourceBundle.getBundle("config.url-config");

    /* Helper Methods ----------------------------------------------*/
    public static String getResourceFilePath(String path) {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\" + path;
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

    /* Scraping Logic ------------------------------------------- */

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

    // Create .json file containing all urls from /news/* hierarchy
    public static void generateAllNewsJsonFile() {
        objectMapper = new ObjectMapper();
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

    // Create .json file containing all urls from /services/* routes
    public static void generateAllServicesJsonFile() {
        objectMapper = new ObjectMapper();
        String jsonFileName = String.format("%s-%d.json", "location", System.currentTimeMillis());
        String jsonFilePath = String.format("%s/src/main/resources/saved/%s", System.getProperty("user.dir"), jsonFileName);
        System.out.println(jsonFileName);

        ObjectNode jsonRoot = objectMapper.createObjectNode();
        ArrayNode stateArray = objectMapper.createArrayNode();

        String pageUrl = "https://shuru.co.in";
        String servicesPageUrl = pageUrl + "/services";

        int maxRetries = 3;

        try {
            Document servicesDocument = connectWithRetry(servicesPageUrl, 20000, maxRetries);
            Elements servicesElements = servicesDocument.select("a[title]");
            for (Element servicesElement : servicesElements) {

            }

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

    // Create .json file containing all card details from /mandi/* routes
    public static void generateAllMandiCardsJsonFile() {
        objectMapper = new ObjectMapper();
        String jsonFileName = String.format("%s-%d.json", "mandi-cards", System.currentTimeMillis());
        String jsonFilePath = String.format("%s/src/main/resources/saved/%s", System.getProperty("user.dir"), jsonFileName);
        System.out.println(jsonFileName);

        ObjectNode jsonRoot = objectMapper.createObjectNode();
        ArrayNode mandiPageCardsArray = objectMapper.createArrayNode();
        ArrayNode stateCardsArray = objectMapper.createArrayNode();

        String pageUrl = "https://shuru.co.in";
        String rootPageUrl = pageUrl + "/mandi";

        int maxRetries = 3;

        try {
            Document rootPageDocument = connectWithRetry(rootPageUrl, 20000, maxRetries);
            Elements rootPageCards = rootPageDocument.select("main>ul>li>a");
            for (Element mandiCard : rootPageCards) {
                String mandiCardUrl = pageUrl + mandiCard.attr("href");
                ObjectNode mandiCardObject = objectMapper.createObjectNode();
                ObjectNode cardText = objectMapper.createObjectNode();
                cardText.put("mandiNameLoc", mandiCard.select("div").get(0).text().trim());
                cardText.put("mandiNameEng", mandiCard.select("div").get(1).text().trim());
                cardText.put("StateNameLoc", mandiCard.select("div").get(2).text().trim());
                cardText.put("stateNameEng", mandiCard.select("div").get(3).text().trim());
                mandiCardObject.put("cardUrl", mandiCardUrl);
                mandiCardObject.set("cardText", cardText);

                mandiPageCardsArray.add(mandiCardObject);
            }

            Elements stateCards = rootPageDocument.select("section>ul>li>a");
            for (Element stateCard : stateCards) {
                String stateUrl = pageUrl + stateCard.attr("href");
                ObjectNode stateObject = objectMapper.createObjectNode();
                ObjectNode stateCardText = objectMapper.createObjectNode();
                String stateNameLoc = Objects.requireNonNull(stateCard.select("a").first()).html().split("<br>")[0].trim();
                String stateNameEng = Objects.requireNonNull(stateCard.select("a").first()).html().split("<br>")[1].trim();
                stateCardText.put("stateNameLoc", stateNameLoc);
                stateCardText.put("stateNameEng", stateNameEng);
                stateObject.put("pageUrl", stateUrl);
                stateObject.set("cardText", stateCardText);

                Document statePageDocument = connectWithRetry(stateUrl, 20000, maxRetries);
                Elements districtCards = statePageDocument.select("section>ul>li>a");
                ArrayNode districtCardsArray = objectMapper.createArrayNode();
                if (!districtCards.isEmpty()) {
                    for (Element districtCard : districtCards) {
                        String districtUrl = pageUrl + districtCard.attr("href");
                        ObjectNode districtObject = objectMapper.createObjectNode();
                        ObjectNode districtCardText = objectMapper.createObjectNode();
                        districtCardText.put("mandiNameLoc", districtCard.select("div").get(0).text().trim());
                        districtCardText.put("mandiNameEng", districtCard.select("div").get(1).text().trim());
                        districtCardText.put("StateNameLoc", districtCard.select("div").get(2).text().trim());
                        districtCardText.put("stateNameEng", districtCard.select("div").get(3).text().trim());
                        districtObject.put("cardUrl", districtUrl);
                        districtObject.set("cardText", districtCardText);

                        districtCardsArray.add(districtObject);
                    }
                }

                stateObject.set("districtCards", districtCardsArray);
                stateCardsArray.add(stateObject);
            }

            String rootPageTitle = connectWithRetry(rootPageUrl, 20000, maxRetries).title();
            jsonRoot.put("pageTitle", rootPageTitle);
            jsonRoot.put("pageUrl", rootPageUrl);
            jsonRoot.set("cards", mandiPageCardsArray);
            jsonRoot.set("stateCards", stateCardsArray);

            // File Creation
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), jsonRoot);
                System.out.println("Success");
            } catch (Exception e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to the website: " + e.getMessage());
        }
    }

    // Create .json file containing urls from /mandi/* routes
    public static void generateAllMandiJsonFile() {
        objectMapper = new ObjectMapper();
        String jsonFileName = String.format("%s-%d.json", "mandi", System.currentTimeMillis());
        String jsonFilePath = String.format("%s/src/main/resources/saved/%s", System.getProperty("user.dir"), jsonFileName);
        System.out.println(jsonFileName);

        ObjectNode jsonRoot = objectMapper.createObjectNode();
        ArrayNode stateArray = objectMapper.createArrayNode();

        String pageUrl = "https://shuru.co.in";
        String rootPageUrl = pageUrl + "/mandi";

        int maxRetries = 3;

        try {
            Document rootPageDocument = connectWithRetry(rootPageUrl, 20000, maxRetries);

            // Getting state page urls
            rootPageDocument.select("section>ul>li>a")
                    .stream()
                    .map(card -> pageUrl + card.attr("href"))
                    .toList()
                    .forEach(stateUrl -> {
                        ObjectNode stateObject = objectMapper.createObjectNode();
                        try {
                            Document statePageDocument = connectWithRetry(stateUrl, 15000, maxRetries);
                            String statePageTitle = statePageDocument.title();
                            String statePageDescription = statePageDocument.select("meta[name=description]")
                                    .get(0)
                                    .attr("content");
                            stateObject.put("pageUrl", stateUrl);
                            stateObject.put("pageLevel", "STATE");
                            stateObject.put("pageTitle", statePageTitle);
                            stateObject.put("pageDescription", statePageDescription);

                            //getting district page urls
                            ArrayNode districtArray = objectMapper.createArrayNode();
                            statePageDocument.select("section>ul>li>a")
                                    .stream()
                                    .map(card -> pageUrl + card.attr("href"))
                                    .forEach(districtUrl -> {
                                        ObjectNode districtObject = objectMapper.createObjectNode();
                                        try {
                                            Document districtPageDocument = connectWithRetry(districtUrl, 15000, maxRetries);
                                            String districtPageTitle = districtPageDocument.title();
                                            String districtPageDescription = districtPageDocument.select("meta[name=description]")
                                                    .get(0)
                                                    .attr("content");
                                            districtObject.put("pageUrl", districtUrl);
                                            districtObject.put("pageLevel", "DISTRICT");
                                            districtObject.put("pageTitle", districtPageTitle);
                                            districtObject.put("pageDescription", districtPageDescription);

                                            districtArray.add(districtObject);
                                        } catch (Exception err) {
                                            throw new RuntimeException(err.getMessage());
                                        }
                                    });

                            stateObject.set("PageRoutes", districtArray);
                            stateArray.add(stateObject);
                        } catch (Exception err) {
                            throw new RuntimeException(err.getMessage());
                        }
                    });

            String rootPageTitle = rootPageDocument.title();
            String rootPageDescription = rootPageDocument.select("meta[name=description]")
                    .get(0)
                    .attr("content");
            jsonRoot.put("pageUrl", rootPageUrl);
            jsonRoot.put("pageLevel", "ROOT");
            jsonRoot.put("pageTitle", rootPageTitle);
            jsonRoot.put("pageDescription", rootPageDescription);
            jsonRoot.set("pageRoutes", stateArray);

            // File Creation
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), jsonRoot);
                System.out.println("Success");
            } catch (Exception e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error connecting to the website: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        /* Test Methods */
        generateAllMandiJsonFile();

    }
}
