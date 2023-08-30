package com.iwhere.gisutil.model;

public class ProjectXYPoint {

    public ProjectXYPoint(double x, double y){
        this.x=x;
        this.y=y;
    }

    double x;
    double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString(){
        return "("+x+","+y+")";
    }
}
