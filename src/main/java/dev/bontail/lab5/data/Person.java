package main.java.dev.bontail.lab5.data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;


class CustomDeserializerColor extends JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();
        if (node.asText().isBlank()) {
            return null;
        }
        return Color.valueOf(node.asText());
    }
}


class CustomDeserializerCountry extends JsonDeserializer<Country> {
    @Override
    public Country deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.readValueAsTree();
        if (node.asText().isBlank()) {
            return null;
        }
        return Country.valueOf(node.asText());
    }
}


@JsonPropertyOrder({"id", "name", "creationDate", "height", "weight", "hairColor", "nationality", "coordinates", "location"})
public class Person implements Comparable<Person> {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = CustomDeserializerColor.class)
    private Color hairColor; //Поле может быть null
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = CustomDeserializerCountry.class)
    private Country nationality; //Поле может быть null
    @JsonUnwrapped(prefix = "coordinates")
    private Coordinates coordinates; //Поле не может быть null
    @JsonUnwrapped(prefix = "location")
    private Location location; //Поле не может быть null

    private static int lastId = 0;

    public static boolean checkName(String name) {
        return name != null && !name.isEmpty();
    }

    public static boolean checkHeight(Long height) {
        return height == null || height > 0;
    }

    public static boolean checkWeight(Float weight) {
        return weight == null || weight > 0;
    }

    public static boolean checkHairColor(Color color) {
        return true;
    }

    public static boolean checkNationality(Country nationality) {
        return true;
    }

    private static int generateId() {
        Person.lastId++;
        return Person.lastId;
    }

    public static void updateLastId(int id) {
        if (Person.lastId < id) {
            Person.lastId = id;
        }
    }

    public Person() {}

    public Person(String name, Long height, Float weight, Color hairColor, Country nationality, Coordinates coordinates, Location location) {
        this.id = Person.generateId();
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.weight = weight;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
        this.creationDate = new java.util.Date();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public java.util.Date getCreationDate() {
        return this.creationDate;
    }

    public Long getHeight() {
        return this.height;
    }

    public Float getWeight() {
        return this.weight;
    }

    public Color getHairColor() {
        return this.hairColor;
    }

    public Country getNationality() {
        return this.nationality;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Person other)) return false;
        if (this.getId() != other.getId()) return false;
        if (!this.getName().equals(other.getName())) return false;
        if (!this.getCreationDate().equals(other.getCreationDate())) return false;
        if (!this.getHeight().equals(other.getHeight())) return false;
        if (!this.getWeight().equals(other.getWeight())) return false;
        if (!this.getHairColor().equals(other.getHairColor())) return false;
        if (!this.getNationality().equals(other.getNationality())) return false;
        if (!this.getCoordinates().equals(other.getCoordinates())) return false;
        if (!this.getLocation().equals(other.getLocation())) return false;
        return true;
    }


    @Override
    public String toString() {
        return "Person(id=" + Long.toString(this.id) + ", creationDate=" + this.creationDate +  ", name=" + this.name + ", height=" + this.height + ", weight=" + this.weight + ", color=" + this.hairColor + ", nationality=" + this.nationality + ", coordinates=" + this.coordinates + ", location=" + this.location + ")";
    }

    public void clonePerson(Object obj){
        try{
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.getName().equals("id")){
                    continue;
                }
                field.setAccessible(true);
                field.set(this, field.get(obj));
            }
        } catch(Exception e){}
    }

    @Override
    public int compareTo(@NotNull Person p) {
        return height.compareTo(p.height);
    }
}


enum Color {
    GREEN,
    RED,
    ORANGE,
    WHITE,
    BROWN;
}


enum Country {
    USA,
    SPAIN,
    VATICAN,
    SOUTH_KOREA,
    JAPAN;
}