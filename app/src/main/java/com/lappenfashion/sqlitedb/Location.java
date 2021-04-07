package com.lappenfashion.sqlitedb;

public class Location {
    String latitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String longitude;
    String time;

    public Location(String latitude, String longitude, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

}
