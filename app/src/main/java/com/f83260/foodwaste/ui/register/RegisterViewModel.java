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


public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> loginResult = new MutableLiveData<>();
    private UserRepository loginRepository;

    RegisterViewModel(UserRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return loginFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return loginResult;
    }


    // TODO: use dto here
    public void register(String firstName, String lastName, String phoneName, String username, String password){
        // TODO
        Result<LoggedInUser> result = loginRepository.register(firstName, lastName, phoneName, username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new RegisterResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new RegisterFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new RegisterFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new RegisterFormState(true));
        }
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