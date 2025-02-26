package main.java.dev.bontail.lab5.data;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.java.dev.bontail.lab5.validator.Check;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"x", "y"})
public class Coordinates {
    @Check(minFloat = -434)
    private Float x; //Значение поля должно быть больше -434

    @Check(notNull = true)
    private Float y; //Поле не может быть null
}
