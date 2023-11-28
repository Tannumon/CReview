package edu.virginia.sde.reviews;

import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private ArrayList<Review> myReviews;

    public User(String inputUsername, String inputPassword){
        username = inputUsername;
        password = inputPassword;
        myReviews = new ArrayList<Review>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Review> getMyReviews(){
        return myReviews;
    }
}
