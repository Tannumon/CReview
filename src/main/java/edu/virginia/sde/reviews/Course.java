package edu.virginia.sde.reviews;

import java.util.ArrayList;

public class Course {

    private String subjectMnemonic;
    private int courseNumber;
    private String courseTitle;
    private double averageReviewRating;
    private ArrayList<Review> reviews;

    public Course(String inpSubjMnemonic, int inpCourseNum, String inpCourseTitle){
        subjectMnemonic = inpSubjMnemonic;
        courseNumber = inpCourseNum;
        courseTitle = inpCourseTitle;
        averageReviewRating = 0.0;
        reviews = new ArrayList<Review>();
    }
    public String getSubjectMnemonic() {
        return subjectMnemonic;
    }

    public int getCourseNumber(){
        return courseNumber;
    }

    public String getCourseTitle(){
        return courseTitle;
    }

    public double getAverageReviewRating(){
        return averageReviewRating;
    }

    public ArrayList<Review> getReviews(){
        return reviews;
    }

    public void addReview(Review newReview){
        if(averageReviewRating == 0.0)
            averageReviewRating = newReview.getRating();
        else
            updateMyRating(newReview);
        reviews.add(newReview);
    }

    public void updateMyRating(Review newReview){
        double sum = averageReviewRating * reviews.size();
        sum += newReview.getRating();
        averageReviewRating = sum / (reviews.size()+1);
    }


}
