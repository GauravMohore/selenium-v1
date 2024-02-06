package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.JsonState;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Helper {
    public static URL getPageUrl(String pageName) throws MalformedURLException {
        String pageUrlString = ResourceBundle.getBundle("config.url-config").getString(pageName);
        return new URL(pageUrlString);
    }

    public static String getResourceFilePath(String[] pathStrings) {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources");
        String joinedPath = String.join(File.separator, pathStrings);
        return path.resolve(joinedPath).toString();
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(getResourceFilePath(new String[]{"saved", "all-news-location-details.json"})));
        ArrayNode stateNode = (ArrayNode) rootNode.get("locList");
        if (stateNode.isArray() & !stateNode.isEmpty()) {
//            mapper.readValues();
        }
    }
}
