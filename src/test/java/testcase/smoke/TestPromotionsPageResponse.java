package testcase.smoke;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import testbase.BaseApiTest;

public class TestPromotionsPageResponse extends BaseApiTest {

    //STEP-1: Generate user id 'x'
    @Test(priority = 1)
    public void generateUser() {
        response = RestAssured.get();
    }

    //STEP-2: Create promotion by passing 'x' parameter
    //STEP-3: Verify banner in 'My Promotions' page
}
