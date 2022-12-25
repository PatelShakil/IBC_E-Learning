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
import com.mianasad.ShyChat.databinding.ActivitySem6Binding;

public class sem6 extends AppCompatActivity {

    ActivitySem6Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySem6Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("SEM-VI");
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        binding.subCg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem6.this, ViewNotes.class);
                intent.putExtra("subname","CG(601)");
                intent.putExtra("semname","SEM-VI");
                startActivity(intent);
            }
        });
        binding.subFcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem6.this, ViewNotes.class);
                intent.putExtra("subname","FCC(601)");
                intent.putExtra("semname","SEM-VI");
                startActivity(intent);
            }
        });
        binding.subEccs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem6.this,ViewNotes.class);
                intent.putExtra("subname","EC CS(602)");
                intent.putExtra("semname","SEM-VI");
                startActivity(intent);

            }
        });
        binding.subProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem6.this,ViewNotes.class);
                intent.putExtra("subname","PROJECT(603)");
                intent.putExtra("semname","SEM-VI");
                startActivity(intent);

            }
        });
        binding.subSeminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem6.this,ViewNotes.class);
                intent.putExtra("subname","SEMINAR(604)");
                intent.putExtra("semname","SEM-VI");
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
        startActivity(new Intent(sem6.this, LernerMain.class));
    }
}