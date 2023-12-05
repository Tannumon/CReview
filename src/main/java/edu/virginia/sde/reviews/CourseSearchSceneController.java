package edu.virginia.sde.reviews;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CourseSearchSceneController {

    @FXML
    private Label addClassErrorMessage;
    @FXML
    private Label addClassSubjectErrorMessage;
    @FXML
    private Label addClassNumberErrorMessage;
    @FXML
    private Label addClassTitleErrorMessage;

    @FXML
    private TextField addNumber;
    @FXML
    private TextField addSubject;
    @FXML
    private TextField addTitle;
    @FXML
    private TextField subjectSearch;
    @FXML
    private TextField numberSearch;
    @FXML
    private TextField titleSearch;
    DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");
    private ObservableList<Course> courseData = FXCollections.observableArrayList();
    @FXML
    private TableView<Course> courseTable;

    private Stage stage;
    private String username;
    ObservableList<Course> data = FXCollections.observableArrayList();


    public void initialize() {
        try {
            driver.connect();
            // Create columns
            TableColumn<Course, String> courseMnem = new TableColumn<>("Subject");
            TableColumn<Course, Integer> courseInt = new TableColumn<>("Number");
            TableColumn<Course, String> courseTit = new TableColumn<>("Title");
            TableColumn<Course, Double> courseAvgRat = new TableColumn<>("Average Rating");

            courseTable.setRowFactory(tv -> {
                TableRow<Course> row = new TableRow<>();
                row.setOnMouseClicked(e -> {
                    try {
                        goToCourseReviewPage(row);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                return row;
            });

            courseMnem.setCellValueFactory(new PropertyValueFactory<>("subjectMnemonic"));
            courseInt.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
            courseTit.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
            courseAvgRat.setCellValueFactory(new PropertyValueFactory<>("averageReviewRatingString"));

            // Add columns to the TableView
            courseTable.getColumns().add(courseMnem);
            courseTable.getColumns().add(courseInt);
            courseTable.getColumns().add(courseTit);
            courseTable.getColumns().add(courseAvgRat);

            // Populate data

            ArrayList<Course> courses = driver.getAllCourses();
            for(Course course: courses) {
                data.add(course);
            }
            courseTable.setItems(data);
            driver.commit();
            driver.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRow(Course newCourse) {
        courseData.add(newCourse);
    }

    private boolean isAlpha(String input){
        String str = input.toLowerCase();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int x = 0; x < str.length(); x++){
            if(!alphabet.contains(str.substring(x, x+1)))
                return false;
        }
        return true;
    }

    private boolean isNum(String input){
        String str = input.toLowerCase();
        String alphabet = "1234567890";
        for(int x = 0; x < str.length(); x++){
            if(!alphabet.contains(str.substring(x, x+1)))
                return false;
        }
        return true;
    }

    @FXML
    private void createCourse() throws IOException {
        addClassSubjectErrorMessage.setText("");
        addClassTitleErrorMessage.setText("");
        addClassNumberErrorMessage.setText("");
        addClassErrorMessage.setText("");
        int errs = 0;
        String courseSubj = addSubject.getText().trim();
        String courseNum = addNumber.getText().trim();
        String courseTitle = addTitle.getText().trim();
        try {
            driver.connect();
            if ((courseTitle.isEmpty()) || (courseSubj.isEmpty()) || (courseNum.isEmpty())) {
                addClassErrorMessage.setText("Please fill in all fields when creating a class.");
                addSubject.clear();
                addTitle.clear();
                addNumber.clear();
                errs += 1;
                driver.disconnect();

            } else {
                if (courseNum.length() != 4) {
                    addNumber.clear();
                    addClassNumberErrorMessage.setText("The course number must be 4 digits.");
                    errs += 1;
                }
                if ((courseSubj.length() > 4) || (courseSubj.length() < 2)) {
                    addSubject.clear();
                    addClassSubjectErrorMessage.setText("The course mnemonic must be between 2 and 4 characters");
                    errs += 1;
                }
                if (!isAlpha(courseSubj)) {
                    addSubject.clear();
                    addClassSubjectErrorMessage.setText("The course mnemonic must only be characters, no numbers or symbols.");
                    errs += 1;
                }
                if (!isNum(courseNum)) {
                    addNumber.clear();
                    addClassNumberErrorMessage.setText("The course number must only be digits, no letters or symbols.");
                    errs += 1;
                }
                if (courseTitle.length() > 50) {
                    addTitle.clear();
                    addClassTitleErrorMessage.setText("The course Title must be between 1 and 50 characters.");
                    errs += 1;
                }
            }
            Course newCourse = new Course(courseSubj.toUpperCase(), Integer.parseInt(courseNum), courseTitle, 0.00);
            if (driver.getCourseID(newCourse) < 1) {
                if (errs == 0) {
                    addClassErrorMessage.setText("Course successfully created, write a review if you would like!");
                    addSubject.clear();
                    addTitle.clear();
                    addNumber.clear();
                    driver.addCourse(newCourse);
                    driver.commit();
                    driver.disconnect();
                    data.add(newCourse);
                    courseTable.setItems(data);
                }
            } else {
                addClassErrorMessage.setText("The class already exists. Write a review for this class or add another class instead.");
                addSubject.clear();
                addTitle.clear();
                addNumber.clear();
                driver.disconnect();
            }
            driver.disconnect();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private ArrayList<Course> filterBySubject(ArrayList<Course> current, String subj){
        ArrayList<Course> result = new ArrayList<Course>();
        for(Course course : current){
            if(course.getSubjectMnemonic().equalsIgnoreCase(subj))
                result.add(course);
        }
        return result;
    }

    private ArrayList<Course> filterByNumber(ArrayList<Course> current, int num){
        ArrayList<Course> result = new ArrayList<Course>();
        for(Course course : current){
            if(course.getCourseNumber() == num)
                result.add(course);
        }
        return result;
    }

    private ArrayList<Course> filterByTitle(ArrayList<Course> current, String title){
        ArrayList<Course> result = new ArrayList<Course>();
        for(Course course : current){
            if(course.getCourseTitle().toLowerCase().contains(title.toLowerCase()))
                result.add(course);
        }
        return result;
    }

    @FXML
    private void searchResult() throws IOException{
        try {
            driver.connect();
            String subject = subjectSearch.getText();
            String num_temp = numberSearch.getText();
            String title = titleSearch.getText();
            if(!isNum(num_temp) && !num_temp.isEmpty()){
                addClassErrorMessage.setText("Please enter a valid number and try again!");
            }
            else{
                ArrayList<Course> searchResults = driver.getAllCourses();
                if(!subject.isEmpty())
                    searchResults = filterBySubject(searchResults, subject);
                if(!num_temp.isEmpty()){
                    int num = Integer.parseInt(num_temp);
                    searchResults = filterByNumber(searchResults, num);
                }
                if(!title.isEmpty())
                    searchResults = filterByTitle(searchResults, title);
                ObservableList<Course> searchData = FXCollections.observableArrayList();
                for(Course c: searchResults){
                    searchData.add(c);
                }
                courseTable.refresh();
                courseTable.setItems(searchData);
            }
            driver.disconnect();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void moveToNextScreen(ActionEvent event) throws IOException, SQLException {
        /*try {
            driver.connect();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my-reviews.fxml"));
            Parent thirdPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(thirdPage));
            stage.show();
            driver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my-reviews.fxml"));
            var scene = new Scene(loader.load());
            var controller = (MyReviewsController)loader.getController();
            controller.setUsername(username);
            controller.populateTable();
            controller.setStage(stage);
            System.out.println("search controller: " + username);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            //e.printStackTrace();
            //errorText.setText("Something went wrong! Please try again!");
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    private void goToCourseReviewPage(TableRow<Course> row) throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("course-reviews-scene.fxml"));
            var scene = new Scene(loader.load());
            var controller = (CourseReviewsSceneController)loader.getController();
            controller.setStage(stage);
            controller.setCourse(row.getItem());
            controller.setUsername(username);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            //throw new RuntimeException(e);
        }
    }


    public void logout(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-screen.fxml"));
            var scene = new Scene(loader.load());
            var controller = (LoginScreenController)loader.getController();
            controller.setStage(stage);
            controller.setUsername("");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            //e.printStackTrace();
            //errorText.setText("Something went wrong! Please try again!");
        }
    }
}