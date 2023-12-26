package testcase.api;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import testbase.BaseTestApi;

public class EntryPages extends BaseTestApi {
    @Test
    public void runTest(){
        System.out.printf(RestAssured.baseURI);
    }

}
