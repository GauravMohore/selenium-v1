package testbase;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.asserts.SoftAssert;

import java.util.ResourceBundle;

public class BaseTestApi {
    public Response response;
    public SoftAssert SAssert;
    public ResourceBundle resourceBundle =ResourceBundle.getBundle("config.baseConfig");

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = resourceBundle.getString("url");
        SAssert = new SoftAssert();
    }
}
