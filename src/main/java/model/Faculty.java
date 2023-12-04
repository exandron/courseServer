package model;

import java.io.Serializable;

public class Faculty implements Serializable {
    int id;
    String name;

    public Faculty(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Faculty() {
        this.id = -1;
        this.name = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}