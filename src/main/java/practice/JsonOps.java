package practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonOps {

    public static String getResourceFilePath(String path) {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\" + path;
    }

    public static JsonNode getEntireJsonFileAsJsonNode(String jsonFileNameInSaved) throws JsonProcessingException {
        try {
            String filePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), jsonFileNameInSaved);
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(filePath);
            return mapper.readTree(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    public static String getEntireJsonFileAsString(String jsonFileNameInSaved) throws JsonProcessingException {
        try {
            String filePath = String.format("%s\\src\\main\\resources\\saved\\%s.json", System.getProperty("user.dir"), jsonFileNameInSaved);
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(filePath);
            JsonNode jsonNode = mapper.readTree(jsonFile);
            return jsonNode.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        /* Test Methods */
    }
}