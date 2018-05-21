package www.gymhop.p5m.data.request;

public class RegistrationRequest implements java.io.Serializable {
    private static final long serialVersionUID = 4834428472264496728L;

    private String firstName;
    private String password;
    private String gender;
    private int userType = 1;
    private String email;

    private String facebookId;
    private String loginThrough;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String facebookId, String firstName) {
        this.firstName = firstName;
        this.facebookId = facebookId;
        loginThrough = "facebook";
    }

    public RegistrationRequest(String firstName, String password, String gender, int userType, String email) {
        this.firstName = firstName;
        this.password = password;
        this.gender = gender;
        this.userType = userType;
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return this.password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return this.gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return this.email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
