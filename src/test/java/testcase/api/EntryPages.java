package testcase.api;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import testbase.BaseTest;

public class EntryPages extends BaseTest {
    @Test
    public void runTest(){
        response = RestAssured.get("/news");
    }
}
