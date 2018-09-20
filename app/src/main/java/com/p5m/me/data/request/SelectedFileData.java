package com.p5m.me.data.request;

public class SelectedFileData {
    private String filepath;
    private int fileUploadProgress;
    private int fileUploadStatus;
    private long fileId;
    private String specialToken;

    public SelectedFileData(String filepath, int fileUploadProgress, int fileUploadStatus, long fileId,String specialToken) {
        this.filepath = filepath;
        this.fileUploadProgress = fileUploadProgress;
        this.fileUploadStatus = fileUploadStatus;
        this.fileId = fileId;
        this.specialToken=specialToken;
    }

    public String getSpecialToken() {
        return specialToken;
    }

    public void setSpecialToken(String specialToken) {
        this.specialToken = specialToken;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getFileUploadProgress() {
        return fileUploadProgress;
    }

    public void setFileUploadProgress(int fileUploadProgress) {
        this.fileUploadProgress = fileUploadProgress;
    }

    public int getFileUploadStatus() {
        return fileUploadStatus;
    }

    public void setFileUploadStatus(int fileUploadStatus) {
        this.fileUploadStatus = fileUploadStatus;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }
}
