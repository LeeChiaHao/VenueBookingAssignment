package com.example.oop_assignment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class UnbookForm extends AppCompatActivity {
    private TextView bookedVenue, bookedType, bookedDes, bookedDate, bookedTime, tooNear;
    private String uniqueID, studentID, venueID, date;
    private Integer startTime, endTime;
    private Long duration;
    private Dialog dialog;
    private File venueFile, studentFile;
    private ArrayList<String> studentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbook_form);
        init(); // initialize all the xml elements

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        venueFile = new File(directory,"venue.bin");
        studentFile = new File(directory,"student.bin");

        // get the time and its corresponding information
        HashMap<Integer, String[]> times = getBookedTime(uniqueID,studentID, venueFile);
        assign(times);
        dialog = new Dialog(this);
    }
    // this showPopup will pass to the unBook btn in xml file
    public void showPopup(View v)
    {
        dialog.setContentView(R.layout.pop_up);
        Button yes,no;
        TextView unbookVenue, unbookDate;

        yes = dialog.findViewById(R.id.yesyes);
        no = dialog.findViewById(R.id.nono);
        unbookVenue = dialog.findViewById(R.id.unbookVenueID);
        unbookDate = dialog.findViewById(R.id.unbookDate);

        unbookVenue.setText(venueID);
        unbookVenue.setPaintFlags(unbookVenue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        unbookDate.setText(date);
        unbookDate.setPaintFlags(unbookDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        // if click yes, check the duration
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // duration smaller than 2 cannot be unbook
                if(duration <= 2)
                {
                    Toast.makeText(UnbookForm.this, "Can not unbook", Toast.LENGTH_SHORT).show();
                    tooNear.setVisibility(View.VISIBLE);

                }else{
                    unbook(); // duration ok, process this function
                    Toast.makeText(UnbookForm.this, "The Venue is unbook...", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UnbookForm.this, HistoryMenu.class);
                    intent.putStringArrayListExtra("studentValues",studentValues);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private HashMap<Integer, String[]> getBookedTime(String uid, String sid, File file)
    {
        HashMap<String, Venue> venues = FileFunction.loadFromVenueFile(file);
        Venue venue = venues.get(uid);
        System.out.println("Venuesss: " + venues.toString());
        HashMap<Integer, String[]> bookedTime = new HashMap<>();
        HashMap<Integer, String[]> startTime = venue.getStartTime();
        HashMap<Integer, String[]> endTime = venue.getEndTime();

        // get the start and end time that belongs to this student
        for(Integer start : startTime.keySet())
        {
            String [] values = startTime.get(start);
            if(values[0].equals(sid))
            {
                bookedTime.put(start,values);
            }
        }
        for(Integer end : endTime.keySet())
        {
            String[] values = endTime.get(end);
            if(values[0].equals(sid))
            {
                bookedTime.put(end,values);
            }
        }
        return bookedTime;
    }

    private void init()
    {
        bookedVenue = findViewById(R.id.bookedVenue);
        bookedType = findViewById(R.id.bookedType);
        bookedDes = findViewById(R.id.bookedDes);
        bookedDate = findViewById(R.id.bookedDate);
        bookedTime = findViewById(R.id.bookedTime);

        tooNear = findViewById(R.id.tooNear);
        tooNear.setVisibility(View.GONE);

        Intent intent = getIntent();
        studentValues = intent.getStringArrayListExtra("studentValues");
        uniqueID = intent.getStringExtra("UniqueID");
        studentID = studentValues.get(1);
        duration = intent.getLongExtra("Duration",0);

        String[] id_date = uniqueID.split("@");
        venueID = id_date[0];
        date = id_date[1];

    }

    /* set the booked venue info to the Form
        the venue ID and date already get from HistoryMenu
        times contains the booked time and
        string[] is the info such as event type and des
     */
    private void assign(HashMap<Integer, String[]> times)
    {
        String[] values = {"","",""};
        int x = 0;
        for(Integer time : times.keySet()) {
            if (x == 0) {
                startTime = time;
                x++;
                values = times.get(time);
            } else{
                endTime = time;
            }
        }
        bookedVenue.setText(venueID);
        bookedDate.setText(date);
        bookedType.setText(values[1]);
        bookedDes.setText(values[2]);
        String time = startTime + "pm - " + endTime + "pm";
        bookedTime.setText(time);
    }

    private void unbook()
    {
        HashMap<String, Venue> venues = FileFunction.loadFromVenueFile(venueFile);
        Venue venue = venues.get(uniqueID);
        /* checkDelete is check, is the current time is the only time that been booked
           if after remove the time, no others student is booking the venue, then this venue can be remove
           else we are just set that time to available
         */
        boolean delete = checkDeleteVenue(venue, startTime, endTime);
        if(delete)
        {
            venues.remove(uniqueID);
        }else{
            venues.put(uniqueID, venue);
        }
        // then update to venue and student file
        FileFunction.writeToVenueFile(venueFile, venues);

        HashMap<String, Student> students = FileFunction.loadFromStudentFile(studentFile);
        Student student = students.get(studentID);

        student.setUnBookVenue(uniqueID);
        students.put(student.getStuId(), student);
        FileFunction.writeToStudentFile(studentFile, students);
    }
    private boolean checkDeleteVenue(Venue v, int start,int end)
    {
        boolean canDelete = true;
        String[] initial = {"-1","",""};

        v.setUnbookTime(start, end, initial);
        HashMap<Integer, String[]> stime = v.getStartTime();
        HashMap<Integer, String[]> etime = v.getEndTime();
        /* check all the time for start and end time
            array[0] is the student ID, -1 is default(available)
         */
        for(int i = 7; i <= 12 ; i++)
        {
            String[] check = stime.get(i);
            String[] check2 = etime.get(i);
            if(!check[0].equals("-1") || !check2[0].equals("-1"))
            {
                canDelete = false;
                System.out.println("Start: " + check[0]);
            }
        }
        return canDelete;
    }
}