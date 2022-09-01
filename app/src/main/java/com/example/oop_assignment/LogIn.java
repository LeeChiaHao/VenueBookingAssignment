package com.example.oop_assignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class LogIn extends AppCompatActivity {
    private TextView SID, Password, register,idError, pwdError,emptyError;
    private Button logIn;
    private String stuId, stuPassword;
    private Student loginStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logIn = findViewById(R.id.LogIn);
        register = findViewById(R.id.register);

        // login is the button_effect and set an on click listener
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assign();
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                File studentFile = new File(directory, "student.bin");

                stuId = SID.getText().toString();
                stuPassword = Password.getText().toString();

                // check if all the fill is not empty, else prompt the error
                if (stuId.isEmpty()){
                   emptyError.setVisibility(View.VISIBLE);
                }else if(stuPassword.isEmpty()){
                    emptyError.setVisibility(View.VISIBLE);
                }else{
                    HashMap<String,Student> stuMap = FileFunction.loadFromStudentFile(studentFile);
                    System.out.println("Student: " + stuMap.keySet());
                    loginStudent = stuMap.get(stuId);
                    if (loginStudent != null){
                        // if the student exist in file, check the password
                        if (stuPassword.equals(loginStudent.getPassword())){
                            // All condition check done and activity_login success
                            Toast.makeText(LogIn.this, stuId + " Log In Successful!", Toast.LENGTH_SHORT).show();
                            ArrayList<String> stu = new ArrayList<>();
                            stu.add(loginStudent.getName());
                            stu.add(loginStudent.getStuId());
                            Intent intent = new Intent(LogIn.this, MainMenu.class);
                            intent.putStringArrayListExtra("studentValues",stu);
                            startActivity(intent);
                        }else{
                            pwdError.setVisibility(View.VISIBLE); // else prompt error message
                        }
                    }else {
                        idError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, Register.class);
                startActivity(intent);
            }
        });
    }

    // Override the backPressed so when user press back will exit the app
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // assign all xml elements
    private void assign()
    {
        SID = findViewById(R.id.studentID);
        Password = findViewById(R.id.password);
        idError = findViewById(R.id.SIDwrong);
        pwdError = findViewById(R.id.PSwrong);
        emptyError = findViewById(R.id.gotEmptyLogIn);
        idError.setVisibility(View.GONE);
        pwdError.setVisibility(View.GONE);
        emptyError.setVisibility(View.GONE);
    }
}


