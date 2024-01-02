package utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class JsonMain {
    public static void main(String[] args) throws IOException {
        String jsonString = "{ \"title\" : \"Rock\" }";
        JsonNode node = JsonOps.parseString(jsonString);
        System.out.println(node.get("title").asText());
    }
}
