package za.co.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginTest {
    private Login login;

    @BeforeEach void setUp() { login = new Login(); }

    @Test void usernameCorrectlyFormatted() { assertTrue(login.checkUserName("kyl_1")); }
    @Test void usernameIncorrectlyFormatted() { assertFalse(login.checkUserName("kyle")); }
    @Test void passwordMeetsComplexity() { assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!")); }
    @Test void passwordDoesNotMeetComplexity() { assertFalse(login.checkPasswordComplexity("password")); }
    @Test void cellNumberCorrectlyFormatted() { assertTrue(login.checkCellPhoneNumber("+27838968976")); }
    @Test void cellNumberIncorrectlyFormatted() { assertFalse(login.checkCellPhoneNumber("08966553")); }
    @Test void loginSuccessful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kamo", "Mokoena");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }
    @Test void loginFailed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kamo", "Mokoena");
        assertFalse(login.loginUser("kyl_1", "wrongpass"));
    }
    @Test void registrationRejectsInvalidUsername() {
        assertTrue(login.registerUser("kyle", "Ch&&sec@ke99!", "+27838968976", "Kamo", "Mokoena").startsWith("Username is not correctly"));
    }
}
