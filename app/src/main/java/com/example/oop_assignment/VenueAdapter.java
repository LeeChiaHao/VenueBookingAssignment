package com.example.oop_assignment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

// extends fragment pager adapter because we want to create 3 fragment
public class VenueAdapter extends FragmentPagerAdapter {
    private ArrayList<String> studentValues;
    public VenueAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<String> id) {
        super(fm, behavior);
        studentValues = id;
    }

    // preparing different venue data here
    private ArrayList<String> LectureList()
    {
        ArrayList<String> Lecture= new ArrayList<>();
        Lecture.add("CLC MSMX 0001");
        Lecture.add("CLC MSMX 0002");
        Lecture.add("CLC MSMX 0003");
        Lecture.add("CLC MSMX 1001");
        Lecture.add("CLC MSMX 1002");
        Lecture.add("CLC MSMX 1003");
        Lecture.add("CLC MSMX 2001");
        Lecture.add("CLC MSMX 2002");
        Lecture.add("CLC MSMX 2003");
        Lecture.add("CLC MSMX 3001");
        Lecture.add("CLC MSMX 3002");
        return Lecture;
    }

    private ArrayList<String> TutorialList()
    {
        ArrayList<String> Tutorial = new ArrayList<>();
        Tutorial.add("FIST MNCR 1001");
        Tutorial.add("FIST MNCR 1002");
        Tutorial.add("FIST MNCR 1003");
        Tutorial.add("FIST MNCR 1004");
        Tutorial.add("FIST MNCR 2001");
        Tutorial.add("FIST MNCR 2002");
        Tutorial.add("FIST MNCR 2003");
        Tutorial.add("FIST MNCR 2004");
        Tutorial.add("CLC MNCR 2010");
        Tutorial.add("CLC MNCR 2011");
        Tutorial.add("CLC MNCR 2012");
        Tutorial.add("CLC MNCR 2013");
        Tutorial.add("CLC MNCR 2014");
        Tutorial.add("CLC MNCR 3010");
        Tutorial.add("CLC MNCR 3011");
        Tutorial.add("CLC MNCR 3012");
        return Tutorial;
    }

    private ArrayList<String> SportList()
    {
        ArrayList<String> Sport = new ArrayList<>();
        Sport.add("Football Field 1");
        Sport.add("Football Field 2");
        Sport.add("Basketball Court 1");
        Sport.add("Basketball Court 2");
        Sport.add("Tennis Court 1");
        Sport.add("Tennis Court 2");
        Sport.add("Futsal 1");
        Sport.add("Futsal 2");

        return Sport;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        VenueFragment venueFragment = new VenueFragment();
        position = position + 1;
        Bundle bundle = new Bundle();

        ArrayList<String> iamLecture = LectureList();
        ArrayList<String> iamTutorial = TutorialList();
        ArrayList<String> iamSport = SportList();

        /* set different data at different fragment, pass the data via bundle
           we will get the data at venueFragment.java
         */
        if(position == 1)
        {
            bundle.putStringArrayList("message",iamLecture);
            bundle.putStringArrayList("studentValues",studentValues);
        }
        else if (position == 2)
        {
            bundle.putStringArrayList("message",iamTutorial);
            bundle.putStringArrayList("studentValues",studentValues);
        }
        else
        {
            bundle.putStringArrayList("message",iamSport);
            bundle.putStringArrayList("studentValues",studentValues);
        }
        venueFragment.setArguments(bundle);
        return venueFragment;
    }

    @Override
    public int getCount(){
        return 3;
    } // set number of fragment

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) { // set fragment title
        position += 1;
        if(position == 1)
        {
            return "Lecture Hall";
        }
        else if (position == 2)
        {
            return "Tutorial Room";
        }
        else
        {
            return "Sport Venue";
        }
    }
}
