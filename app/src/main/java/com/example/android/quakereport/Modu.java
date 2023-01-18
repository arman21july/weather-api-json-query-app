package com.example.android.quakereport;

public class Modu {
    private Double mMagnitude;
    private String mPlace;
    private String mDate;
    private Long mMilliSecond;
    private String mUrl;

    public Modu (Double magnitude, String place, Long milliSecond, String url){
        mMagnitude = magnitude;
        mPlace = place;
        mMilliSecond = milliSecond;
        mUrl = url;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public Long getmMilliSecond() {
        return mMilliSecond;
    }

    public String getmUrl() {
        return mUrl;
    }
}
