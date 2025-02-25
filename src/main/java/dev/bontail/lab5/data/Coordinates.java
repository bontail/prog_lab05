package main.java.dev.bontail.lab5.data;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"x", "y"})
public class Coordinates {
    private Float x; //Значение поля должно быть больше -434
    private Float y; //Поле не может быть null

    public static boolean checkX(Float x) {
        return x > -434;
    }

    public static boolean checkY(Float y) {
        return y != null;
    }

    public Coordinates(){}

    public Coordinates(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coordinates other)) return false;
        if (!this.getX().equals(other.getX())) return false;
        if (!this.getY().equals(other.getY())) return false;
        return true;
    }


    @Override
    public String toString() {
        return "Coordinates(x=" + this.x + ", y=" + this.y + ")";
    }
}