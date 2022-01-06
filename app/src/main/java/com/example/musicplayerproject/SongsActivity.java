package com.example.musicplayerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.musicplayerproject.Adapter.SongsAdapter;
import com.example.musicplayerproject.Model.GetSongs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SongsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RelativeLayout relative1;
    MotionEvent motionEvent;
    Boolean checkin = false;
    List<GetSongs> mupload;
    SongsAdapter adapter;
    ImageButton btn1;
    Uri uri;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognizerIntent;
    private String keeper = "";
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private int currentIndex;


    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        recyclerView = findViewById(R.id.recyclerView_songs_id);
        progressBar = findViewById(R.id.progressBarSongsActivity);
        jcPlayerView = findViewById(R.id.jcplayer);
        relative1 = findViewById(R.id.relative1);
        recyclerView.setHasFixedSize(true);
        btn1 = findViewById(R.id.voiceCommandBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mupload = new ArrayList<>();
        recyclerView.setAdapter(adapter);

        btn1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.voice_button_background_ontouch));
                        speechRecognizer.startListening(speechRecognizerIntent);
                        keeper = "";
                        break;
                    case MotionEvent.ACTION_UP:
                        btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.voice_button_background));
                        speechRecognizer.stopListening();
                        break;
                }
                return false;
            }
        });

        checkVoiceCommandPermission();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(SongsActivity.this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matchesFound = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matchesFound != null) {

                    keeper = matchesFound.get(0);
                    if (keeper.equals("pause")) {
                        pauseSong();
                        Toast.makeText(SongsActivity.this, "Command: " + keeper, Toast.LENGTH_SHORT).show();
                    } else if (keeper.equals("play")) {
                        playSong();
                        Toast.makeText(SongsActivity.this, "Command: " + keeper, Toast.LENGTH_SHORT).show();
                    } else if (keeper.equals("next song")) {
                        playNextSong();
                        Toast.makeText(SongsActivity.this, "Command: " + keeper, Toast.LENGTH_SHORT).show();
                    } else if (keeper.equals("previous song")) {
                        playPreviousSong();
                        Toast.makeText(SongsActivity.this, "Command: " + keeper, Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(SongsActivity.this, "Result =" + keeper, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        adapter = new SongsAdapter(getApplicationContext(), mupload, new SongsAdapter.RecyclerItemClickListener() {
            @Override


            public void onClickListener(GetSongs songs, int position) {

                changeSelectedSong(position);

                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();


            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mupload.clear();
                for (DataSnapshot dss : snapshot.getChildren()) {
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongs.setmKey(dss.getKey());
                    currentIndex = 0;
                    Intent intent = getIntent();
                    final String s = intent.getStringExtra("songAlbum");
                    if (s.equals(getSongs.getAlbum_name())) {
                        mupload.add(getSongs);
                        checkin = true;
                        jcAudios.add(JcAudio.createFromURL(getSongs.getSongTitle(), getSongs.getSongLink()));
                    }
                }

                adapter.setSelectedPosition(0);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (checkin) {
                    jcPlayerView.initPlaylist(jcAudios, null);
                } else {
                    Toast.makeText(SongsActivity.this, "there are no songs.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void playPreviousSong() {


        if (jcPlayerView.isPaused()){
            jcPlayerView.previous();
        }
        else
        {
            jcPlayerView.previous();
        }
    }

    private void playSong() {

       if (jcPlayerView.isPaused()){
        jcPlayerView.continueAudio();
       }
    }

    private void pauseSong() {

        jcPlayerView.pause();
    }

    private void playNextSong() {
        jcPlayerView.next();

        if (jcPlayerView.isPaused()){
            jcPlayerView.next();
        }
        else
        {
            jcPlayerView.next();
        }

    }


    public void changeSelectedSong(int index) {
        adapter.notifyItemChanged(adapter.getSelectedPosition());
        currentIndex = index;
        adapter.setSelectedPosition(currentIndex);
        adapter.notifyItemChanged(currentIndex);
    }

    public void checkVoiceCommandPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(SongsActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }


        }

    }

}