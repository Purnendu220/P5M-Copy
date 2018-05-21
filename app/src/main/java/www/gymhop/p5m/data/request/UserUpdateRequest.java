package www.gymhop.p5m.data.request;

public class UserUpdateRequest implements java.io.Serializable {

    public boolean favTrainerNotification;

    public UserUpdateRequest(boolean favTrainerNotification) {
        this.favTrainerNotification = favTrainerNotification;
    }
}
