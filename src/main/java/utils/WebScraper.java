package utils;

import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.ResourceBundle;

public class WebScraper {
    private static ResourceBundle resource = ResourceBundle.getBundle("config.baseConfig");
    private static String baseURL = resource.getString("url");
    private static WebDriver driver;


    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get(baseURL+"news");
        try {
            List<WebElement> states = driver.findElements(By.cssSelector("div li a[title]"));
            states.forEach(state-> System.out.printf("%s -> %s%n",state.getText(),state.getAttribute("href")));
        }finally {
            if(driver!=null) driver.quit();
        }
    }
}
