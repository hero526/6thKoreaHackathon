package com.example.chimchakae.Model;

public class User {

    private String userId;
    private String deviceToken;
    private String carNum;

    public User() { }

    public User(String userId, String deviceToken, String carNum) {
        this.userId = userId;
        this.deviceToken = deviceToken;
        this.carNum = carNum;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public String getCarNum() {
        return this.carNum;
    }

    public void setCarNum(String newCarNum) {
        this.carNum = newCarNum;
    }

    public void setUserId(String newID) {
        this.userId = newID;
    }

    public void setDeviceToken(String newToken) {
        this.deviceToken = newToken;
    }
}
