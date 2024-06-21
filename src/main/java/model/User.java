package model;

import db.DataBase;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User() {}

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public void signUp(Map<String, String> queryParams) {
        User user = new User(queryParams.get("userId"), queryParams.get("password"), queryParams.get("name"), queryParams.get("email"));
        DataBase.addUser(user);
    }

    public boolean signIn(Map<String, String> signInParams) {
        String userIdParam = signInParams.get("userId");
        String passwordParam = signInParams.get("password");

        User user = DataBase.findUserByUserId(userIdParam);
        if (user == null) {
            return false;
        }

        if (user.password.equals(passwordParam)) {
            return true;
        }

        return false;
    }
}


