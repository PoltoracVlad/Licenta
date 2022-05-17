package com.example.licenta;

import com.google.firebase.database.PropertyName;

public class SearchModel {

    String firstName;
    String lastName;

    public SearchModel() {}

    public SearchModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PropertyName("FirstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PropertyName("LastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
