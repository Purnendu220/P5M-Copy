package www.gymhop.p5m.data;

public class TrainerDetail implements java.io.Serializable {
    private static final long serialVersionUID = 5464270229681801949L;
    private String firstName;
    private boolean trainerDeleted;
    private int id;
    private boolean trainerStatus;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean getTrainerDeleted() {
        return this.trainerDeleted;
    }

    public void setTrainerDeleted(boolean trainerDeleted) {
        this.trainerDeleted = trainerDeleted;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getTrainerStatus() {
        return this.trainerStatus;
    }

    public void setTrainerStatus(boolean trainerStatus) {
        this.trainerStatus = trainerStatus;
    }
}
