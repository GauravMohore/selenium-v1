package practice;

import javax.swing.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomActions {
    public static void main(String[] args) {
        String inputString = "chandrakona---i";
        System.out.println(getEndpointToCamelCase(inputString));
    }

    public static String getEndpointToCamelCase(String endpoint) {
        String finalString = "{{locName}} আজিৰ খবৰ. Read {{locName}} ব্ৰেকিং নিউজ only on Shuru. Also check out [{{locName}} আজিৰ সংবাদ] for the latest updates on crime, education, politics, etc.";
        String[] words = endpoint.replace('-', ' ').split("/s");
        for (int i = 0; i < words.length; i++) {
            StringBuilder currentWord = new StringBuilder(words[i]);
            currentWord.replace(0, 1, Character.toString(Character.toUpperCase(currentWord.charAt(0))));
            finalString = finalString.concat(currentWord.toString());
            if (i != words.length - 1) finalString = finalString.concat(" ");
        }
        return finalString;
    }
}
