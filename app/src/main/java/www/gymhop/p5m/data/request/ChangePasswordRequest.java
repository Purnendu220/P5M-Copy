package www.gymhop.p5m.data.request;

public class ChangePasswordRequest implements java.io.Serializable {
    private static final long serialVersionUID = -1355248798324517494L;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private int userId;

    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword, int userId) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
        this.userId = userId;
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
