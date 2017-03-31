package entities;

import adapters.Convertable;
import beans.User;
import beans.builders.UserBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by u624 on 3/24/17.
 */
@Entity
@Table(name = "users")
public class UserEntity implements Serializable, Convertable<User> {
    @Id
    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "PASSWORD")
    private String passwordHashCode;

    public UserEntity() {
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
    public User convert() {
        return new UserBuilder()
                .setUsername(username)
                .setFullName(fullName)
                .setPasswordHashCode(passwordHashCode)
                .build();
    }
}
