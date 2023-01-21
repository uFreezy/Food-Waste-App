package com.f83260.foodwaste.data;

import com.f83260.foodwaste.data.model.LoggedInUser;
import com.f83260.foodwaste.ui.common.dto.UserDto;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;

    private final AuthDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private UserRepository(AuthDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static UserRepository getInstance(AuthDataSource dataSource) {
        if (instance == null) {
            instance = new UserRepository(dataSource);
        }

        return instance;
    }

    public static boolean isLoggedIn() {
        return instance.user != null;
    }

    public LoggedInUser loggedUser() {
        return instance.user;
    }

    public void logout() {
        user = null;
    }

    public void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result login(String username, String password) {
        // handle login
        Result result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }

    public Result register(String firstName, String lastName, String phoneName, String username, String password) {
        Result user = dataSource.register(firstName, lastName, phoneName, username, password);

        if (user instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) user).getData());
        }

        return user;
    }

    public Result updateProfile(UserDto userDto) {
        return dataSource.editProfile(userDto);
    }
}