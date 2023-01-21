package com.f83260.foodwaste.data.model;

import android.database.Cursor;

import java.util.List;

public class Store {
    private String name;
    private Double longitude;
    private Double latitude;
    private List<Opportunity> opportunities;

    public Store(String name, Double longitude, Double latitude, List<Opportunity> opportunities) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.opportunities = opportunities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public List<Opportunity> getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(List<Opportunity> opportunities) {
        this.opportunities = opportunities;
    }
}
