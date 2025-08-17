package sideprojects.dreamdecoder.domain.auth.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sideprojects.dreamdecoder.domain.auth.persistence.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserModel {
    private final String username;
    private final String email;
    private final String password;

    public static UserModel of(String username, String email, String password) {
        return new UserModel(username, email, password);
    }

    public static UserModel from(User user) {
        return new UserModel(user.getUsername(), user.getEmail(), user.getPassword());
    }
}