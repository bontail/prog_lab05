package main.java.dev.bontail.lab5.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"x", "y", "name"})
public class Location {
    private Integer x; //Поле не может быть null
    private Double y; //Поле не может быть null
    private String name; //Поле не может быть null

    public static boolean checkX(Integer x) {
        return x != null;
    }

    public static boolean checkY(Double y) {
        return y != null;
    }

    public static boolean checkName(String name) {
        return name != null;
    }

    public Location(){}

    public Location(Integer x, Double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Integer getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Location other)) return false;
        if (!this.getX().equals(other.getX())) return false;
        if (!this.getY().equals(other.getY())) return false;
        if (!this.getName().equals(other.getName())) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Location(x=" + this.x + ", y=" + this.y +  ", name=" + this.name + ")";
    }
}
