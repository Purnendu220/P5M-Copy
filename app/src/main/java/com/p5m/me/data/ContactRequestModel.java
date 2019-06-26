package com.p5m.me.data;

public class ContactRequestModel {
    /**
     * message : hello
     * senderId : 6
     * receiverId : 6
     */

    private String message;
    private int senderId;
    private int receiverId;
    private int classSessionId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }
}
