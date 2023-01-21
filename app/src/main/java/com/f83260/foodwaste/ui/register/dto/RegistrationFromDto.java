package com.f83260.foodwaste.ui.register.dto;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.f83260.foodwaste.databinding.ActivityRegisterBinding;
import com.f83260.foodwaste.ui.common.dto.UserProfileFormDto;


public class RegistrationFromDto  extends UserProfileFormDto {
    private Button registerButton;
    private ProgressBar loadingProgressBar;

    public RegistrationFromDto(ActivityRegisterBinding binding){
        super();
        this.setFirstNameEditText(binding.registerForm.firstName);
        this.setLastNameEditText(binding.registerForm.lastName);
        this.setPhoneNameEditText(binding.registerForm.phone);
        this.setUsernameEditText(binding.registerForm.username);
        this.setPasswordEditText(binding.registerForm.password);

        this.registerButton  = binding.register;
        this.loadingProgressBar = binding.loading;
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

