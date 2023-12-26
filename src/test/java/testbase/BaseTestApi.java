package testbase;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import java.util.ResourceBundle;

public class BaseTestApi {

    public ResourceBundle resourceBundle =ResourceBundle.getBundle("config.baseConfig");
    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = resourceBundle.getString("url");
    }
}
