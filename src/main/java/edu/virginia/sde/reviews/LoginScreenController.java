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
    private Button loginButton;

    @FXML
    private Button createUserButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorMessage;

    @FXML
    private PasswordField passwordField;
    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");

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
    private void create(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            driver.connect();
            if(driver.getUserIDbyUsername(username) != -1){
                errorMessage.setText("User already exists! Please log in!");
            }
            else{
                if(password.length() < 8){
                    passwordField.clear();
                    errorMessage.setText("Please enter a password that is 8 or more characters in length and try again.");
                }
                User newUser = new User(username, password);
                driver.addUser(newUser);
                driver.commit();
                driver.disconnect();
            }

        } catch (SQLException e) {
            errorMessage.setText("Something went wrong. Please try again!");
        }
    }





    // Method to handle saving data to SQLite and clearing fields
   /* @FXML
    private void addingUsertoDB() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Save to SQLite Database
        try {
            Connection userConnection = DriverManager.getConnection("PATH/TO/DB");
            String sql = "INSERT INTO User (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = userConnection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            userConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Clear text fields after insertion
        usernameField.clear();
        passwordField.clear();
    }*/
}
