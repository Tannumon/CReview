package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.util.ArrayList;

public class Course {

    private String subjectMnemonic;
    private int courseNumber;
    private String courseTitle;
    private double averageReviewRating;

    private DatabaseDriver driver = new DatabaseDriver("course_review_system.sqlite3");

    public Course(String inpSubjMnemonic, int inpCourseNum, String inpCourseTitle, double inputRating){
        subjectMnemonic = inpSubjMnemonic;
        courseNumber = inpCourseNum;
        courseTitle = inpCourseTitle;
        averageReviewRating = inputRating;
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

    public double getAverageReviewRating() {
        return averageReviewRating;
    }


}
