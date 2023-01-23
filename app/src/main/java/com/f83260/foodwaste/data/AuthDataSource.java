package com.f83260.foodwaste.data;

import com.f83260.foodwaste.data.model.LoggedInUser;
import com.f83260.foodwaste.service.UserService;
import com.f83260.foodwaste.ui.common.dto.UserDto;

import org.json.JSONException;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class AuthDataSource {

    private final UserService userService = new UserService();

    public Result login(String username, String password) {
        // https://api.jsonbin.io/v3/b/635fa63365b57a31e6a81495
        // X-MASTER-KEY $2b$10$Vl72g5hPKSe53zEGDI3GhO6CeXJ2WP/vK2vKwnvaq1u3D8jve/T/u
        try {
            LoggedInUser user = userService.login(username, password);
            if (user != null)
                return new Result.Success<>(user);
        } catch (Exception e) {
            if (e.getClass().equals(InterruptedException.class))
                Thread.currentThread().interrupt();
            return new Result.Error(new IOException("Error logging in", e));
        }

        return new Result.Error(new WrongCredentialsException("Wrong username or password."));
    }


    public Result register(String firstName, String lastName, String phoneName, String username, String password) {
        try {
            if (userService.checkIfUsernameExists(username))
                return new Result.Error(new UserRegistrationFailed("User with username " + username + "already exits."));
            if (userService.register(username, password, firstName, lastName, phoneName))
                return new Result.Success<>(userService.login(username, password));
        } catch (Exception e) {
            if (e.getClass().equals(InterruptedException.class))
                Thread.currentThread().interrupt();
            return new Result.Error(new UserRegistrationFailed("Error registering in", e));
        }
        return new Result.Error(new UserRegistrationFailed("Error registering in."));
    }

    public Result editProfile(UserDto userDto) {
        LoggedInUser updatedUser;
        try {
            updatedUser = userService.updateUser(userDto);
        } catch (JSONException e) {
            return new Result.Error(new IOException("Error updating profile", e));
        }

        // update logged in user info
        if (updatedUser != null) {
            UserRepository.getInstance(new AuthDataSource()).setLoggedInUser(updatedUser);
        }

        return new Result.Success(updatedUser);
    }
}
class WrongCredentialsException extends Exception{
    public WrongCredentialsException(String msg){
        super(msg);
    }
}

class UserRegistrationFailed extends Exception{
    public UserRegistrationFailed(String msg){
        super(msg);
    }
    public UserRegistrationFailed(String msg, Exception e){
        super(msg, e);
    }
}