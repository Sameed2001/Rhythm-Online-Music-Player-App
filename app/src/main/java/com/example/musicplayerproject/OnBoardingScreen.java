package com.example.musicplayerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingScreen extends AppCompatActivity {


    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private MaterialButton buttonOnboardingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screen);

        layoutOnboardingIndicators = findViewById(R.id.layoutOnBoardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnBoardingAction);

        setupOnboardingItems();
        ViewPager2 onboardingViewPager = findViewById(R.id.onBoardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setupCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);

                }else{
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });
    }


    private void setupOnboardingItems(){
        List<OnboardingItem> onBoardingItems = new ArrayList<>();

        OnboardingItem itemWelcomeToMusic = new OnboardingItem();
        itemWelcomeToMusic.setTitle("Welcome to Rhythm");
        itemWelcomeToMusic.setDescription("Listen to the songs you love and millions more.");
        itemWelcomeToMusic.setImage(R.drawable.onboarding_one);

        OnboardingItem itemListenOnline = new OnboardingItem();
        itemListenOnline.setTitle("Listen Music Online");
        itemListenOnline.setDescription("No need to download music offline, just connect to the internet and start listening music from our vast library");
        itemListenOnline.setImage(R.drawable.onboarding_two);

        OnboardingItem itemVoiceCommand = new OnboardingItem();
        itemVoiceCommand.setTitle("Use Voice Commands");
        itemVoiceCommand.setDescription("Both hands busy in doing another task? Just enable voice feature and tell the app what action you want it to do.");
        itemVoiceCommand.setImage(R.drawable.onboarding_three);

        onBoardingItems.add(itemWelcomeToMusic);
        onBoardingItems.add(itemListenOnline);
        onBoardingItems.add(itemVoiceCommand);

        onboardingAdapter = new OnboardingAdapter(onBoardingItems);
    }

    private void setupOnboardingIndicators(){

        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0,8,0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setupCurrentOnboardingIndicator(int index){

        int childCount = layoutOnboardingIndicators.getChildCount();
        for(int i = 0; i < childCount; i ++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if(i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));
            }
        }

        if(index == onboardingAdapter.getItemCount() - 1){
            buttonOnboardingAction.setText(R.string.get_started);
        }else{
            buttonOnboardingAction.setText(R.string.next);
        }

    }



}