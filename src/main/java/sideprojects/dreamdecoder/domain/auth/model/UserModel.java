package sideprojects.dreamdecoder.domain.auth.model;

import lombok.Getter;
import sideprojects.dreamdecoder.domain.auth.persistence.User;

@Getter
public class UserModel {
    private final String username;
    private final String email;
    private final String password;

    private UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static UserModel of(String username, String email, String password) {
        return new UserModel(username, email, password);
    }

    public static UserModel from(User user) {
        return new UserModel(user.getUsername(), user.getEmail(), user.getPassword());
    }
}