package com.f83260.foodwaste.service;


import static com.f83260.foodwaste.service.Columns.*;

import android.util.Base64;

import com.f83260.foodwaste.common.Request;
import com.f83260.foodwaste.data.AuthDataSource;
import com.f83260.foodwaste.data.UserRepository;
import com.f83260.foodwaste.data.model.LoggedInUser;
import com.f83260.foodwaste.service.util.PasswordUtil;
import com.f83260.foodwaste.ui.common.dto.UserDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class Columns {
    static final String ID = "id";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";
    static final String PHONE = "phone_number";
    static final String EMAIL = "email";
    static final String SALT = "salt";
    static final String HASH = "hash";

    private Columns(){
    }
}

public class UserService {
    private static final String API_BASE_URL = "https://api.jsonbin.io/v3";
    private static final String API_KEY = "$2b$10$Vl72g5hPKSe53zEGDI3GhO6CeXJ2WP/vK2vKwnvaq1u3D8jve/T/u";
    private static final String LOGGED_USERS = "/b/635fa63365b57a31e6a81495";

    public static void seedUsers() {
        UserService service = new UserService();

        JSONArray users = service.fecthUsers();

        assert users != null;
        if (service.fecthUsers() == null || users.length() == 0) {
            service.register("ivan@abv.bg", "123456789", "Ivan", "Petrov", "0888180533");
            service.register("mariya@abv.bg", "987654321", "Mariya", "Dimitrova", "0888180533");
        }
    }

    public LoggedInUser login(String username, String password) throws JSONException {
        JSONObject user = this.getUser(username);

        if (user == null) return null;

        byte[] salt = Base64.decode(user.getString(SALT), Base64.DEFAULT);
        byte[] hash = Base64.decode(user.getString(HASH), Base64.DEFAULT);

        if (PasswordUtil.isExpectedPassword(password.toCharArray(), salt, hash)) {
            return new LoggedInUser(user.getString(ID),user.getString(FIRST_NAME),user.getString(LAST_NAME),user.getString(PHONE), user.getString(EMAIL));

        } else {
            return null;
        }
    }

    public boolean checkIfUsernameExists(String username){
        return getUser(username) != null;
    }

    private JSONObject getUser(String username){
        JSONArray users = this.fecthUsers();

        try {
            // Simple, but inefficient
            for (int i = 0; i < users.length(); i++) {
                if (users.getJSONObject(i).getString(EMAIL).equals(username)) {
                    return users.getJSONObject(i);
                }
            }
        } catch(JSONException ex){
            ex.printStackTrace();
        }

        return null;
    }

    public LoggedInUser updateUser(UserDto userDto) throws JSONException{
        JSONArray users = this.fecthUsers();

        JSONObject user = null;

        assert users != null;

        // Simple, but inefficient
        for (int i = 0; i < users.length(); i++) {
            if (users.getJSONObject(i).getString(EMAIL).equals(userDto.getUsername())) {
                user =  users.getJSONObject(i);
            }
        }

        if (user == null){
            throw new IllegalArgumentException("user with username " + userDto.getUsername() + " doesn't exist.");
        }

        user.put(FIRST_NAME, userDto.getFirstName())
                .put(LAST_NAME,userDto.getLastName())
                .put(PHONE, userDto.getPhone());


        if (!userDto.getPass().isEmpty()){
            byte[] salt = PasswordUtil.getNextSalt();
            byte[] hash = PasswordUtil.hash(userDto.getPass().toCharArray(), salt);

            user.put(SALT, Base64.encodeToString(salt, Base64.DEFAULT))
                    .put(HASH, Base64.encodeToString(hash, Base64.DEFAULT));
        }

        String payload = "";
        payload = new JSONObject()
                .put("users", users).toString();

        Map<String, String> headers = new HashMap<>();
        headers.put("X-MASTER-KEY", API_KEY);
        Request executor = new Request(API_BASE_URL + LOGGED_USERS, "PUT", headers, payload);

        Thread thread = new Thread(executor);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        if (executor.getStatusCode() == 200){
            LoggedInUser updatedUser = new LoggedInUser(user.getString(ID),user.getString(FIRST_NAME),user.getString(LAST_NAME),user.getString(PHONE), user.getString(EMAIL));
            UserRepository.getInstance(new AuthDataSource()).setLoggedInUser(updatedUser);
            return updatedUser;

        } else {
            throw new RuntimeException("failed to update user");
        }
    }

    public boolean register(String email, String password, String firstName, String lastName, String phoneNumber) {
        JSONArray users = this.fecthUsers();

        if (users == null) {
            users = new JSONArray();
        }

        JSONObject newUser = null;
        try {
            newUser = new JSONObject()
                    .put(ID, UUID.randomUUID())
                    .put(EMAIL, email)
                    .put(FIRST_NAME, firstName)
                    .put(LAST_NAME, lastName)
                    .put(PHONE, phoneNumber);

            byte[] salt = PasswordUtil.getNextSalt();
            byte[] hash = PasswordUtil.hash(password.toCharArray(), salt);

            newUser.put(SALT, Base64.encodeToString(salt, Base64.DEFAULT))
                    .put(HASH, Base64.encodeToString(hash, Base64.DEFAULT));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        users.put(newUser);

        String payload = "";
        try {
            payload = new JSONObject()
                    .put("users", users).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("X-MASTER-KEY", API_KEY);
        Request executor = new Request(API_BASE_URL + LOGGED_USERS, "PUT", headers, payload);

        Thread thread = new Thread(executor);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return executor.getStatusCode() == 200;
    }

    private JSONArray fecthUsers() {
        // Get all users
        JSONArray users = null;

        Map<String, String> headers = new HashMap<>();
        headers.put("X-MASTER-KEY", API_KEY);
        Request executor = new Request(API_BASE_URL + LOGGED_USERS, "GET", headers, null);


        Thread thread = new Thread(executor);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        // returns the whole list of users
        JSONObject value = executor.getValue();

        try {
            users = value.getJSONObject("record").getJSONArray("users");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

}
