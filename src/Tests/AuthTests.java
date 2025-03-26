import org.junit.Test;
import org.mockito.Mockito;
import com.models.PlaneDb;
import com.models.User;
import com.services.AuthenticationService;
import com.services.custom_exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;

public class AuthTests {

    public AuthTests() {
    }

    @Test
    public void when_user_register_then_new_user_created() {
        List<User> users = new ArrayList<>();

        User testUser = new User();
        String testEmail = "admin@gmail.com";
        String testPass = "test_pass1234";
        testUser.setEmail(testEmail);
        testUser.setPassword(testPass);

        PlaneDb planeDb = Mockito.mock(PlaneDb.class);
        doAnswer(a -> users).when(planeDb).getUsers();
        doAnswer(a -> {
            users.add(testUser);
            return "";
        }).when(planeDb).setUsers(users);

        AuthenticationService authenticationService = new AuthenticationService(planeDb);
        authenticationService.register(testUser);

        User authUser = authenticationService.login(testPass, testEmail);

        assertEquals(testUser.getEmail(), authUser.getEmail());
        assertNotNull(authUser.getSalt());
        assertNotNull(authUser.getPasswordHash());
        assertEquals(1000, authUser.getBalance(), 000.1);
    }

    @Test
    public void when_user_register_with_existing_email_then_exception_thrown() {
        List<User> users = new ArrayList<>();

        User testUser = new User();
        String testEmail = "admin@gmail.com";
        String testPass = "test_pass1234";
        testUser.setEmail(testEmail);
        testUser.setPassword(testPass);
        users.add(testUser);

        PlaneDb planeDb = Mockito.mock(PlaneDb.class);
        doAnswer(a -> users).when(planeDb).getUsers();
        doAnswer(a -> {
            users.add(testUser);
            return "";
        }).when(planeDb).setUsers(users);

        AuthenticationService authenticationService = new AuthenticationService(planeDb);
        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(testUser));
    }

    @Test
    public void when_user_register_with_invalid_data_then_validationException_thrown() {
        List<User> users = new ArrayList<>();

        User testUser = new User();
        String testEmail = "admin@gmail.com";
        String testPass = "1234";
        testUser.setEmail(testEmail);
        testUser.setPassword(testPass);

        PlaneDb planeDb = Mockito.mock(PlaneDb.class);
        doNothing().when(planeDb).setUsers(users);

        AuthenticationService authenticationService = new AuthenticationService(planeDb);
        assertThrows(ValidationException.class, () -> authenticationService.register(testUser));
    }

    @Test
    public void when_user_login_with_incorrect_password_then_validationException_thrown() {
        List<User> users = new ArrayList<>();

        User testUser = new User();
        String testEmail = "admin@gmail.com";
        String testPass = "test_pass1234";
        testUser.setEmail(testEmail);
        testUser.setPassword(testPass);

        PlaneDb planeDb = Mockito.mock(PlaneDb.class);
        doAnswer(a -> users).when(planeDb).getUsers();
        doAnswer(a -> {
            users.add(testUser);
            return "";
        }).when(planeDb).setUsers(users);

        AuthenticationService authenticationService = new AuthenticationService(planeDb);
        authenticationService.register(testUser);

        String wrongPass = "test_pass12345";
        assertThrows(ValidationException.class, () -> authenticationService.login(wrongPass, testEmail));
    }
}
