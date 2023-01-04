package com.f83260.foodwaste.ui.common.dto;

import com.f83260.foodwaste.databinding.ProfileBinding;

public class UserDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String username;
    private String pass;

    public UserDto(String firstName, String lastName, String phone, String username, String pass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.pass = pass;
    }

    public UserDto(ProfileBinding userBinding){
        this.firstName = userBinding.firstName.getText().toString();
        this.lastName = userBinding.lastName.getText().toString();
        this.phone = userBinding.phone.getText().toString();
        this.username = userBinding.username.getText().toString();
        this.pass = userBinding.password.getText().toString();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
