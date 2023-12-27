package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.sql.Timestamp;

public class Review {

    private int rating;
    private String timestamp;
    private String comment;

    private int courseId;

    private int userId;

    private DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");

    public Review(int inpRating, String inpTimestamp, String inpComment, int cID, int uID){
        rating = inpRating;
        timestamp = inpTimestamp;
        comment = inpComment;
        courseId = cID;
        userId = uID;
    }

    public int getRating(){
        return rating;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getComment(){
        return comment;
    }

    public String getSubjectMnemonic() throws SQLException {
        try{
            driver.connect();
            Course c = driver.getCourseFromID(courseId);
            driver.disconnect();
            return c.getSubjectMnemonic();
        }
        catch(SQLException e){

        }
        return null;
    }

    public int getCourseNumber() throws SQLException{
        try{
            driver.connect();
            Course c = driver.getCourseFromID(courseId);
            driver.disconnect();
            return c.getCourseNumber();
        }
        catch(SQLException e){

        }
        return -1;
    }
}
