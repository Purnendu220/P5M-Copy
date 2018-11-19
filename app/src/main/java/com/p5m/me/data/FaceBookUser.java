package com.p5m.me.data;

/**
 * Created by Varun John on 5/12/2018.
 */

public class FaceBookUser {
    String id;
    String name;
    String lastName;
    String gender;
    String email;

    public FaceBookUser(String id, String name,String lastName, String gender, String email) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
