package components;

import com.beust.ah.A;
import common.ApiResult;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ResourceBundle;

public class RestAssuredUtils {
    static Response response;

    public synchronized static ApiResult testUrl(String url) {
        response = RestAssured.get(url);

        ApiResult apiResult = new ApiResult();
        apiResult.setStatusCode(response.getStatusCode());
        apiResult.setResponseTime(response.getTime());
        apiResult.setPageSource(response.getBody().asString());

        return apiResult;
    }


}
