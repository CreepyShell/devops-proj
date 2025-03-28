package app.services.interfaces;

import app.models.User;

public interface IAuthenticationService {
    User login(String password, String email);
    User register(User user);
    String hashPassword(String password, String salt);
    boolean isValidPassword(String password, String salt, String hash);
}
