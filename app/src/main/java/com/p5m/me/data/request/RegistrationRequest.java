package com.p5m.me.data.request;

public class RegistrationRequest implements java.io.Serializable {
    private static final long serialVersionUID = 4834428472264496728L;

    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private int userType = 1;
    private String email;
    private int storeId;

    private String facebookId;
    private String loginThrough;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String facebookId, String firstName,String lastName, int storeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.facebookId = facebookId;
        this.storeId = storeId;
        loginThrough = "facebook";
    }

    public RegistrationRequest(String firstName,String lastName, String password, String gender, int userType, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.userType = userType;
        this.email = email;
    }


    public String getLastName() { return this.lastName == null ? "" : lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

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


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
