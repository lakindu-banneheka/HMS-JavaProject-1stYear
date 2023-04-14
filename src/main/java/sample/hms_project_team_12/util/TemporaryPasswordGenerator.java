package sample.hms_project_team_12.util;

public class TemporaryPasswordGenerator {
    public static String generateTemporaryPassword(String firstName, String nicNumber) {
        String password = "";

        // Get the first three characters of the user's first name
        if (firstName.length() >= 3) {
            password += firstName.substring(0, 3).toLowerCase();
        } else {
            password += firstName.toLowerCase();
        }

        // Get the last three characters of the user's NIC number
        if (nicNumber.length() >= 3) {
            password += nicNumber.substring(nicNumber.length() - 3);
        } else {
            password += nicNumber;
        }

        // Add a random number or special character
        password += (char) (Math.random() * 10 + 48);

        return password;
    }
}

