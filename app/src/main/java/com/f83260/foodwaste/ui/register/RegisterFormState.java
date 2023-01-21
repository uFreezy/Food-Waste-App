package com.f83260.foodwaste.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
public class RegisterFormState {

    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer phoneError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    RegisterFormState(boolean isDataValid) {
        this.firstNameError = null;
        this.lastNameError = null;
        this.phoneError = null;
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }


    @Nullable
    public Integer getFirstNameError() {
        return firstNameError;
    }

    @Nullable
    public Integer getLastNameError() {
        return lastNameError;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    public void setFirstNameError(@Nullable Integer firstNameError) {
        this.firstNameError = firstNameError;
    }

    public void setLastNameError(@Nullable Integer lastNameError) {
        this.lastNameError = lastNameError;
    }

    public void setPhoneError(@Nullable Integer phoneError) {
        this.phoneError = phoneError;
    }

    public void setUsernameError(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
    }

    public void setPasswordError(@Nullable Integer passwordError) {
        this.passwordError = passwordError;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }
}