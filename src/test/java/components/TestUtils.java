package components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.devtools.v85.dom.model.ShadowRootType;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestUtils {
    public static String getRandomGID() {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        char firstDigit = numbers.charAt(new Random().nextInt(numbers.length()));
        char thirdDigit = numbers.charAt(new Random().nextInt(numbers.length()));
        char secondChar = characters.charAt(new Random().nextInt(characters.length()));
        char fourthChar = characters.charAt(new Random().nextInt(characters.length()));
        char fifthChar = characters.charAt(new Random().nextInt(characters.length()));
        String randomString = "" + firstDigit + secondChar + thirdDigit + fourthChar + fifthChar;

        return "f879594d-4c6c-4dbc-abf0-619dc9e" + randomString;
    }

    public static HashMap<String, String> getRequestHeaders(String jsonFileNameInSaved) throws IOException, IOException {
        String jsonFilePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), jsonFileNameInSaved);
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(jsonFilePath);

        // Read JSON as a Map
        HashMap<String, Object> jsonMap = mapper.readValue(jsonFile, HashMap.class);

        // Convert Map to HashMap<String, String>, extracting keys and values as Strings
        HashMap<String, String> keyValuePairs = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
            keyValuePairs.put(entry.getKey(), entry.getValue().toString());
        }
        return keyValuePairs;
    }

    public static URL addUrlParams(URL url, String paramName, String paramValue) {
        try {
            String encodedName = URLEncoder.encode(paramName, "UTF-8");
            String encodedValue = URLEncoder.encode(paramValue, "UTF-8");
            String separator = url.getQuery() == null ? "?" : "&";
            return new URL(url + separator + encodedName + "=" + encodedValue);
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }
}
