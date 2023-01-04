package com.f83260.foodwaste.ui.common.dto;

import android.widget.EditText;

public class UserProfileFormDto {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;

    protected UserProfileFormDto(){
    }

    public UserProfileFormDto(EditText firstNameEditText, EditText lastNameEditText, EditText phoneNameEditText, EditText usernameEditText, EditText passwordEditText) {
        this.firstNameEditText = firstNameEditText;
        this.lastNameEditText = lastNameEditText;
        this.phoneNameEditText = phoneNameEditText;
        this.usernameEditText = usernameEditText;
        this.passwordEditText = passwordEditText;
    }

    public EditText getFirstNameEditText() {
        return firstNameEditText;
    }

    public void setFirstNameEditText(EditText firstNameEditText) {
        this.firstNameEditText = firstNameEditText;
    }

    public EditText getLastNameEditText() {
        return lastNameEditText;
    }

    public void setLastNameEditText(EditText lastNameEditText) {
        this.lastNameEditText = lastNameEditText;
    }

    public EditText getPhoneNameEditText() {
        return phoneNameEditText;
    }

    public void setPhoneNameEditText(EditText phoneNameEditText) {
        this.phoneNameEditText = phoneNameEditText;
    }

    public EditText getUsernameEditText() {
        return usernameEditText;
    }

    public void setUsernameEditText(EditText usernameEditText) {
        this.usernameEditText = usernameEditText;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }
}
