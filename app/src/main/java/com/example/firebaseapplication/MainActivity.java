package com.example.firebaseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        isNetworkConnectionAvailable();
    }


    public void checkNetworkConnection()
    {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please turn on internet connection");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

    }

    public  boolean isNetworkConnectionAvailable()

    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected)
        {
            Log.d("Network", "Connected");
            return true;
        }
        else
        {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    public void loginUser(View view)
    {
        String email = ((EditText) findViewById(R.id.editText_Log_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Log_password)).getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Login Successful",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), FirstPage.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Login Error",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void goRegister(View view)
    {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    public void forgottenPassword(View view)
    {
        startActivity(new Intent(MainActivity.this, ForgottenPassword.class));
    }
}
