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
    @FXML
    private Label errorText;

    private int rating;

    private Stage stage;
    private Course course;
    private ToggleGroup buttonGroup;

    private ObservableList<courseReview> reviewsData;

    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
    private String username;

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
            TableColumn<courseReview, String> date = new TableColumn<>("Date");

            courseTable.setRowFactory(tv -> {
                TableRow<courseReview> row = new TableRow<>();
                row.setOnMouseClicked(e -> populateFields(row));
                return row;
            });

            // kind of like setting up the table in terms of data types
            score.setCellValueFactory(new PropertyValueFactory<>("score"));
            comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));

            score.prefWidthProperty().bind(courseTable.widthProperty().multiply(3.0/20.0));
            comment.prefWidthProperty().bind(courseTable.widthProperty().multiply(10.0/20.0));
            date.prefWidthProperty().bind(courseTable.widthProperty().multiply(6.9/20.0));

            // actually adding the created columns to the same tableView that is connected to the FXML
            courseTable.getColumns().add(score);
            courseTable.getColumns().add(comment);
            courseTable.getColumns().add(date);

            // create empty reviewsData observable list with datarows in the tableView
            reviewsData = FXCollections.observableArrayList();
        } catch (Exception e) {
            e.printStackTrace();
            errorText.setText("Something went wrong! Please try again!");
        }
    }

    private void populateFields(TableRow<courseReview> row) {
        myReview.setText(row.getItem().getComment());
        int rating = row.getItem().getScore();
        switch (rating) {
            case 1: radio1.setSelected(true);
            break;
            case 2: radio2.setSelected(true);
            break;
            case 3: radio3.setSelected(true);
            break;
            case 4: radio4.setSelected(true);
            break;
            case 5: radio5.setSelected(true);
            break;
        }
    }

    private void addRow(courseReview newReview) {
        reviewsData.add(newReview);
    }
    @FXML
    private void saveReview() throws IOException, SQLException {
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
        else if (radio4.isSelected()) {
            rating = 4;
        }
        else if (radio5.isSelected()) {
            rating = 5;
        }
        else {
            errorText.setText("Please choose a rating before saving the review");
            return;
        }

        //add functionality for if nothing is selected and button is pressed

        try {
            driver.connect();
            //String username = UserSingleton.getInstance().getUser().getUsername();
            int userID = driver.getUserIDbyUsername(username);
            int courseID = driver.getCourseID(course);
            String time = timestamp.toString();
            time = time.substring(0, time.length()-7);
            Review rev = new Review(rating, time, comment, courseID, userID);
            driver.deleteReviewIfPresent(course, userID);
            driver.addReview(rev, username, course);
            driver.commit();
            updateMyCourseReviewsPage();
            driver.disconnect();
            errorText.setText("");
        } catch (SQLException e) {
            //e.printStackTrace();
            errorText.setText("Something went wrong! Please try again!");
        }
    }
    @FXML
    private void deleteReview() throws SQLException {
        try {
            driver.connect();
            int userID = driver.getUserIDbyUsername(username);
            driver.deleteReviewIfPresent(course,userID);
            updateReviewsDatabase();
            if(driver.getCourseReviews(course).size() == 0){
                //driver.updateAverageCourseRating(course);
                driver.resetCourseAverageRating(course);
                averageRating.setText("Average Rating: ");
            }

            else{
                driver.updateAverageCourseRating(course);
                updateMyCourseReviewsPage();
            }
            radio1.setSelected(false);
            radio2.setSelected(false);
            radio3.setSelected(false);
            radio4.setSelected(false);
            radio5.setSelected(false);
            myReview.setText("");
            driver.commit();
            driver.disconnect();
        }
        catch (SQLException e) {

        }
    }

    private void updateMyCourseReviewsPage() throws SQLException {
        try{
            //driver.connect();
            updateReviewsDatabase();
            String formattedString = String.format("%.2f", driver.getCourseAverageRating(course));
            if(driver.getCourseReviews(course).size() == 0)
                averageRating.setText("Average Rating: ");
            else
                averageRating.setText("Average Rating: " + formattedString);
        }
        catch(SQLException e){

        }

    }

    private void updateReviewsDatabase() throws SQLException {
        try {
            ArrayList<Review> allCourseReviews = driver.getCourseReviews(course);
            reviewsData.clear();
            for (Review r: allCourseReviews) {
                reviewsData.add(new courseReview(r.getRating(), r.getComment(), r.getTimestamp().toString()));
            }
            courseTable.setItems(reviewsData);
        }
        catch (SQLException e){

        }
    }

    public void setCourse(Course course) throws SQLException {
        try{
            driver.connect();
            this.course = course;
            courseDetails.setText(course.getSubjectMnemonic() + " " + course.getCourseNumber() + ": " + course.getCourseTitle());
            if(driver.getCourseReviews(course).size() == 0)
                averageRating.setText("Average Rating: ");
            else
                averageRating.setText("Average Rating: " + driver.getCourseAverageRating(course));
            updateMyCourseReviewsPage();
            driver.disconnect();
        }
        catch(SQLException e){
        }
    }
    @FXML
    public void goToCourseReviewPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("course-search-scene.fxml"));
            var scene = new Scene(loader.load());
            var controller = (CourseSearchSceneController)loader.getController();
            controller.setStage(stage);
            controller.setUsername(username);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            errorText.setText("Something went wrong! Please try again!");
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public record courseReview(int score, String comment, String date) {
        public int getScore() {
            return score;
        }
        public String getComment() {
            return comment;
        }
        public String getDate() {
            return date;
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}