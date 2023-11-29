package edu.virginia.sde.reviews;

import java.util.ArrayList;

public class Course {

    private String subjectMnemonic;
    private int courseNumber;
    private String courseTitle;
    private double averageReviewRating;

    public Course(String inpSubjMnemonic, int inpCourseNum, String inpCourseTitle){
        subjectMnemonic = inpSubjMnemonic;
        courseNumber = inpCourseNum;
        courseTitle = inpCourseTitle;
        averageReviewRating = 0.0;
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


}
