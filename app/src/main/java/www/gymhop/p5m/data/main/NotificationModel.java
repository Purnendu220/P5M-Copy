package www.gymhop.p5m.data.main;

public class NotificationModel implements java.io.Serializable {
    private static final long serialVersionUID = 1008345138655054871L;
    private int notificationTypeId;
    private long createdAt;
    private boolean isRead;
    private int id;
    private String notificationType;
    private UserWhoFiredEvent userWhoFiredEvent;
    private String message;
    private int objectDataId;

    public int getNotificationTypeId() {
        return this.notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationType() {
        return this.notificationType == null ? "" : notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public UserWhoFiredEvent getUserWhoFiredEvent() {
        return this.userWhoFiredEvent;
    }

    public void setUserWhoFiredEvent(UserWhoFiredEvent userWhoFiredEvent) {
        this.userWhoFiredEvent = userWhoFiredEvent;
    }

    public String getMessage() {
        return this.message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getObjectDataId() {
        return this.objectDataId;
    }

    public void setObjectDataId(int objectDataId) {
        this.objectDataId = objectDataId;
    }
}
