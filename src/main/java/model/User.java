package model;

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

    public User signUp(String queryString) {
        String[] params = queryString.split("&");
        Map<String, String> informations = new HashMap<>();

        for (int i = 0; i < params.length; i++) {
            int equalIdx = params[i].indexOf("=");
            informations.put(params[i].substring(0, equalIdx), params[i].substring(equalIdx+1));
        }

        User user = new User(informations.get("userId"), informations.get("password"), informations.get("name"), informations.get("email"));

        return user;
    }
}


