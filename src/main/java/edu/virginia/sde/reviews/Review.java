package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {

    private int rating;
    private String timestamp;
    private String comment;

    private int courseId;

    private int userId;

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
}
