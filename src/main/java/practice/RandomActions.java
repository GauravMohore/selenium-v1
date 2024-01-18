package practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RandomActions {

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();

        File jsonFile = new File("output.json");
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        WebDriver driverRoot = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        WebDriver driverRoot = new ChromeDriver();
        driverRoot.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        Map<String, String> stateUrlInfo = new LinkedHashMap<>();
        try {
            driverRoot.get("http://shuru.co.in./news");
            driverRoot.findElements(By.cssSelector("a[title]")).forEach(element -> stateUrlInfo.put(element.getText(), element.getAttribute("href")));
        } finally {
            driverRoot.quit();
        }
        System.out.println(stateUrlInfo.size());
//        stateUrlInfo.forEach((k, v) -> System.out.println(k + " -> " + v));
        stateUrlInfo.forEach((sName, sUrl) -> {
//            WebDriver driverNode = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
            WebDriver driverNode = new ChromeDriver();
            try {
                driverNode.get(sUrl);
                Map<String, String> districtUrlInfo = new LinkedHashMap<>();
                driverNode.findElements(By.cssSelector("div[class=list-items]>a")).forEach((element -> districtUrlInfo.put(element.getText(), element.getAttribute("href"))));
                districtUrlInfo.forEach((k, v) -> System.out.println(k + " --> " + v));
            } finally {
                driverNode.quit();
            }
        });
    }
}
