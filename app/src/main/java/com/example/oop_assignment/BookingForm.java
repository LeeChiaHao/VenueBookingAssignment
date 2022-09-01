package com.example.oop_assignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BookingForm extends AppCompatActivity {
    private TextView bookID, des, y, m, da,bookAdy,emptyError,bookedError,
            timeError, dateError, agreeError,desLengthError;
    private Button bookBtn;
    private Spinner bookType, bookStart, bookEnd;
    private CheckBox checked;
    private String eventType, description, year, month, day;
    private int startTime, endTime;
    private ArrayList<String> studentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__form);
        assign(); // assign all xml elements

        Intent intent = getIntent();
        final String bookVenueID = intent.getStringExtra("venue");
        studentValues = intent.getStringArrayListExtra("studentValues");
        final String studentID = studentValues.get(1);

        setBookid(bookVenueID);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                assign(); // assign every xml elements to get the data

                boolean isNoEmpty;
                boolean isAgree = false;
                boolean isValidTime = false;
                boolean isValidDate = false;

                // All condition checking to check the from is with valid data and no empty
                isNoEmpty = checkEmpty();
                if(isNoEmpty) {
                    if(description.length() <= 30)
                    {
                        isAgree = checked.isChecked();
                        if(isAgree) {
                            isValidTime = checkTime();
                            isValidDate = checkDate();
                            if(!isValidTime) {
                                timeError.setVisibility(View.VISIBLE);
                            }
                            if(!isValidDate) {
                                dateError.setVisibility(View.VISIBLE);
                            }
                        }else{
                            agreeError.setVisibility(View.VISIBLE);
                        }
                    }else{
                        desLengthError.setVisibility(View.VISIBLE);
                    }
                }else{
                    emptyError.setVisibility(View.VISIBLE);
                }

                // If all data fill, then try to check the file to check is the venue available
                if(isNoEmpty && isAgree && isValidTime && isValidDate)
                {
                    String combineDate = year + "/" + month + "/" + day;
                    Venue bookVenue = new Venue(bookVenueID, combineDate);

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    File venueFile = new File(directory,"venue.bin");
                    File studentFile = new File(directory, "student.bin");

                    boolean write = venueIsBooked(venueFile,studentFile, bookVenue, studentID, combineDate);
                    // if write is true, means the venue has been booked by others
                    if(write)
                    {
                        if(bookAdy.getVisibility() == View.GONE)
                        {
                            bookedError.setVisibility(View.VISIBLE);
                        }
                    }
                    else // if book success, update the student object's bookVenue
                    {
                        updateStudentFile(studentFile, bookVenue, studentID, combineDate);
                        Toast.makeText(BookingForm.this, "Book Success!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(BookingForm.this, VenueMenu.class);
                        intent.putExtra("studentValues", studentValues);
                        startActivity(intent);
                    }
                }
            }
        });
    }
    private void assign()
    {
        bookID = findViewById(R.id.bookID);
        bookBtn = findViewById(R.id.bookBook);

        bookStart = findViewById(R.id.bookStartTime);
        bookEnd = findViewById(R.id.bookEndTime);

        bookType = findViewById(R.id.bookType);
        eventType = bookType.getSelectedItem().toString();

        bookAdy = findViewById(R.id.bookAdy);
        bookAdy.setVisibility(View.GONE);

        des = findViewById(R.id.bookDescription);
        description = des.getText().toString();

        y = findViewById(R.id.bookYear);
        year = y.getText().toString();

        m = findViewById(R.id.bookMonth);
        month = m.getText().toString();

        da = findViewById(R.id.bookDay);
        day = da.getText().toString();

        checked = findViewById(R.id.bookAgree);

        emptyError = findViewById(R.id.Empty);
        emptyError.setVisibility(View.GONE);

        bookedError = findViewById(R.id.isBooked);
        bookedError.setVisibility(View.GONE);

        timeError = findViewById(R.id.invalidTime);
        timeError.setVisibility(View.GONE);

        dateError = findViewById(R.id.invalidDate);
        dateError.setVisibility(View.GONE);

        agreeError = findViewById(R.id.isChecked);
        agreeError.setVisibility(View.GONE);

        desLengthError = findViewById(R.id.desLengtherr);
        desLengthError.setVisibility(View.GONE);

    }
    private void setBookid(String bookVenueID)
    {
        try {
            bookID.setText(bookVenueID);
        }catch(NullPointerException exception)
        {
            bookID.setText(exception.toString());
        }
    }
    private boolean checkEmpty()
    {
        boolean isNoempty = true;
        if(bookType.getSelectedItem().toString().equals("") || description.equals("")
            || year.equals("") || month.equals("") || day.equals("") )
        {
            isNoempty = false;
        }
        return  isNoempty;

    }
    private boolean checkTime()
    {
        String stime = bookStart.getSelectedItem().toString();
        String etime = bookEnd.getSelectedItem().toString();

        if(stime.length() == 4)
        {
            startTime = Integer.parseInt(stime.substring(0,1));
        }else
        {
            startTime = Integer.parseInt(stime.substring(0,2));
        }

        if(etime.length() == 4)
        {
            endTime = Integer.parseInt(etime.substring(0,1));
        }else
        {
            endTime = Integer.parseInt(etime.substring(0,2));
        }
        return startTime < endTime;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkDate()
    {
        boolean isValid = true;
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);

        if(y < 2020){isValid = false;}
        else if(m> 12 || m < 0){isValid = false;}
        else if (d <= 31 && d > 0 ){
            if(m == 2)
            {
                if(d > 28){isValid = false;}
            }
            if(m == 2 || m == 4 || m == 6 || m == 9 || m == 11 )
            {
                if(d > 30){isValid = false;}
            }
        }
        else
        {
            isValid = false;
        }

        if(countDuration(year,month,day) <= 0)
        {
            isValid = false;
        }
        return isValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long countDuration(String y, String m, String d)
    {
        String[] date = todayDate();
        System.out.println("Date: " + date[0] + date[1] + date[2]);
        String startDate = date[0] + "-" + date[1] + "-" + date[2];
        String endDate = d + "-" + m + "-" + y;

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date start = format.parse(startDate);
            Date end = format.parse(endDate);

            long diff = end.getTime() - start.getTime();
            return TimeUnit.MILLISECONDS.toDays(diff);
        }catch (ParseException | NullPointerException e)
        {
            return -1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] todayDate()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate).split("-");
    }

    private boolean venueIsBooked(File vfile,File sfile, Venue v, String studentID, String date)
    {
        boolean timeIsBooked = false;  // check the time be booked or not
        HashMap<String, Venue> allVenues = FileFunction.loadFromVenueFile(vfile);
        String uniqueID = v.getId() + "@" + date;
        Venue existedVenue = allVenues.get(uniqueID);

        String[] values = {studentID, eventType, description};

        /* existedVenue not NULL means the venue already store in file
        *  so check if the time is available or not
        */
        if(existedVenue != null)
        {
            HashMap<Integer, String[]> startMap = existedVenue.getStartTime();
            HashMap<Integer, String[]> endMap = existedVenue.getEndTime();

            for(int start = startTime; start < endTime; start++)
            {
                String [] checkValues = startMap.get(start);
                if(!Objects.equals(checkValues[0], "-1"))
                {
                    timeIsBooked = true;
                }
            }
            for(int end = endTime; end > startTime; end--) {
                String [] checkValues = endMap.get(end);
                if(!Objects.equals(checkValues[0],"-1")) {
                    timeIsBooked = true;
                }
            }
            HashMap<String, Student> students = FileFunction.loadFromStudentFile(sfile);
            Student student = students.get(studentID);
            HashSet<String> bookV = student.getBookVenue();

            // if the student book the venue at the same date before, then cannot book again
            if(bookV.contains(existedVenue.getId() + "@"+ date))
            {
                timeIsBooked = true;
                bookAdy.setVisibility(View.VISIBLE);
            }
            // If the time havent been booked, then IS OK to set it
            if(!timeIsBooked) {
                existedVenue.setBookTime(startTime, endTime,values);
                allVenues.put(uniqueID, existedVenue);
                FileFunction.writeToVenueFile(vfile,allVenues);
                System.out.println("My new ID is inside: " + uniqueID);
            }
        }
        else {  // the venue is totally haven't been booked so just set the time
            v.setBookTime(startTime, endTime, values);
            allVenues.put(uniqueID, v);
            FileFunction.writeToVenueFile(vfile,allVenues);
            System.out.println("My new ID is outside: " + uniqueID);
        }
        return timeIsBooked;
    }

    private void updateStudentFile(File f, Venue bookVenue, String sID, String date)
    {
        HashMap<String, Student> students = FileFunction.loadFromStudentFile(f);
        Student student = students.get(sID);

        // update the bookVenue in student
        student.setBookVenue(bookVenue, date);
        students.put(student.getStuId(),student);
        System.out.println("Are u booked: " + student.getBookVenue().toString());
        FileFunction.writeToStudentFile(f,students);
    }
}
