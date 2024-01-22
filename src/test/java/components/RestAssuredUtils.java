package components;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ResourceBundle;

public class RestAssuredUtils {
    static Response response;

    public static class ApiResult {
        private int statusCode;
        private long responseTime;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public long getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(long responseTime) {
            this.responseTime = responseTime;
        }
    }

    public synchronized static ApiResult testUrl(String url) {
        response = RestAssured.get(url);

        ApiResult apiResult = new ApiResult();
        apiResult.setStatusCode(response.getStatusCode());
        apiResult.setResponseTime(response.getTime());

        return apiResult;
    }

}
