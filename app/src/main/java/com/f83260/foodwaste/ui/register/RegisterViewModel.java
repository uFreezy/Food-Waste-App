package com.f83260.foodwaste.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.f83260.foodwaste.R;
import com.f83260.foodwaste.data.Result;
import com.f83260.foodwaste.data.UserRepository;
import com.f83260.foodwaste.data.model.LoggedInUser;
import com.f83260.foodwaste.ui.login.LoggedInUserView;
import com.f83260.foodwaste.ui.register.dto.RegistrationFromDto;


public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private UserRepository userRepository;

    RegisterViewModel(UserRepository loginRepository) {
        this.userRepository = loginRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }


    // TODO: use dto here
    public void register(String firstName, String lastName, String phoneName, String username, String password){
        Result result = userRepository.register(firstName, lastName, phoneName, username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            registerResult.setValue(new RegisterResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void registerDataChanged(RegistrationFromDto form) {
        RegisterFormState formState = new RegisterFormState(true);

        if (!isNameValid(form.getFirstNameEditText().getText().toString())){
            formState.setFirstNameError(R.string.invalid_first_name);
            formState.setDataValid(false);
        }
        if (!isNameValid(form.getLastNameEditText().getText().toString())){
            formState.setLastNameError(R.string.invalid_last_name);
            formState.setDataValid(false);
        }
        if (!isUserNameValid(form.getUsernameEditText().getText().toString())) {
            formState.setUsernameError(R.string.invalid_username);
            formState.setDataValid(false);
        }
        if (!isPasswordValid(form.getPasswordEditText().getText().toString())) {
            formState.setPasswordError(R.string.invalid_password);
            formState.setDataValid(false);
        }

        registerFormState.setValue(formState);
    }

    private boolean isNameValid(String name){
        return !name.trim().isEmpty();
    }


    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }

        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}