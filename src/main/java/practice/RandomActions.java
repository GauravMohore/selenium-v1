package practice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomActions {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Person {
        @JsonProperty("full_name")
        private String name;

        private String city;

        private int Age;

        private List<String> hobbies;

        public List<String> getHobbies() {
            return hobbies;
        }

        public void setHobbies(List<String> hobbies) {
            this.hobbies = hobbies;
        }

        public int getAge() {
            return Age;
        }

        public void setAge(int age) {
            Age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

    public static void main(String[] args) throws IOException {
        String fileName = "new-" + ((int) (Math.random() * 1000)) + ".txt";
        ObjectMapper mapper = new ObjectMapper();

        Person person = new Person();
        person.setName("Rock");
        person.setCity("NY");
        person.setAge(25);
        person.setHobbies(Stream.of("kidnapping", "murder").collect(Collectors.toList()));
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), person);

    }
}
