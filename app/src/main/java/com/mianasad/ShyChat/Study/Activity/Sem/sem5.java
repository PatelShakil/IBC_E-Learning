package com.mianasad.ShyChat.Study.Activity.Sem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Study.Activity.LernerMain;
import com.mianasad.ShyChat.Study.Activity.ViewNotes;
import com.mianasad.ShyChat.databinding.ActivitySem5Binding;
import com.mianasad.ShyChat.databinding.ActivitySem6Binding;

public class sem5 extends AppCompatActivity {

    ActivitySem5Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySem5Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("SEM-V");
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        binding.subAmc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem5.this, ViewNotes.class);
                intent.putExtra("subname","AMC(501)");
                intent.putExtra("semname","SEM-V");
                startActivity(intent);

            }
        });
        binding.subAwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem5.this, ViewNotes.class);
                intent.putExtra("subname","AWD(501)");
                intent.putExtra("semname","SEM-V");
                startActivity(intent);

            }
        });
        binding.subUnix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem5.this,ViewNotes.class);
                intent.putExtra("subname","UNIX(502)");
                intent.putExtra("semname","SEM-V");
                startActivity(intent);

            }
        });
        binding.subNt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem5.this,ViewNotes.class);
                intent.putExtra("subname","NT(503)");
                intent.putExtra("semname","SEM-V");
                startActivity(intent);

            }
        });
        binding.subWfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem5.this,ViewNotes.class);
                intent.putExtra("subname","WFS(504)");
                intent.putExtra("semname","SEM-V");
                startActivity(intent);

            }
        });
        binding.subAsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem5.this,ViewNotes.class);
                intent.putExtra("subname","ASP(505)");
                intent.putExtra("semname","SEM-V");
                startActivity(intent);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(sem5.this, LernerMain.class));
    }
}