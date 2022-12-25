package com.mianasad.ShyChat.Study.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.mianasad.ShyChat.Activities.MainActivity;
import com.mianasad.ShyChat.Activities.PhoneNumberActivity;
import com.mianasad.ShyChat.SplashActivity;
import com.mianasad.ShyChat.databinding.ActivitySplashMainBinding;

public class SplashMain extends AppCompatActivity {
    ActivitySplashMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);

                }
                catch (Exception e) {
                    e.printStackTrace();

                }
                finally {
                    Intent intent = new Intent(SplashMain.this, PhoneNumberActivity.class);
                    startActivity(intent);
                }
            }
        };thread.start();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}