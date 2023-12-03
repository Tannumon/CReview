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
    @FXML
    private Label courseDetails;
    @FXML
    private Label averageRating;

    private int rating;

    private Stage stage;
    private Course course;
    private ToggleGroup buttonGroup;

    private ObservableList<courseReview> reviewsData;

    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
    public void initialize() {
        try {
            // Create columns
            buttonGroup = new ToggleGroup();
            radio1.setToggleGroup(buttonGroup);
            radio2.setToggleGroup(buttonGroup);
            radio3.setToggleGroup(buttonGroup);
            radio4.setToggleGroup(buttonGroup);
            radio5.setToggleGroup(buttonGroup);
            TableColumn<courseReview, Integer> score = new TableColumn<>("Score");
            TableColumn<courseReview, String> comment = new TableColumn<>("Comment");
            TableColumn<courseReview, Timestamp> date = new TableColumn<>("Date");

            courseTable.setRowFactory(tv -> {
                TableRow<courseReview> row = new TableRow<>();
                row.setOnMouseClicked(e -> populateFields(row));
                return row;
            });

            // kind of like setting up the table in terms of data types
            score.setCellValueFactory(new PropertyValueFactory<>("score"));
            comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            // actually adding the created columns to the same tableView that is connected to the FXML
            courseTable.getColumns().add(score);
            courseTable.getColumns().add(comment);
            courseTable.getColumns().add(date);
            // create empty reviewsData observable list with datarows in the tableView
            reviewsData = FXCollections.observableArrayList(courseTable.getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateFields(TableRow<courseReview> row) {
        myReview.setText(row.getItem().getComment());
        int rating = row.getItem().getScore();
        switch (rating) {
            case 1: radio1.setSelected(true);
            case 2: radio2.setSelected(true);
            case 3: radio3.setSelected(true);
            case 4: radio4.setSelected(true);
            case 5: radio5.setSelected(true);
        }
    }

    private void addRow(courseReview newReview) {
        reviewsData.add(newReview);
    }
    /*@FXML
    private void saveReview() throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        //String pattern = "yyyy-MM-dd";
        //String timestamp = new SimpleDateFormat(pattern).format(new Date());
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

        try {
            String username = UserSingleton.getInstance().getUser().getUsername();
            Review rev = null;
            rev = new Review(rating, timestamp.toString(), comment, course.getCourseNumber(), driver.getUserIDbyUsername(username));
            driver.addReview(rev, username, course);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void deleteReview() throws SQLException {
        if ()
        updateReviewsDatabase();
    }

    private void updateMyCourseReviewsPage() throws SQLException {
        updateMyCourseReviewsTable();
    }*/

    /*private void updateReviewsDatabase() throws SQLException {
        ArrayList<Review> allCourseReviews = driver.getCourseReviews(course);
        reviewsData.clear();
        for (Review r: allCourseReviews) {
            reviewsData.add(new courseReview())
        }
    }*/

    public void setCourse(Course course) {
        this.course = course;
        courseDetails.setText(course.getSubjectMnemonic() + " " + course.getCourseNumber() + ": " + course.getCourseTitle());
        averageRating.setText("Average Rating: " + course.getAverageReviewRating());
    }
    @FXML
    public void goToCourseReviewPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
            var scene = new Scene(loader.load());
            var controller = (CourseSearchSceneController)loader.getController();
            controller.setStage(stage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

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
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
