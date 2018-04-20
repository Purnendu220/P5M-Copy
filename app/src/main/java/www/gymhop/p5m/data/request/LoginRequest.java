package www.gymhop.p5m.data.request;

public class LoginRequest implements java.io.Serializable {
    private static final long serialVersionUID = -5995156969528755923L;

    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
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
