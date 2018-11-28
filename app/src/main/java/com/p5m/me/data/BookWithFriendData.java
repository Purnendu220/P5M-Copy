package com.p5m.me.data;

import java.io.Serializable;

public class BookWithFriendData implements Serializable {
    public String firstName;
    public String email;
    public String gender;

    public BookWithFriendData(String firstName, String email,String gender) {
        this.firstName = firstName;
        this.email = email;
        this.gender=gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
