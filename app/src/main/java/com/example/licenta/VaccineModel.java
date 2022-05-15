package com.example.licenta;

import com.google.firebase.database.PropertyName;

public class VaccineModel {

    private String name;
    private String type;
    private String description;

    public VaccineModel() {}

    public VaccineModel(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
