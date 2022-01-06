package com.example.musicplayerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    TextInputLayout username_signup, phone_signup, email_signup, password_signup, confirm_password_signup;
    Button register;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;

    TextView textLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        username_signup = findViewById(R.id.username_signup);
        phone_signup = findViewById(R.id.phone_signup);
        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        confirm_password_signup = findViewById(R.id.confirmPassword_signup);
        textLogin = findViewById(R.id.textLogin);
        register = findViewById(R.id.register);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_signup = username_signup.getEditText().getText().toString();
                String user_phone = phone_signup.getEditText().getText().toString();
                String user_email = email_signup.getEditText().getText().toString();
                String user_password = password_signup.getEditText().getText().toString();
                String user_confirm_password = confirm_password_signup.getEditText().getText().toString();

                if (!user_signup.isEmpty()) {
                    username_signup.setError(null);
                    username_signup.setErrorEnabled(false);
                    if (!user_phone.isEmpty()) {
                        phone_signup.setError(null);
                        phone_signup.setErrorEnabled(false);
                        if (!user_email.isEmpty()) {
                            email_signup.setError(null);
                            email_signup.setErrorEnabled(false);
                            if (!user_password.isEmpty()) {
                                password_signup.setError(null);
                                password_signup.setErrorEnabled(false);
                                if (!user_confirm_password.isEmpty()) {
                                    confirm_password_signup.setError(null);
                                    confirm_password_signup.setErrorEnabled(false);
                                    if (user_email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                                        if (user_password.equals(user_confirm_password)) {


                                            createUser();


                                        } else {

                                            confirm_password_signup.setError("Passwords do not match.");

                                        }

                                    } else {
                                        email_signup.setError("Please enter valid email address");
                                    }

                                } else {
                                    confirm_password_signup.setError("Please enter your password again.");
                                }

                            } else {
                                password_signup.setError("Please enter your password.");
                            }

                        } else {
                            email_signup.setError("Please enter your email.");
                        }

                    } else {
                        phone_signup.setError("Pleaser enter our phone number.");
                    }

                } else {
                    username_signup.setError("Please enter your username.");
                }
            }
        });
    }


    private void createUser() {
        String user_email_a = email_signup.getEditText().getText().toString();
        String user_password_a = password_signup.getEditText().getText().toString();
        auth.createUserWithEmailAndPassword(user_email_a, user_password_a).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("datauser");
                    String user_signup_s = username_signup.getEditText().getText().toString();
                    String user_phone_s = phone_signup.getEditText().getText().toString();
                    String user_email_s = email_signup.getEditText().getText().toString();
                    String user_password_s = password_signup.getEditText().getText().toString();


                    StoringUserData storingUserData = new StoringUserData(user_signup_s, user_phone_s, user_email_s, user_password_s);
                    reference.child(user_signup_s).setValue(storingUserData);

                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}