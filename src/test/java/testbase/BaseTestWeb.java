package testbase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class BaseTestWeb {
    public WebDriver driver;
    public SoftAssert SAssert;
    public ResourceBundle resourceBundle = ResourceBundle.getBundle("config.baseConfig");
    public String baseURL = resourceBundle.getString("url");

    public static String getTestDataFilePath(String workbookName){
        return System.getProperty("user.dir") + "\\src\\main\\resources\\testdata\\"+workbookName+".xlsx";
    }

    @BeforeClass
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) throws MalformedURLException {
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                driver = new ChromeDriver(options);
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "remote":
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities.setCapability(CapabilityType.BROWSER_NAME,"chrome");
                String hubUrl = resourceBundle.getString("hubUrl");
                driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
                break;
            default:
                throw new IllegalArgumentException("Invalid browser: " + browser);
        }
        SAssert = new SoftAssert();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public void teardown(){
        if(driver!=null){
            driver.quit();
        }
    }

    public static void failedTest(Exception error){
        Assert.fail(error.getStackTrace().toString());
    }
}
