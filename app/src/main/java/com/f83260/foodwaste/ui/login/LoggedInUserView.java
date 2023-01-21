package com.f83260.foodwaste.ui.login;


public class LoggedInUserView {
    private final String displayName;

    public LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}