package com.p5m.me.data.request;

import com.p5m.me.utils.AppConstants;

public class LoginRequest implements java.io.Serializable {
    private static final long serialVersionUID = -5995156969528755923L;

    private String email;
    private String password;

    private int userType = 1;

    private String facebookId;
    private String googleId;
    private String firstName;
    private String lastName;
    private String gender;
    private String loginThrough;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest(String id, String firstName,String lastName,String email, String gender,String loginThrough) {
//        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
//        loginThrough = "facebook";
        this.loginThrough = loginThrough;
        if(loginThrough.equalsIgnoreCase(AppConstants.ApiParamValue.LOGINWITHFACEBOOK))
            this.facebookId = id;
        else
            this.googleId = id;
    }
public LoginRequest(String id, String firstName,String lastName,String email, String loginThrough) {
//        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
//        loginThrough = "facebook";
        this.loginThrough = loginThrough;
        if(loginThrough.equalsIgnoreCase(AppConstants.ApiParamValue.LOGINWITHFACEBOOK))
            this.facebookId = id;
        else
            this.googleId = id;
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

    public String getLoginThrough() {
        return loginThrough;


    }

    public void setLoginThrough(String loginThrough) {
        this.loginThrough = loginThrough;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
