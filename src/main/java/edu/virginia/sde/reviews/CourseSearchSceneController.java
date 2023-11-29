package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CourseSearchSceneController{

    @FXML
    private Button createClassButton;
    @FXML
    private Label errorMessage;
    @FXML
    private TextField addNumber;
    @FXML
    private TextField addSubject;
    @FXML
    private TextField addTitle;
    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");

    public void initialize(URL location, ResourceBundle resources) throws SQLException {
//        driver.connect();
//        subjMnemoic.setCellValueFactory(new PropertyValueFactory<>("Subject"));
//        subjNum.setCellValueFactory(new PropertyValueFactory<>("Number"));
//        subjTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
    }

    @FXML
    private void createCourse() throws IOException {
        int errs = 0;
        String courseSubj = addSubject.getText();
        String courseNum = addNumber.getText();
        String courseTitle = addTitle.getText();
        try {
            driver.connect();
            if ((courseTitle.isEmpty()) || (courseSubj.isEmpty()) || (courseNum.isEmpty())) {
                errorMessage.setText("Please fill in all fields when creating a class.");
                addSubject.clear();
                addTitle.clear();
                addNumber.clear();
                errs += 1;

            } else {
                if (courseNum.length() != 4) {
                    addNumber.clear();
                    errorMessage.setText("The course number must be 4 digits.");
                    errs += 1;
                }
                if ((courseSubj.length() > 4) || (courseSubj.length() < 2)) {
                    addSubject.clear();
                    errorMessage.setText("The course mnemonic must be between 2 and 4 characters");
                    errs += 1;
                }
                if ((courseSubj.length() > 4) || (courseSubj.length() < 2)) {
                    addSubject.clear();
                    errorMessage.setText("The course mnemonic must only be characters, no numbers or symbols.");
                    errs += 1;
                }
                if (courseTitle.length() > 50) {
                    addTitle.clear();
                    errorMessage.setText("The course Title must be between 1 and 50 characters.");
                    errs += 1;
                }
            }
            if (errs == 0) {
                Course newCourse = new Course(courseSubj, Integer.parseInt(courseNum), courseTitle, 0.00);
                errorMessage.setText("Course successfully created, write a review if you would like!");
                driver.addCourse(newCourse);
                driver.commit();
                addSubject.clear();
                addTitle.clear();
                addNumber.clear();
                driver.disconnect();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
