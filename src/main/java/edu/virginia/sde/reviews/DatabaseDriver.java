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
                    Password   TEXT     not null,
                );
                """;

        final String createCourses = """
                create table if not exists Courses
                (
                    ID                INTEGER  primary key,
                    SubjectMnemonic   TEXT     not null,
                    CourseNumber      INTEGER  not null,
                    CourseTitle       TEXT     not null,
                    AverageRating     DOUBLE
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
                    insert into Users (Username, Passwords)
                        values 
                """;
            String username = user.getUsername();
            String password = user.getPassword();
            insert += "(\"" + username + "\", \"" + password + "\", );";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public void addReview(Review review) throws SQLException {
        try{
            String insert = "";
            insert += "(\"" + "aaaaaa" + "\", \"" + "bbbbbbb" + "\", );";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public void addCourse(Course course) throws SQLException {
        try{
            String insert = "";
            insert += "(\"" + "aaaaaa" + "\", \"" + "bbbbbbb" + "\", );";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public ArrayList<Review> getUserReviews(String username) throws SQLException {
        try{
            String insert = "";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return new ArrayList<Review>();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public ArrayList<Review> getCourseReviews(Course course) throws SQLException {
        try{
            String insert = "";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return new ArrayList<Review>();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public ArrayList<Course> getCourseBySubject(String subject) throws SQLException {
        try{
            String insert = "";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return new ArrayList<Course>();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public ArrayList<Course> getCourseByNumber(int number) throws SQLException {
        try{
            String insert = "";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return new ArrayList<Course>();
        }
        catch (SQLException e){
            rollback();
            throw e;
        }
    }

    //TODO: IMPLEMENT
    public ArrayList<Course> getCourseByTitle(String title) throws SQLException {
        try{
            String insert = "";
            PreparedStatement preparedStatement = connection.prepareStatement(insert);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return new ArrayList<Course>();
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
