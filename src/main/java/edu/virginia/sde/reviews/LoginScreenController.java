package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Correct import for ActionEvent
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginScreenController {
    @FXML
    public TextField usernameField;

    @FXML
    private Label errorMessage;

    @FXML
    private PasswordField passwordField;
    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");


    public String getUsernameField(){
        String theUser = usernameField.getText();
        return theUser;
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            driver.connect();
            if(driver.getUserIDbyUsername(username) == -1){
                errorMessage.setText("User does not exist! Please enter information, select Create User, and then try again!");
                usernameField.clear();
                passwordField.clear();
                driver.disconnect();
            }
            else{
                if(password.equals(driver.getUserPassword(username))){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
                    Parent secondPage = loader.load();
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(secondPage));
                    stage.show();
                }
                else{
                    errorMessage.setText("Password is incorrect! Please try again!");
                }
                driver.disconnect();
            }

        } catch (SQLException e) {
            errorMessage.setText("Something went wrong. Please try again!");
        }
    }

    @FXML
    private void create() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            driver.connect();
            if(driver.getUserIDbyUsername(username) != -1){
                errorMessage.setText("User already exists! Please log in!");
                usernameField.clear();
                passwordField.clear();
                driver.disconnect();
            }
            else{
                if(password.length() < 8){
                    passwordField.clear();
                    errorMessage.setText("Please enter a password that is 8 or more characters in length and try again.");
                }
                else{
                    User newUser = new User(username, password);
                    errorMessage.setText("User account created successfully! Please log in!");
                    driver.addUser(newUser);
                    driver.commit();
                    usernameField.clear();
                    passwordField.clear();
                }
                driver.disconnect();
            }
        } catch (SQLException e) {
            errorMessage.setText("Something went wrong. Please try again!");
        }
    }
}
