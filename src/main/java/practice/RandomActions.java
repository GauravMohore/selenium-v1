package practice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class RandomActions {
    public static void main(String[] args) {
        print();

    }

    static void print() {
        WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            driver.get("https://shuru.co.in/");
            List<WebElement> footerSections = driver.findElements(By.xpath("//footer/div/div[2]/div"));

            for (int i = 0; i < footerSections.size(); i++) {
                String secTitle = footerSections.get(i).findElement(By.xpath("./div[1]")).getText();
                List<WebElement> linkList = footerSections.get(i).findElements(By.xpath("./div[2]/a"));
                for (int j = 0; j < linkList.size(); j++) {

                    System.out.println(String.format("%d.%d", i + 1, j + 1));
                }
            }
        } finally {
            driver.quit();
        }
    }
}
