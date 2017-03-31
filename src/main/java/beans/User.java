package beans;

import adapters.Convertable;
import entities.UserEntity;
import entities.builders.UserEntityBuilder;

import java.io.Serializable;

/**
 * Created by u624 on 3/24/17.
 */
public class User implements Serializable, Convertable<UserEntity> {
    private String username;
    private String fullName;
    private String passwordHashCode;

    public User() {
        /* default constructor */
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordHashCode() {
        return passwordHashCode;
    }

    public void setPasswordHashCode(String passwordHashCode) {
        this.passwordHashCode = passwordHashCode;
    }

    @Override
    public UserEntity convert() {
        return new UserEntityBuilder()
                .setUsername(username)
                .setFullName(fullName)
                .setPasswordHashCode(passwordHashCode)
                .build();
    }
}
