package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class CourseReviewsApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Course Review System");
        stage.setScene(scene);
        stage.show();
        var controller = (LoginScreenController) fxmlLoader.getController();
        controller.setStage(stage);
        DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
        driver.connect();
        driver.createTables();
        driver.commit();
        driver.disconnect();
    }
}
