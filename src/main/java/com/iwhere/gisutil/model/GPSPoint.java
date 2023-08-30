package com.iwhere.gisutil.model;

public class GPSPoint {
    double longitude;
    double latitude;

    public GPSPoint(){

    }

    public GPSPoint(double longitude,double latitude){
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String toString(){
        return "("+longitude+","+latitude+")";
    }
}
