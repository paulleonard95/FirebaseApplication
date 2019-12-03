package com.example.firebaseapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPassword extends AppCompatActivity
{

    EditText forgotten_email;
    Button btn_forgotten_email;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        forgotten_email = findViewById(R.id.editText_Forgot_Password_Email);
        btn_forgotten_email = findViewById(R.id.button_forgotten_password);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_forgotten_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = forgotten_email.getText().toString();

                if (email.equals(""))
                {
                    Toast.makeText(ForgottenPassword.this, "Please entered email registered to your account.", Toast.LENGTH_LONG).show();
                }
                else
                    {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ForgottenPassword.this, "Email has been sent to your email account with link to reset password.", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgottenPassword.this, MainActivity.class));
                            }
                            else
                                {
                                Toast.makeText(ForgottenPassword.this, "Error! Please check email address entered and try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void backToLogin(View view)
    {
        startActivity(new Intent(ForgottenPassword.this, MainActivity.class));
    }
}