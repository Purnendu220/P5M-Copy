package com.p5m.me.data.request;

import com.p5m.me.utils.AppConstants;

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

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    private String googleId ;
    private String loginThrough;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String id, String firstName,String lastName, int storeId,String loginThrough) {
        this.firstName = firstName;
        this.lastName = lastName;

        this.storeId = storeId;
        this.loginThrough = loginThrough;
        if(loginThrough.equalsIgnoreCase(AppConstants.ApiParamValue.LOGINWITHFACEBOOK))
            this.facebookId = id;
        else
            this.googleId = id;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getLoginThrough() {
        return loginThrough;
    }

    public void setLoginThrough(String loginThrough) {
        this.loginThrough = loginThrough;
    }
}
