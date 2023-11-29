package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {

    private int rating;
    private String timestamp;
    private String comment;

    public Review(int inpRating, String inpTimestamp, String inpComment){
        rating = inpRating;
        timestamp = inpTimestamp;
        comment = inpComment;
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
