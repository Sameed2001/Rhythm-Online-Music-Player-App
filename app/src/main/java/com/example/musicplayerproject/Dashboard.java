 package com.example.musicplayerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.view.Menu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


 public class Dashboard extends AppCompatActivity {
     Toolbar toolbar;
     ImageView profileImg;
     CardView cardAllSongs, cardSettings, cardProfile, cardLogout;
     TextView dashboardUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.myToolbar);
        profileImg = findViewById(R.id.profileImg);

        cardAllSongs = findViewById(R.id.cardAllSongs);
        cardSettings = findViewById(R.id.cardSettings);
        cardProfile = findViewById(R.id.cardProfile);
        cardLogout = findViewById(R.id.cardLogout);

        dashboardUsername = findViewById(R.id.dashboardUsername);

        Intent intent = getIntent();
        String user_username = intent.getStringExtra("username");
        String user_email = intent.getStringExtra("email");
        String user_phoneNo = intent.getStringExtra("phoneNo");

        dashboardUsername.setText(user_username);


        setSupportActionBar(toolbar);

        cardAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ShowAlbumsActivity.class );
                startActivity(intent);
            }
        });

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentProfile = new Intent(getApplicationContext(), EditProfile.class);

                intentProfile.putExtra("username", user_username);
                intentProfile.putExtra("email", user_email);
                intentProfile.putExtra("phoneNo",user_phoneNo);

                startActivity(intentProfile);

            }
        });

        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Dashboard.this,settings.class);
                startActivity(intent2);
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }

 }