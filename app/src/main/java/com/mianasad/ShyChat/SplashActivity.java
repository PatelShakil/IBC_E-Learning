package com.mianasad.ShyChat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.gesture.GestureLibraries;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mianasad.ShyChat.Activities.MainActivity;
import com.mianasad.ShyChat.Activities.PhoneNumberActivity;
import com.mianasad.ShyChat.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        String aboutbg = FirebaseRemoteConfig.getInstance().getString("aboutbg");
        Glide.with(this).load(aboutbg).into(binding.aboutimg);
        binding.mslearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://t.me/iqrabca");
            }
        });
        binding.ibc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("http://www.iqraitc.com/bca.html");
            }
        });
        binding.vnsgu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://www.vnsgu.ac.in/");
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://wa.me/919510634082");
            }
        });
        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://instagram.com/patelshakil95");
            }
        });
        binding.telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://t.me/patelshakil95");
            }
        });
        binding.version.setText("App version "+ getCurrentVersionCode());
    }

    public int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return packageInfo.versionCode;
    }
    public void gotoUrl(String s){
        Uri uri = Uri.parse(String.valueOf(s));
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}