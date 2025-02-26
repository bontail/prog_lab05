package main.java.dev.bontail.lab5.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.java.dev.bontail.lab5.validator.Check;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"x", "y", "name"})
public class Location {
    @Check(notNull = true)
    private Integer x; //Поле не может быть null

    @Check(notNull = true)
    private Double y; //Поле не может быть null

    @Check(notNull = true)
    private String name; //Поле не может быть null

}
