package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonOps {
    private static ObjectMapper objectMapper;
    private static ObjectMapper getDefaultObjectmapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        // configurations...
        return defaultObjectMapper;
    }
}
