package testcase.web;

import groovyjarjarantlr4.v4.codegen.model.SrcOp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import testbase.BaseTest;
import utils.DataProviders;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import static testbase.BaseTest.getTestDataFilePath;

public class TestMetaTags {
    WebDriver driver;

    @BeforeMethod
    public void methodSetup() {
        driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
    }

    @AfterMethod
    public void afterMethodSetup() {
        driver.quit();
    }


    @DataProvider(name = "state")
    private Object[][] StateMetaTags() {
        String sheetName = "State";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_MetadataUpdate"), sheetName);
    }

    @DataProvider(name = "district")
    private Object[][] districtMetaTags() {
        String sheetName = "District";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_MetadataUpdate"), sheetName);
    }

    @DataProvider(name = "city")
    private Object[][] cityMetaTags() {
        String sheetName = "City";
        return DataProviders.readFromExcel(getTestDataFilePath("TD_MetadataUpdate"), sheetName);
    }

    @Test(priority = 1, dataProvider = "state")
    public void TestState(String category, String givenUrl, String expectedTitle, String expectedDescription, String expectedH1, String expectedH2, String expectedLanguage
    ) throws IOException {
        String fileName = "checkStateData";
        executeTest(fileName, category, givenUrl, expectedTitle, expectedDescription, expectedH1, expectedH2, expectedLanguage);
    }

    @Test(priority = 2, dataProvider = "district")
    public void TestDistrict(String category, String givenUrl, String expectedTitle, String expectedDescription, String expectedH1, String expectedH2, String expectedLanguage
    ) throws IOException {
        String fileName = "checkDistrictData";
        executeTest(fileName, category, givenUrl, expectedTitle, expectedDescription, expectedH1, expectedH2, expectedLanguage);
    }

    @Test(priority = 3, dataProvider = "city")
    public void TestCity(String category, String givenUrl, String expectedTitle, String expectedDescription, String expectedH1, String expectedH2, String expectedLanguage
    ) throws IOException {
        String fileName = "checkCityData";
        executeTest(fileName, category, givenUrl, expectedTitle, expectedDescription, expectedH1, expectedH2, expectedLanguage);
    }

    private synchronized void executeTest(String fileName, String category, String givenUrl, String expectedTitle, String expectedDescription, String expectedH1, String expectedH2, String expectedLanguage) throws IOException {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\temp-data\\" + fileName + ".txt";
        Document document = Jsoup.connect(givenUrl).get();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Create the file if it doesn't exist
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            String actualTitle = document.title();
            String actualDescription = document.select("meta[name=\"description\"]").get(0).attr("content");
            String actualH1 = document.select("h1[class]").text();
            String actualH2 = document.select("footer>div>h2[class]").text();
            // Append data to the file in a for loop
            String compareData = String.format("page url : %s\nE--> %s\nA--> %s\n\n\n", givenUrl, expectedTitle, actualTitle)
                    .concat(String.format("page url : %s\nE--> %s\nA--> %s\n\n\n", givenUrl, expectedDescription, actualDescription))
                    .concat(String.format("page url : %s\nE--> %s\nA--> %s\n\n\n", givenUrl, expectedH1, actualH1))
                    .concat(String.format("page url : %s\nE--> %s\nA--> %s\n\n\n", givenUrl, expectedH2, actualH2))
                    .concat("________________________________________\n\n\n");
            writer.write(compareData);
        }
    }
}
