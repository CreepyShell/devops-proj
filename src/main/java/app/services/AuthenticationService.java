package app.services;

import app.models.PlaneDb;
import app.models.User;
import app.services.custom_exceptions.ValidationException;
import app.services.interfaces.IAuthenticationService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

public class AuthenticationService implements IAuthenticationService {
    private final PlaneDb db;

    public AuthenticationService(PlaneDb db) {
        this.db = db;
    }

    @Override
    public User login(String password, String email) {
        User user = db.getUsers().stream().filter(u -> Objects.equals(u.getEmail(), email)).findAny().orElse(null);
        if (user == null)
            throw new IllegalArgumentException("Did not find user with given email");
        if (!isValidPassword(password, user.getSalt(), user.getPasswordHash())) {
            throw new ValidationException("Password is not correct");
        }
        return user;
    }

    @Override
    public User register(User user) {
        List<User> users = db.getUsers();
        if (users.stream().anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail()))) {
            throw new IllegalArgumentException("There is a user with same email");
        }

        if (!validateUser(user).isEmpty() || user.getPasswordHash().length() < 8) {
            throw new ValidationException("Invalid user: " + validateUser(user) + " or password too weak");
        }

        user.setPassword(this.hashPassword(user.getPasswordHash(), user.getSalt()));
        users.add(user);
        db.setUsers(users);
        return user;
    }

    @Override
    public String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            return salt;
        }
    }

    @Override
    public boolean isValidPassword(String password, String salt, String hash) {
        return Objects.equals(hashPassword(password, salt), hash);
    }

    private String validateUser(User user) {
        if (user.getEmail().length() < 4 || user.getEmail().length() > 40)
            return "Email is invalid. Must be between 4 and 40";

        if (user.getFirstName().length() < 2 || user.getFirstName().length() > 20)
            return "First name is invalid. Must be between 2 and 20";

        if (user.getLastName().length() < 2 || user.getLastName().length() > 20)
            return "Last name is invalid. Must be between 2 and 20";
        return "";

    }
}
