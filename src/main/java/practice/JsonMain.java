package practice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import practice.JsonOps;
import utils.WebScraper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JsonMain {

    public static void temp(String[] args) throws IOException {
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

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            String parentEP = "news/madhya-pradesh";
            String parentUrl = "https://Shuru.co.in/news/madhya-pradesh";
            driver.get(parentUrl);
            wait.until(ExpectedConditions.urlContains(parentEP));

            List<WebElement> links = driver.findElements(By.cssSelector("a[class=community-next-link]"));
            for (WebElement link : links) {
                String linkStr = link.getAttribute("href");
                driver.navigate().to(linkStr);

                try {
                    // Refind the link element to avoid StaleElementReferenceException
                    link = driver.findElement(By.cssSelector("a[class=community-next-link]"));
                    wait.until(ExpectedConditions.urlToBe(linkStr));
                    System.out.println(driver.getCurrentUrl());
                } finally {
                    if (!Objects.equals(driver.getCurrentUrl(), parentUrl))
                        driver.navigate().back();
                }
            }
        } finally {
            driver.quit();
        }
    }
}
