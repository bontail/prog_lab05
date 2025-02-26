package main.java.dev.bontail.lab5.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import main.java.dev.bontail.lab5.validator.Check;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;


/**
 * Deserializer for Color
 */
class CustomDeserializerColor extends JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        if (node.asText().isBlank()) {
            return null;
        }
        return Color.valueOf(node.asText());
    }
}


/**
 * Deserializer for Country
 */
class CustomDeserializerCountry extends JsonDeserializer<Country> {
    @Override
    public Country deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = jsonParser.readValueAsTree();
        if (node.asText().isBlank()) {
            return null;
        }
        return Country.valueOf(node.asText());
    }
}


/**
 * Data class
 * autogenerate id and creationDate
 */
@Getter
@EqualsAndHashCode
@ToString
@JsonPropertyOrder({"id", "name", "creationDate", "height", "weight", "hairColor", "nationality", "coordinates", "location"})
public class Person implements Comparable<Person> {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Check(notEmptyString = true)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Check(minLong = 0)
    private Long height; //Поле может быть null, Значение поля должно быть больше 0

    @Check(minFloat = 0)
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0

    @Check(isEnum = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = CustomDeserializerColor.class)
    private Color hairColor; //Поле может быть null

    @Check(isEnum = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = CustomDeserializerCountry.class)
    private Country nationality; //Поле может быть null

    @JsonUnwrapped(prefix = "coordinates")
    private Coordinates coordinates; //Поле не может быть null

    @JsonUnwrapped(prefix = "location")
    private Location location; //Поле не может быть null

    private static int lastId = 0;

    private static int generateId() {
        Person.lastId++;
        return Person.lastId;
    }

    public static void updateLastId(int id) {
        if (Person.lastId < id) {
            Person.lastId = id;
        }
    }

    public Person(){}

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

    public void setFrom(Object obj) {
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                if (field.getName().equals("id")) {
                    continue;
                }
                field.setAccessible(true);
                field.set(this, field.get(obj));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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