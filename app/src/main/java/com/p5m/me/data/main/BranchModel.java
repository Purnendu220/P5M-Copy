package com.p5m.me.data.main;

import java.io.Serializable;


public class BranchModel implements Serializable {

    private int branchId;
    private String localityName;
    private int localityId;
    private String branchName;
    private String address;
    private double latitude;
    private double longitude;
    private String gymName;
    private int gymId;
    private int mediaId;
    private String mediaUrl;
    private String mediaThumbNailUrl;
    private String studioInstruction;
    private String phoneNumber;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaThumbNailUrl() {
        return mediaThumbNailUrl;
    }

    public void setMediaThumbNailUrl(String mediaThumbNailUrl) {
        this.mediaThumbNailUrl = mediaThumbNailUrl;
    }

    public String getStudioInstruction() {
        return studioInstruction;
    }

    public void setStudioInstruction(String studioInstruction) {
        this.studioInstruction = studioInstruction;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
