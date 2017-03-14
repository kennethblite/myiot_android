package com.example.colin.myapplication;

/**
 * Created by colin on 7/18/2016.
 */
public class Recommendation {
    private String mRecommendations;
    private String mDate;
    public Recommendation(String recommendations, String Date){
        mRecommendations = recommendations;
        mDate = Date;
    }

    public String getRecommendations(){
        return mRecommendations;
    }

    public String getDate(){
        return mDate;
    }

}
