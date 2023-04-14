package sample.hms_project_team_12.util;
import javafx.scene.control.Alert;

public class ErrorChecking {

    public static boolean isValidEmail (String email) {
        if(email.contains("@")){
            return true;
        } else {
            System.out.println("Invalid email");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Provided email is invalid!");
            alert.show();
            return false;
        }
    }
}
