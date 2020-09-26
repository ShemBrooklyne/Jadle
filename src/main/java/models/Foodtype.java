package models;

import java.util.Objects;

public class Foodtype {

    private String name;
    private int id;

    public Foodtype(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Foodtype)) return false;
        Foodtype foodtype = (Foodtype) o;
        return id == foodtype.id &&
                Objects.equals(name, foodtype.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

}
