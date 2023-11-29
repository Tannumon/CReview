package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Correct import for ActionEvent
import java.io.IOException;

public class LoginScreenController {
    @FXML
    private Button loginButton;

    @FXML
    private void login(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
        Parent secondPage = loader.load();

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(secondPage));
        stage.show();
    }
}
