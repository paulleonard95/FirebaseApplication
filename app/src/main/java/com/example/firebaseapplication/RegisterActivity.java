package com.example.firebaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class RegisterActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar_UserReg);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void registerUser(View view)
    {
        //getting email and password from edit texts
        String first_name = ((EditText) findViewById(R.id.editText_First_Name)).getText().toString();
        String last_name = ((EditText) findViewById(R.id.editText_Last_Name)).getText().toString();
        String email = ((EditText) findViewById(R.id.editText_UserEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Password)).getText().toString();
        String cnf_password = ((EditText) findViewById(R.id.editText_Cnf_Password)).getText().toString();
        String female_btn = ((RadioButton) findViewById(R.id.btn_female)).getText().toString();
        String male_btn = ((RadioButton) findViewById(R.id.btn_male)).getText().toString();

        //checking if email and password are empty
        if (TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name))
        {
            Toast.makeText(this, "Please enter first & last name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(cnf_password))
        {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(cnf_password))
        {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 5 )
        {
            Toast.makeText(getApplicationContext(), "Password is too easy", Toast.LENGTH_LONG).show();
            return;
        }
        //if the email and password are not empty, displaying a progress bar
        progressBar.setVisibility(View.VISIBLE);
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            //display some message here
                            Toast.makeText(RegisterActivity.this, "Successfully register",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        }
                        else
                            {
                                //display some message here
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(RegisterActivity.this, "Failed Registration: "
                                + e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    public void goLogin(View view)
    {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }
}
