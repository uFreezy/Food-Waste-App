package com.f83260.foodwaste.ui.register.dto;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.f83260.foodwaste.databinding.ActivityRegisterBinding;
import com.f83260.foodwaste.ui.register.RegisterFormState;


public class RegistrationFromDto {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private ProgressBar loadingProgressBar;

    public RegistrationFromDto(EditText firstNameEditText, EditText lastNameEditText, EditText phoneNameEditText, EditText usernameEditText, EditText passwordEditText, Button registerButton, ProgressBar loadingProgressBar) {
        this.firstNameEditText = firstNameEditText;
        this.lastNameEditText = lastNameEditText;
        this.phoneNameEditText = phoneNameEditText;
        this.usernameEditText = usernameEditText;
        this.passwordEditText = passwordEditText;
        this.registerButton = registerButton;
        this.loadingProgressBar = loadingProgressBar;
    }

    public RegistrationFromDto(ActivityRegisterBinding binding){
        this.firstNameEditText = binding.firstName;
        this.lastNameEditText = binding.lastName;
        this.phoneNameEditText = binding.phone;
        this.usernameEditText = binding.username;
        this.passwordEditText = binding.password;
        this.registerButton  = binding.register;
        this.loadingProgressBar = binding.loading;
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

    public Button getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(Button registerButton) {
        this.registerButton = registerButton;
    }

    public ProgressBar getLoadingProgressBar() {
        return loadingProgressBar;
    }

    public void setLoadingProgressBar(ProgressBar loadingProgressBar) {
        this.loadingProgressBar = loadingProgressBar;
    }


}

