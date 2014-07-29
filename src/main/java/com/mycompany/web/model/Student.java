package com.mycompany.web.model;

public class Student {
    private String firstname;
    private String lastname;
    private int points;

    public final String getFirstname() {
        return firstname;
    }

    public final void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public final String getLastname() {
        return lastname;
    }

    public final void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public final int getPoints() {
        return points;
    }

    public final void setPoints(int points) {
        this.points = points;
    }
}
