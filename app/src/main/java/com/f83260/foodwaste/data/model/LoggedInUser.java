package com.f83260.foodwaste.data.model;

public class LoggedInUser {
    private final String userId;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String displayName;

    public LoggedInUser(String userId, String firstName, String lastName, String phone, String displayName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisplayName() {
        return displayName;
    }
}