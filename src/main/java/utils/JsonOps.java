package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonOps {
    private static ObjectMapper objectMapper = getDefaultObjectmapper();
    private static ObjectMapper getDefaultObjectmapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        // configurations...
        return defaultObjectMapper;
    }

    // Method - Parsing JSON string into JSON node
    public static JsonNode parseString(String src) throws IOException {
        return objectMapper.readTree(src);
    }
}