package com.f83260.foodwaste.data.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Opportunity {
    private int id;
    private String productName;
    private boolean isAvailable;
    private int storeId;
    private Date createdAt;
    private String userClaimedId;

    public Opportunity(int id, String productName, boolean isAvailable, int storeId, String createdAtStr) {
        this.id = id;
        this.productName = productName;
        this.isAvailable = isAvailable;
        this.storeId = storeId;
        try {
            this.createdAt = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(createdAtStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public Opportunity(int id, String productName, boolean isAvailable, int storeId, String createdAtStr, String userClaimedId) {
        this(id, productName, isAvailable, storeId, createdAtStr);
        this.userClaimedId = userClaimedId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserClaimedId() {
        return userClaimedId;
    }

    public void setUserClaimedId(String userClaimedId) {
        this.userClaimedId = userClaimedId;
    }

}