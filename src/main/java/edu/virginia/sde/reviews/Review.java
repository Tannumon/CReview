package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {

    private Course myCourse;
    private int rating;
    private Timestamp timestamp;
    private String comment;
    private User myUser;

    public Review(Course inpCourse, int inpRating, Timestamp inpTimestamp, String inpComment, User inpUser){
        myCourse = inpCourse;
        rating = inpRating;
        timestamp = inpTimestamp;
        comment = inpComment;
        myUser = inpUser;
    }

    public Course getMyCourse(){
        return myCourse;
    }

    public int getRating(){
        return rating;
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public String getComment(){
        return comment;
    }

    public User getMyUser(){
        return myUser;
    }
}
