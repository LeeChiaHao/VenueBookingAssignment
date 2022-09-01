package com.example.oop_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class VenueMenu extends AppCompatActivity {
    private ArrayList<String> studentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_menu);

        Intent intent = getIntent();
        studentValues = intent.getStringArrayListExtra("studentValues");

        ViewPager viewPager;
        VenueAdapter adapter;
        TabLayout tabLayout;

        // set the xml elements to display the data
        viewPager = findViewById(R.id.pager);
        adapter = new VenueAdapter(getSupportFragmentManager(),3, studentValues);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    // to make sure the back press will always go back to the main menu
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VenueMenu.this, MainMenu.class);
        intent.putStringArrayListExtra("studentValues",studentValues);
        startActivity(intent);
    }
}