package com.example.oop_assignment;

import java.io.Serializable;
import java.util.HashSet;

// implements serializable to store in file as ObjectInputStream
public class Student implements Serializable {

    private String name;
    private String stuId;
    private String password;
    // string is combination of venue ID and its book date
    private HashSet<String> bookVenue = new HashSet<>();

    public Student(){}
    public Student(String name, String stuId, String password) {
        this.name = name;
        this.stuId = stuId;
        this.password = password;
    }

    public void setBookVenue(Venue v, String date) {
        String uniqueID = v.getId() + "@" + date;
       bookVenue.add(uniqueID);
    }
    public void setUnBookVenue(String id)
    {
        bookVenue.remove(id);
    }

    public String getName() {
        return name;
    }
    public String getStuId() {
        return stuId;
    }
    public String getPassword() {
        return password;
    }
    public HashSet<String> getBookVenue() {
        return bookVenue;
    }

}
