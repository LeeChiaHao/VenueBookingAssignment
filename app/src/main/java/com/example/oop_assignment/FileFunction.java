package com.example.oop_assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

// Every function contains try and catch block to handle exception
public class FileFunction {
    /* loadFromFile will take the parameter file, which is the file name. and return the file content
        which is a HashMap*/
    public static HashMap<String, Student> loadFromStudentFile(File file)
    {
        HashMap <String, Student> students = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream(String.valueOf(file));
            ObjectInputStream in = new ObjectInputStream(fis);
            students = (HashMap<String,Student>) in.readObject();
            fis.close();
            in.close();

        }catch (IOException | ClassNotFoundException e){
            System.out.println("ERROR 401:"+e);
        }
        return students;
    }

    public static HashMap<String, Venue> loadFromVenueFile(File file)
    {
        HashMap <String, Venue>venues = new HashMap<>();
        try {
            FileInputStream fis = new FileInputStream(String.valueOf(file));
            ObjectInputStream in = new ObjectInputStream(fis);
            venues = (HashMap<String,Venue>) in.readObject();
            fis.close();
            in.close();

        }catch (IOException | ClassNotFoundException e){
            System.out.println("ERROR 402:"+e);
        }
        return venues;
    }

    // writeToFile will also take the file name, and second parameter will write into the file
    public static void writeToStudentFile(File file, HashMap<String, Student> students)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(students);
            fos.close();
            out.close();

        }catch (IOException error)
        {
            System.out.println("ERROR 403: " + error);
        }
    }
    public static void writeToVenueFile(File file, HashMap<String, Venue> venues)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(String.valueOf(file));
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(venues);
            fos.close();
            out.close();

        }catch (IOException error)
        {
            System.out.println("ERROR 404: " + error);
        }
    }
}
