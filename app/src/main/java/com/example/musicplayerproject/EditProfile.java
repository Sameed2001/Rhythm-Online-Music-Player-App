package com.example.musicplayerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    TextView usernameTitle;
    TextInputLayout profileUserName, profileUserEmail, profileUserPhoneNumber;
    Button btnEditDetails;
    String user_username, user_email, user_phoneNo;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        usernameTitle = findViewById(R.id.username_title);
        profileUserName = findViewById(R.id.profileUserName);
        profileUserEmail = findViewById(R.id.profileUserEmail);
        profileUserPhoneNumber = findViewById(R.id.profileUserPhoneNumber);
        btnEditDetails = findViewById(R.id.btnEditDetails);


        //Database Reference
        reference = FirebaseDatabase.getInstance().getReference("datauser");

        // show all data

        showUserData();

        // edit data

        btnEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailChange() || phoneNoChange()){
                    Toast.makeText(EditProfile.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(EditProfile.this, "No changes made.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean emailChange() {
        if(!user_email.equals(profileUserEmail.getEditText().getText().toString())){
            reference.child(user_username).child("email").setValue(profileUserEmail.getEditText().getText().toString());
            user_email = profileUserEmail.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean phoneNoChange() {
        if(!user_phoneNo.equals(profileUserPhoneNumber.getEditText().getText().toString())){
            reference.child(user_username).child("phoneNo").setValue(profileUserPhoneNumber.getEditText().getText().toString());
            user_phoneNo = profileUserPhoneNumber.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private void showUserData() {
        Intent intent = getIntent();
        user_username = intent.getStringExtra("username");
        user_email = intent.getStringExtra("email");
        user_phoneNo = intent.getStringExtra("phoneNo");

        //set Data

        usernameTitle.setText(user_username);
        profileUserName.getEditText().setText(user_username);
        profileUserEmail.getEditText().setText(user_email);
        profileUserPhoneNumber.getEditText().setText(user_phoneNo);
        //sample intent

    }

}