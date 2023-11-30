package edu.virginia.sde.reviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseDriver {
    private final String sqliteFilename;
    private Connection connection;


    public DatabaseDriver (String sqlListDatabaseFilename) {
        this.sqliteFilename = sqlListDatabaseFilename;
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        //the next line enables foreign key enforcement - do not delete/comment out
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        //the next line disables auto-commit - do not delete/comment out
        connection.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public void createTables() throws SQLException {
        // if not exists
        final String createUsers = """
                create table if not exists Users
                (
                    ID         INTEGER  primary key,
                    Username   TEXT     not null unique,
                    Password   TEXT     not null
                );
                """;

        final String createCourses = """
                create table if not exists Courses
                (
                    ID                INTEGER  primary key,
                    SubjectMnemonic   TEXT     not null,
                    CourseNumber      INTEGER  not null,
                    CourseTitle       TEXT     not null,
                    AverageRating     DOUBLE   not null
                );
                """;

        final String createReviews = """
                create table if not exists Reviews
                (
                    ID                                               INTEGER   primary key,
                    Rating                                           INTEGER   not null,
                    Timestamp                                        TEXT      not null,
                    Comment                                          TEXT      not null,
                    UserID                                           TEXT      not null,
                    CourseID                                         INTEGER   not null,
                    FOREIGN KEY (UserID) references Users(ID)        on delete cascade,
                    FOREIGN KEY (CourseID) references Courses(ID)    on delete cascade
                );
                """;

        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is not open");
        }
        var createTables = List.of(createUsers, createCourses, createReviews);
        for (var query : createTables) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

    }

    public void addUser(User user) throws SQLException {
        try{
            String insert = """
                    insert into Users (Username, Password)
                        values 
                """;
            String username = user.getUsername();
            String password = user.getPassword();
            insert += "(\"" + username + "\", \"" + password + "\");";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public int getUserIDbyUsername(String username) throws SQLException {
        try{
            String getUserID = "select * from Users where Username LIKE '"+username+"'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getUserID);
            if(rs.next() == false)
                return -1;
            int userID = rs.getInt("ID");
            return userID;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public String getUserPassword(String username) throws SQLException {
        try{
            String getUserID = "select * from Users where Username LIKE '"+username+"'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getUserID);
            String password = rs.getString("Password");
            return password;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public int getCourseID(Course course) throws SQLException {
        try{
            String getUserID = "select * from Courses where SubjectMnemonic LIKE '"+course.getSubjectMnemonic()+"' " +
                    "AND CourseTitle LIKE '"+course.getSubjectMnemonic()+"' " +
                    "AND CourseNumber = " + course.getCourseNumber() + ";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getUserID);
            if(rs.next() == false)
                return -1;
            int courseID = rs.getInt("ID");
            return courseID;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public void addReview(Review review, String username, Course course) throws SQLException {
        try{
            String insert = """
                    insert into Reviews (Rating, Timestamp, Comment, UserID, CourseID)
                        values 
                """;
            int rating = review.getRating();
            String timestamp = review.getTimestamp();
            String comment = review.getComment();
            int userID = getUserIDbyUsername(username);
            int courseID = getCourseID(course);
            insert += "(" + rating + ", \"" + timestamp + "\", \"" + comment + "\", " + userID + ", " + courseID + ");";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            updateAverageCourseRating(course);
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public void updateAverageCourseRating(Course course) throws SQLException {
        try{
            ArrayList<Review> courseReviews = getCourseReviews(course);
            double averageRating = 0;
            for(Review review: courseReviews){
                averageRating += review.getRating();
            }
            averageRating /= courseReviews.size();
            int courseID = getCourseID(course);
            String update = "UPDATE Courses SET AverageRating = " + averageRating + " WHERE ID = " +  courseID + ";";
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public void addCourse(Course course) throws SQLException {
        try{
            if(getCourseID(course) != -1)
                return;
            String insert = """
                    insert into Courses (SubjectMnemonic, CourseNumber, CourseTitle, AverageRating)
                        values 
                """;
            String subj = course.getSubjectMnemonic();
            int courseNum = course.getCourseNumber();
            String title = course.getCourseTitle();
            double rating = course.getAverageReviewRating();
            insert += "(\"" + subj + "\", " + courseNum + ", \"" + title + "\", " + rating + ");";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }
    public ArrayList<Review> getUserReviews(String username) throws SQLException {
        try{
            int userID = getUserIDbyUsername(username);
            String getMyReviews = "select * from Reviews WHERE UserID = " + userID + ";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getMyReviews);

            ArrayList<Review> myReviews = new ArrayList<Review>();

            while(rs.next()) {
                int rating = rs.getInt("Rating");
                String timestamp = rs.getString("Timestamp");
                String comment = rs.getString("Comment");
                int courseID = rs.getInt ("CourseID");
                int userIDNumber = rs.getInt ("UserID");
                Review r = new Review(rating, timestamp, comment, courseID, userIDNumber);
                myReviews.add(r);
            }
            return myReviews;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public ArrayList<Review> getCourseReviews(Course course) throws SQLException {
        try{
            int courseId = getCourseID(course);
            String getCourseReviews = "select * from Reviews WHERE CourseID = " + courseId + ";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getCourseReviews);

            ArrayList<Review> courseReviews = new ArrayList<Review>();

            while(rs.next()) {
                int rating = rs.getInt("Rating");
                String timestamp = rs.getString("Timestamp");
                String comment = rs.getString("Comment");
                int courseID = rs.getInt ("CourseID");
                int userIDNumber = rs.getInt ("UserID");
                Review r = new Review(rating, timestamp, comment, courseID, userIDNumber);
                courseReviews.add(r);
            }
            return courseReviews;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public ArrayList<Course> getCourseBySubject(String subject) throws SQLException {
        try{
            String getCoursesUnderSubject = "select * from Courses WHERE SubjectMnemonic LIKE '"+subject+"';";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getCoursesUnderSubject);

            ArrayList<Course> coursesUnderSubject = new ArrayList<Course>();

            while(rs.next()) {
                int courseNum = rs.getInt("CourseNumber");
                String title = rs.getString("CourseTitle");
                double rating = rs.getDouble("AverageRating");
                Course c = new Course(subject, courseNum, title, rating);
                coursesUnderSubject.add(c);
            }
            return coursesUnderSubject;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }
    public ArrayList<Course> getCourseByNumber(int number) throws SQLException {
        try{
            String getCoursesUnderNumber = "select * from Courses WHERE CourseNumber = " + number + ";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getCoursesUnderNumber);

            ArrayList<Course> coursesUnderNumber = new ArrayList<Course>();

            while(rs.next()) {
                String subj = rs.getString("SubjectMnemonic");
                String title = rs.getString("CourseTitle");
                double rating = rs.getDouble("AverageRating");
                Course c = new Course(subj, number, title, rating);
                coursesUnderNumber.add(c);
            }
            return coursesUnderNumber;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public ArrayList<Course> getCourseByTitle(String title) throws SQLException {
        try{
            String getCoursesUnderTitle = "select * from Courses WHERE SubjectMnemonic LIKE '"+title+"';";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getCoursesUnderTitle);

            ArrayList<Course> coursesUnderTitle = new ArrayList<Course>();

            while(rs.next()) {
                int courseNum = rs.getInt("CourseNumber");
                String subj = rs.getString("SubjectMnemonic");
                double rating = rs.getDouble("AverageRating");
                Course c = new Course(subj, courseNum, title, rating);
                coursesUnderTitle.add(c);
            }
            return coursesUnderTitle;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public ArrayList<Course> getAllCourses() throws SQLException {
        try{
            String getCoursesUnderTitle = "select * from Courses;";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(getCoursesUnderTitle);

            ArrayList<Course> courses = new ArrayList<Course>();

            while(rs.next()) {
                int courseNum = rs.getInt("CourseNumber");
                String subj = rs.getString("SubjectMnemonic");
                double rating = rs.getDouble("AverageRating");
                String title = rs.getString("CourseTitle");
                Course c = new Course(subj, courseNum, title, rating);
                courses.add(c);
            }
            return courses;
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public void clearTables() throws SQLException {
        String deleteUsers = "delete from Users";
        String deleteCourses = "delete from Courses";
        String deleteReviews = "delete from Reviews";

        PreparedStatement preparedStatement1 = connection.prepareStatement(deleteUsers);
        preparedStatement1.executeUpdate();
        preparedStatement1.close();

        PreparedStatement preparedStatement2 = connection.prepareStatement(deleteCourses);
        preparedStatement2.executeUpdate();
        preparedStatement2.close();

        PreparedStatement preparedStatement3 = connection.prepareStatement(deleteReviews);
        preparedStatement3.executeUpdate();
        preparedStatement3.close();
    }

}
