package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseReviewsSceneController {
    @FXML
    private TextArea myReview;
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;
    @FXML
    private RadioButton radio4;
    @FXML
    private RadioButton radio5;
    @FXML
    private TableView<courseReview> courseTable;

    private int rating;

    private ObservableList<courseReview> reviewsData;

    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
    public void initialize() {
        try {
            driver.connect();
            // Create columns
            TableColumn<courseReview, Integer> score = new TableColumn<>("Score");
            TableColumn<courseReview, String> comment = new TableColumn<>("Comment");
            TableColumn<courseReview, Timestamp> date = new TableColumn<>("Date");
            // kind of like setting up the table in terms of data types
            score.setCellValueFactory(new PropertyValueFactory<>("score"));
            comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            // actually adding the created columns to the same tableView that is connected to the FXML
            courseTable.getColumns().add(score);
            courseTable.getColumns().add(comment);
            courseTable.getColumns().add(date);

            reviewsData = FXCollections.observableArrayList(courseTable.getItems());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRow(courseReview newReview) {
        reviewsData.add(newReview);
    }
    @FXML
    /*private void saveReview() throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String pattern = "yyyy-MM-dd";
        String timestamp = new SimpleDateFormat(pattern).format(new Date());
        String comment = myReview.getText();
        if (radio1.isSelected()) {
            rating = 1;
        }
        else if (radio2.isSelected()) {
            rating = 2;
        }
        else if (radio3.isSelected()) {
            rating = 3;
        }
        else if (radio3.isSelected()) {
            rating = 4;
        }
        else if (radio3.isSelected()) {
            rating = 5;
        }

        //add functionality for if nothing is selected and button is pressed

        Review rev = new Review(rating, timestamp.toString(), comment, 1, );
        driver.addReview(rev, UserSingleton.getInstance().getUser().getUsername());
    }*/
    @FXML
    private void deleteReview() throws IOException {
    }

    @FXML
    /*private void moveToNextScreen(ActionEvent event) throws IOException {
        try {
            driver.connect();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my-reviews.fxml"));
            Parent thirdPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(thirdPage));
            stage.show();
            driver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    public record courseReview(int score, String comment, Timestamp date) {
        public int getScore() {
            return score;
        }
        public String getComment() {
            return comment;
        }
        public Timestamp getDate() {
            return date;
        }
    }
}
