package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LocationScraper {

    public static void main(String[] args) {
        // Set the path to the ChromeDriver executable

        // Initialize the WebDriver
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            // Save all states first
            ArrayNode statesArray = saveStates(driver);

            // Save the JSON data to a file
            saveJsonToFile(statesArray, "output.json");

        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }

    private static ArrayNode saveStates(WebDriver driver) {
        ArrayNode statesArray = new ObjectMapper().createArrayNode();

        // Go to the specified URL
        driver.get("https://shuru.co.in/news");

        // Find list of elements with the given CSS selector
        java.util.List<WebElement> stateElements = driver.findElements(By.cssSelector("li>a[title]"));

        // Iterate through state elements
        for (WebElement stateElement : stateElements) {
            // Create a state object
            ObjectNode stateObject = new ObjectMapper().createObjectNode();

            // Extract information from state element
            String locName = stateElement.getText();
            String locUrl = stateElement.getAttribute("href");

            // Add values to state object
            stateObject.put("locName", locName);
            stateObject.put("locUrl", locUrl);
            stateObject.put("locLevel", "STATE");

            // Add state object to states array
            statesArray.add(stateObject);
        }

        return statesArray;
    }

    private static void saveJsonToFile(ArrayNode node, String filename) {
        try {
            // Create a root object to hold the states array
            ObjectNode root = new ObjectMapper().createObjectNode();
            root.set("states", node);

            // Write the JSON data to a file
            new ObjectMapper().writeValue(new File(filename), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
