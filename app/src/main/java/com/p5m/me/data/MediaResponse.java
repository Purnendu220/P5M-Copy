package com.p5m.me.data;

public class MediaResponse implements java.io.Serializable {
    private static final long serialVersionUID = 6360105176345827096L;
    private long createdAt;
    private int objectMediaTypeId;
    private long modifiedAt;
    private String thumbnailPath;
    private int width;
    private int id;
    private String mediaPath;
    private int objectDataId;
    private String mediaName;
    private boolean status;
    private int height;

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getObjectMediaTypeId() {
        return this.objectMediaTypeId;
    }

    public void setObjectMediaTypeId(int objectMediaTypeId) {
        this.objectMediaTypeId = objectMediaTypeId;
    }

    public long getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getThumbnailPath() {
        return this.thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaPath() {
        return this.mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public int getObjectDataId() {
        return this.objectDataId;
    }

    public void setObjectDataId(int objectDataId) {
        this.objectDataId = objectDataId;
    }

    public String getMediaName() {
        return this.mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
