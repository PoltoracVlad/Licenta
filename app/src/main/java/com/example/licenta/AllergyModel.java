package com.example.licenta;

import com.google.firebase.database.PropertyName;

public class AllergyModel {

    private String name;
    private String symptoms;
    private String description;

    public AllergyModel() {}

    public AllergyModel(String name, String symptoms, String description) {
        this.name = name;
        this.symptoms = symptoms;
        this.description = description;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("symptoms")
    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    @PropertyName("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
