package com.example.oop_assignment;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText Name, ID, Password, Password2;
    private Button Register;
    private String registerName, registerID, registerPwd, registerPwd2;
    private TextView NoName, NoID, IDExists, NoPass, wrongPass2,empty;
    private Student newStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register = findViewById(R.id.save);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assign();
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File studentFile = new File(directory, "student.bin");

                boolean isFill = checkFill();
                // checkFill function is check all the form has been filled
                if(isFill){
                    newStudent = new Student(registerName, registerID, registerPwd);
                    HashMap<String,Student> hashMap = new HashMap<>();

                    boolean isExist = false;
                    // then check the existence in file, if existed, cannot register with same ID
                    if (studentFile.exists()){
                        hashMap  = FileFunction.loadFromStudentFile(studentFile);
                        if (hashMap.containsKey(registerID)){
                            IDExists.setVisibility(View.VISIBLE);
                            isExist = true;
                        }
                    }
                    // if false means no exist, write it to the student file
                    if(!isExist) {
                        hashMap.put(registerID,newStudent);
                        FileFunction.writeToStudentFile(studentFile, hashMap);
                        Toast.makeText(Register.this, registerID +" Register Successfully !!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, LogIn.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
    // assign all the xml elements
    private void assign() {
        Name = findViewById(R.id.editText1);
        ID = findViewById(R.id.editText2);
        Password = findViewById(R.id.editText3);
        Password2 = findViewById(R.id.editText4);

        empty = findViewById(R.id.gotEmpty);
        empty.setVisibility(View.GONE);

        NoName = findViewById(R.id.NoName);
        NoName.setVisibility(View.GONE);

        NoID = findViewById(R.id.NoID);
        NoID.setVisibility(View.GONE);

        IDExists = findViewById(R.id.IDExists);
        IDExists.setVisibility(View.GONE);

        NoPass = findViewById(R.id.NoPass);
        NoPass.setVisibility(View.GONE);

        wrongPass2 = findViewById(R.id.wrongPass2);
        wrongPass2.setVisibility(View.GONE);

    }

    // check if the name, id, and password is valid
    private boolean checkFill() {
        boolean isNoEmpty = true;

        registerName = Name.getText().toString();
        registerID = ID.getText().toString();
        registerPwd = Password.getText().toString();
        registerPwd2 = Password2.getText().toString();

        if (registerName.isEmpty() && registerID.isEmpty() && registerPwd.isEmpty() && registerPwd2.isEmpty()) {
            isNoEmpty = false;
           empty.setVisibility(View.VISIBLE);
        }else if(registerName.isEmpty())
        {
            NoName.setVisibility(View.VISIBLE);isNoEmpty = false;
        }else if(registerID.isEmpty())
        {
            NoID.setVisibility(View.VISIBLE);isNoEmpty = false;
        }else if(registerPwd.isEmpty())
        {
            NoPass.setVisibility(View.VISIBLE);isNoEmpty = false;
        }else if(registerPwd2.isEmpty())
        {
            NoPass.setVisibility(View.VISIBLE);isNoEmpty = false;
        }
        else if (!registerPwd.equals(registerPwd2)) {
            wrongPass2.setVisibility(View.VISIBLE);isNoEmpty = false;
        }
        return isNoEmpty;
    }
}


