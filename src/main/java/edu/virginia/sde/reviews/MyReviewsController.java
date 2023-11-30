package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class MyReviewsController {

    @FXML
    private TableView<MyReview> myReviewsTable;
    @FXML
    private TextField usernameField;

    String username = usernameField.getText();

    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
    ObservableList<MyReview> data = FXCollections.observableArrayList();

    public void initialize() {
        try {
            driver.connect();
            // Create columns
            TableColumn<MyReview, String> reviewCourseMnem = new TableColumn<>("Subject");
            TableColumn<MyReview, Integer> reviewCourseNum = new TableColumn<>("Number");
            TableColumn<MyReview, Integer> reviewRating = new TableColumn<>("Rating");
            TableColumn<MyReview, String> reviewComment = new TableColumn<>("Comment");
            TableColumn<MyReview, String> reviewTime = new TableColumn<>("Time Stamp");


            reviewCourseMnem.setCellValueFactory(new PropertyValueFactory<>("subjectMnemonic"));
            reviewCourseNum.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
            reviewRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            reviewComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            reviewTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

            // Add columns to the TableView
            myReviewsTable.getColumns().add(reviewCourseMnem);
            myReviewsTable.getColumns().add(reviewCourseNum);
            myReviewsTable.getColumns().add(reviewRating);
            myReviewsTable.getColumns().add(reviewComment);
            myReviewsTable.getColumns().add(reviewTime);

            /*//Populate data
            ArrayList<MyReview> allReviews = driver.getUserReviews(username);
            for(MyReview review: allReviews) {
                data.add(review);
            }
            myReviewsTable.setItems(data);
            driver.commit();
            driver.disconnect(); */

        } catch (SQLException e) {
            e.printStackTrace();
        }



        }
    }
