package edu.virginia.sde.reviews;

public class MyReview {

    private int rating;
    private String timestamp;
    private String comment;

    private String subjectMnemonic;
    private int courseNumber;
    private String courseTitle;
    private double averageReviewRating;


    public MyReview (Review review, Course course){
        rating = review.getRating();
        timestamp = review.getTimestamp();
        comment = review.getComment();

        subjectMnemonic = course.getSubjectMnemonic();
        courseNumber = course.getCourseNumber();
        courseTitle = course.getCourseTitle();
        averageReviewRating = course.getAverageReviewRating();
    }

    public String getSubjectMnemonicReview() {
        return subjectMnemonic;
    }

    public int getCourseNumberReview(){
        return courseNumber;
    }

    public String getCourseTitleReview(){
        return courseTitle;
    }

    public double getAverageReviewRatingReview(){
        return averageReviewRating;
    }

    public int getRatingReview(){
        return rating;
    }

    public String getTimestampReview(){
        return timestamp;
    }

    public String getCommentReview(){
        return comment;
    }
}
