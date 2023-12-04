package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyReviewsController {

    @FXML
    private TableView<Review> myReviewsTable;

    //private User userOfApp;
    private Stage stage;
    private String username;

    //User user = UserSingleton.getInstance().getUser();

    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
    ObservableList<Review> data = FXCollections.observableArrayList();


    /*public void setUserModel(User userModel) {
        userOfApp = userModel;
    }*/

    public void initialize() {
        // Create columns
        TableColumn<Review, String> reviewCourseMnem = new TableColumn<>("Subject");
        TableColumn<Review, Integer> reviewCourseNum = new TableColumn<>("Number");
        TableColumn<Review, Integer> reviewRating = new TableColumn<>("Rating");
        TableColumn<Review, String> reviewComment = new TableColumn<>("Comment");
        TableColumn<Review, String> reviewTime = new TableColumn<>("Time Stamp");


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

        //Populate dat
        //ArrayList<Review> allReviews = driver.getUserReviews(UserSingleton.getInstance().getUser().getUsername());


    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void populateTable() throws SQLException{
        try {
            driver.connect();
            ArrayList<Review> allReviews = driver.getUserReviews(username);
            System.out.println(username);
            System.out.println(allReviews.size());
            for(Review review: allReviews) {
                data.add(review);
            }
            myReviewsTable.setItems(data);
            driver.commit();
            driver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
        Parent secondPage = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(secondPage));
        stage.show();*/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
            var scene = new Scene(loader.load());
            var controller = (CourseSearchSceneController)loader.getController();
            controller.setStage(stage);
            controller.setUsername(username);
            stage.setScene(scene);
            stage.setX(20.0);
            stage.setY(20.0);
            stage.show();
        } catch (IOException e){
            //e.printStackTrace();
            //errorText.setText("Something went wrong! Please try again!");
        }
    }
}
