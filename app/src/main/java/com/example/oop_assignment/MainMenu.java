package com.example.oop_assignment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        TextView welcome = findViewById(R.id.welcome);
        Intent intent = getIntent();
        final ArrayList<String> studentValues = intent.getStringArrayListExtra("studentValues");
        String name = studentValues.get(0) + "!";
        final String id = studentValues.get(1);
        welcome.setText(name);

        // different button_effect in main menu will go to different activity
        Button bookingBtn = findViewById(R.id.bookbtn);
        bookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, VenueMenu.class);
                intent.putExtra("studentID",id);
                intent.putStringArrayListExtra("studentValues",studentValues);
                 startActivity(intent);
            }
        });

        Button noticeBtn = findViewById(R.id.noticebtn);
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, HistoryMenu.class);
                intent.putStringArrayListExtra("studentValues",studentValues);
                startActivity(intent);
            }
        });

        final Dialog dialog = new Dialog(this);
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // the dialog is to send the pop up window to double confirm the exit
                dialog.setContentView(R.layout.pop_up);
                Button yes,no;
                TextView popUpText = dialog.findViewById(R.id.popUpText);
                yes = dialog.findViewById(R.id.yesyes);
                no = dialog.findViewById(R.id.nono);

                popUpText.setText(getText(R.string.logoutPop));
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainMenu.this, LogIn.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
    }

    /* Override the backPressed so user will exit the app
        instead of go back to the previous page (Login Page)
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}