package com.example.oop_assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HistoryMenu extends AppCompatActivity {
    private ListView showBook;
    private LinearLayout layout;
    private ArrayList<String> studentValues;
    private Button bookBtn;
    private Student student;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__menu);

        Intent intent = getIntent();
        studentValues = intent.getStringArrayListExtra("studentValues");
        String studentID = studentValues.get(1);

        showBook = findViewById(R.id.showBooked);
        layout = findViewById(R.id.historyEmpty);
        bookBtn = findViewById(R.id.bookbtn2);
        layout.setVisibility(View.GONE);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File studentFile = new File(directory,"student.bin");

        HashMap <String, Student> students = FileFunction.loadFromStudentFile(studentFile);
        student = students.get(studentID);
        ArrayList<String> bookIDs = sortList(student.getBookVenue());

        // if the student have not book any venue, it will show the hint word for you to book a new venue
        if(bookIDs.isEmpty())
        {
            layout.setVisibility(View.VISIBLE);
            bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoryMenu.this, VenueMenu.class);
                    intent.putStringArrayListExtra("studentValues",studentValues);
                    startActivity(intent);
                }
            });
        }else{
            BookedVenueAdapter adapter = new BookedVenueAdapter(this, bookIDs, directory, studentID);
            showBook.setAdapter(adapter);
        }
    }

    // BookedVenueAdapter is to customize our list view
    class BookedVenueAdapter extends ArrayAdapter<String>{
        TextView bookedVenue,duration, date;
        File directory;
        String studentID;
        public BookedVenueAdapter(Context context, ArrayList<String> bookVenues, File file, String sid)
        {
            super(context,0,bookVenues);
            directory = file;
            studentID = sid;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final String uniqueId = getItem(position);
            String[] venueID = uniqueId.split("@"); // venueID @ date
            final long getDuration = countDuration(venueID[1]); // use the date to count the duration
            final String durations;
            // set different description based on the date
            if(getDuration == 0)
            {
                durations = " (Today)";
            }else if(getDuration >= 0)
            {
                durations = " (" + getDuration + " days)";
            }else
            {
                durations = "Expired";
            }
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.unbookvenue_row,parent,false);
            }
            bookedVenue = convertView.findViewById(R.id.bookedVenue);
            duration = convertView.findViewById(R.id.duration);
            date = convertView.findViewById(R.id.date);

            bookedVenue.setText(venueID[0]);
            date.setText(venueID[1]);
            duration.setText(durations);

            if(getDuration < 0)
            {
                bookedVenue.setTextColor(getColor(R.color.ERRORCOLOR));
                bookedVenue.setPaintFlags(bookedVenue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                date.setTextColor(getColor(R.color.ERRORCOLOR));
                date.setPaintFlags(bookedVenue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                duration.setTextColor(getColor(R.color.ERRORCOLOR));
            }else{
                bookedVenue.setTextColor(getColor(R.color.Black));
                bookedVenue.setPaintFlags(bookedVenue.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                date.setPaintFlags(bookedVenue.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                date.setTextColor(getColor(R.color.Black));
                duration.setTextColor(getColor(R.color.Black));
            }

            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoryMenu.this, UnbookForm.class);
                    intent.putExtra("UniqueID", uniqueId);
                    intent.putExtra("Duration", getDuration);
                    intent.putStringArrayListExtra("studentValues",studentValues);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private long countDuration(String d)
    {
        String[] values = d.split("/");
        String year = values[0];
        String month = values[1];
        String day = values[2];
        String[] date = BookingForm.todayDate();
        System.out.println("Date: " + date[0] + date[1] + date[2]);
        String startDate = date[0] + "-" + date[1] + "-" + date[2];
        String endDate = day + "-" + month + "-" + year;

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

    /* sort the booking date with ascending order
        the parameter is the student object's bookVenue
        this function will return bookVenue with sorted date
        so it can display the venue with sorted date
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> sortList(Set<String> combination)
    {
        HashMap<Long, ArrayList<String>> dates = new HashMap<>();
        ArrayList<String> sortedID = new ArrayList<>();
        ArrayList<String> passed = new ArrayList<>();

        for(String uniqueID : combination)
        {
            String[] values = uniqueID.split("@");
            long duration = countDuration(values[1]);
            ArrayList<String> tmp = new ArrayList<>();

            /* add the venue that have not expired, the key is the remaining duration
                and the value is a list of venueID belongs to this duration
             */
            if(duration < 0){
                passed.add(uniqueID);
            }else if(dates.get(duration) != null)
            {
                tmp = dates.get(duration);
                tmp.add(uniqueID);
                dates.put(duration, tmp);

            }else
            {
                tmp.add(uniqueID);
                dates.put(duration, tmp);
            }
        }
        List<Long> sortedDuration = new ArrayList<>(dates.keySet());
        Collections.sort(sortedDuration); // sort the dates list

        for(Long d :sortedDuration)
        {
            sortedID.addAll(dates.get(d)); // add all the values to sorted ID
        }
        sortedID.addAll(passed); // the venue that expired add lastly
        return  sortedID;
    }

    // To make sure this page will always return to the main menu
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoryMenu.this, MainMenu.class);
        intent.putStringArrayListExtra("studentValues",studentValues);
        startActivity(intent);
    }
}
