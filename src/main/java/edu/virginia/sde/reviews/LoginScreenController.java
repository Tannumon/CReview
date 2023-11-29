package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Connection connection;

    @FXML
    private void login(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
        Parent secondPage = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(secondPage));
        stage.show();
    }



    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Method to handle saving data to SQLite and clearing fields
    @FXML
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
    }
}
