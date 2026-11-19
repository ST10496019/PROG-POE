package za.co.quickchat;

import java.util.regex.Pattern;

/** Handles registration and authentication for QuickChat. */
public class Login {
    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;

    // Regex reference: ICASA, Numbering Plan Regulations (2016), which specifies country code 27;
    // international numbers use +27 followed by the national significant number (without the 0 prefix).
    // https://www.icasa.org.za/uploads/files/NumberingPlanReg.pdf
    // South African international format used here: +27 followed by nine digits.
    private static final Pattern SA_CELL_PATTERN = Pattern.compile("^\\+27[0-9]{9}$");

    public boolean checkUserName(String value) {
        return value != null && value.length() <= 5 && value.contains("_");
    }

    public boolean checkPasswordComplexity(String value) {
        if (value == null) return false;
        return value.length() >= 8
                && value.matches(".*[A-Z].*")
                && value.matches(".*[0-9].*")
                && value.matches(".*[^a-zA-Z0-9].*");
    }

    public boolean checkCellPhoneNumber(String value) {
        return value != null && SA_CELL_PATTERN.matcher(value).matches();
    }

    public String registerUser(String username, String password, String cellNumber,
                               String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        }
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
        this.firstName = firstName == null ? "" : firstName.trim();
        this.lastName = lastName == null ? "" : lastName.trim();
        return "User registered successfully.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null && password != null
                && username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getUsername() { return username; }
    public String getCellNumber() { return cellNumber; }
}
