package com.example.licenta;

import com.google.firebase.database.PropertyName;

public class MedicalProfile {

    private String name;
    private String type;

    public MedicalProfile() {}

    public MedicalProfile(String name, String type) {
        this.name = name;
        this.type = type;
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
}
