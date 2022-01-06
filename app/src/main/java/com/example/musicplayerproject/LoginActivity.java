package com.example.musicplayerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnSignup;
    TextView forgotpass;
    TextInputLayout username_var, password_var;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotpass = findViewById(R.id.forgotpass);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBarLoginActivity);

        username_var = findViewById(R.id.username_text_field_design);
        password_var = findViewById(R.id.username_password_field_design);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String username_ = username_var.getEditText().getText().toString();
                String password_ = password_var.getEditText().getText().toString();

                if (!username_.isEmpty()) {
                    username_var.setError(null);
                    username_var.setErrorEnabled(false);
                    if (!password_.isEmpty()) {
                        password_var.setError(null);
                        password_var.setErrorEnabled(false);

                        final String username_data = username_var.getEditText().getText().toString();
                        final String password_data = password_var.getEditText().getText().toString();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference reference = firebaseDatabase.getReference("datauser");

                        Query check_username = reference.orderByChild("username").equalTo(username_data);
                        check_username.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    username_var.setError(null);
                                    username_var.setErrorEnabled(false);
                                    String check_password = dataSnapshot.child(username_data).child("password").getValue(String.class);
                                    if (check_password.equals(password_data)) {
                                        password_var.setError(null);
                                        password_var.setErrorEnabled(false);
                                        //getting data from firebase
                                        String email = dataSnapshot.child(username_data).child("email").getValue(String.class);
                                        String username = dataSnapshot.child(username_data).child("username").getValue(String.class);
                                        String phoneNo = dataSnapshot.child(username_data).child("phoneNo").getValue(String.class);

                                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                                        intent.putExtra("username", username);
                                        intent.putExtra("phoneNo", phoneNo);
                                        intent.putExtra("email", email);

                                        progressBar.setVisibility(View.GONE);


                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();


                                        startActivity(intent);
                                        finish();

                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        password_var.setError("The password that you've entered is incorrect. ");
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    username_var.setError("Sorry! User does not exist.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        password_var.setError("Please enter the password.");
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    username_var.setError("Please enter the username.");
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}