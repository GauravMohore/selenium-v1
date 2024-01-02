package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonOps {
    private static final ObjectMapper objectMapper = getDefaultObjectmapper();

    static ObjectMapper getDefaultObjectmapper() {
        // configurations...
        return new ObjectMapper();
    }

    // Method - Parsing JSON string into JSON node
    public static JsonNode parseString(String src) throws IOException {
        return objectMapper.readTree(src);
    }
}