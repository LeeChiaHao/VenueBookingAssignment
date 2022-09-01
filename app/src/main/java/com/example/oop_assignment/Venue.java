package com.example.oop_assignment;

import java.io.Serializable;
import java.util.HashMap;

public class Venue implements Serializable {
    private String id;
    private String date;
    private HashMap<Integer, String[]> startTime = new HashMap<>();
    private HashMap<Integer, String[]> endTime = new HashMap<>();

    public Venue(){}
    public Venue(String id, String d)
    {
        this.id = id;
        this.date = d;
        for(int i = 7; i <= 12; i++)
        {
            String[] init = {"-1", "",""};  // {studentID, eventType, eventDes}
            startTime.put(i,init); // initial all to - 1 means no booked
            endTime.put(i,init);
        }
    }

    public void setBookTime(int s, int e, String[] combination)
    {
        /* FOR EXAMPLE: 8 - 10
         * set startTime 8, 9 no available
         * set endTime 10, 9 no available
         * so the others user can choose maybe 6 - 8, (endTime (8) is still available)
         * or 10 - 11, startTime (10) is still available
         */
        for(int start = s; start < e; start ++)
        {
            if(start == s)
                startTime.put(start, combination);  //combination is stuentID, eventType, eventDes
            else{
                String[] value = startTime.get(start);
                value[0] = "0";
                startTime.put(start,value);
            }
        }
        for(int end = e; end> s; end--) {
            if(end == e)
                endTime.put(end,combination);
            else{
                String[] value = endTime.get(end);
                value[0] = "0";
                endTime.put(end,value);
            }
        }
    }

    public void setUnbookTime(int s, int e, String[] combination)
    {
        for(int start = s; start < e; start ++)
        {
            startTime.put(start, combination);
        }
        for(int end = e; end> s; end--) {
            endTime.put(end, combination);
        }
    }

    public HashMap<Integer, String[]> getStartTime() {
        return startTime;
    }
    public HashMap<Integer, String[]> getEndTime() {
        return endTime;
    }
    public String getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
}
