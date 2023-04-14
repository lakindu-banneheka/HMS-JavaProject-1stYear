package sample.hms_project_team_12;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.hms_project_team_12.User.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ComboBox<String> cb_account_type;
    @FXML
    private Button button_login;
    @FXML
    private Button button_register;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_password;
    @FXML
    private Label lb_not_registered;

    String[] account_types = {User.AccountType.PATIENT.name(), User.AccountType.DOCTOR.name(),User.AccountType.ADMIN.name()};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cb_account_type.getItems().addAll(account_types);
        cb_account_type.setValue(account_types[0]);
        cb_account_type.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!cb_account_type.getValue().equals(account_types[0])){
                    lb_not_registered.setText("");
                    button_register.setText("");
                    button_register.setDisable(true);
                } else {
                    lb_not_registered.setText("not registered ?  ");
                    button_register.setText("register");
                    button_register.setDisable(false);
                }
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("@HelloController53 Login button clicked");
                if(!tf_email.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()){
                    User.userLogIn(event,tf_email.getText(), tf_password.getText(), User.AccountType.valueOf(cb_account_type.getValue()));
                } else {
                    System.out.println("Please fill in all information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill email and password fields to logIn!");
                    alert.show();
                }
            }
        });

        button_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("@HelloController68 Register button clicked");
                try {
                    SceneController.switchScene(event,"self-register-patient-view.fxml","ABC Hospital - Patient Register");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}