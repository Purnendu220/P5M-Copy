package www.gymhop.p5m.data.request;

public class LoginRequest implements java.io.Serializable {
    private static final long serialVersionUID = -5995156969528755923L;

    private String email;
    private String password;

    private int userType = 1;

    private String facebookId;
    private String firstName;
    private String gender;
    private String loginThrough;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest(String facebookId, String firstName, String email, String gender) {
        this.facebookId = facebookId;
        this.firstName = firstName;
        this.gender = gender;
        this.email = email;
        loginThrough = "facebook";
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
