package testbase;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BaseTest {
    public WebDriver driver;
    public WebDriverWait wait;
    public Response response;
    public SoftAssert SAssert;
    public ResourceBundle resourceBundle = ResourceBundle.getBundle("config.base-config");
    public String baseURL = resourceBundle.getString("url");

    public static String getTestDataFilePath(String workbookName) {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\testdata\\" + workbookName + ".xlsx";
    }

    @BeforeClass
    @Parameters({"browser", "setHeadless"})
    public void setUp(@Optional("chrome") String browser, @Optional("true") boolean setHeadless) throws MalformedURLException {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (setHeadless) chromeOptions.addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (setHeadless) firefoxOptions.addArguments("--headless");
                driver = new FirefoxDriver();
                break;
            case "edge":
                /* Headless mode not directly supported in EdgeDriver */
                driver = new EdgeDriver();
                break;
            case "remote":
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
                if (setHeadless) capabilities.setCapability("--headless", true);
                String hubUrl = resourceBundle.getString("hubUrl");
                driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
                break;
            default:
                throw new IllegalArgumentException("Invalid browser: " + browser);
        }
        SAssert = new SoftAssert();
        RestAssured.baseURI = baseURL;
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected static void failedTest(Exception error) {
        Assert.fail(Arrays.toString(error.getStackTrace()));
    }

    protected URL getPageUrl(String pageName) throws MalformedURLException {
        String pageUrl = ResourceBundle.getBundle("config.url-config").getString(pageName);
        return new URL(pageUrl);
    }
}
