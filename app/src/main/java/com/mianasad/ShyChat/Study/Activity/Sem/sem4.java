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
import com.mianasad.ShyChat.databinding.ActivitySem4Binding;
import com.mianasad.ShyChat.databinding.ActivitySem6Binding;

public class sem4 extends AppCompatActivity {

    ActivitySem4Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySem4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("SEM-IV");
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        binding.subIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem4.this, ViewNotes.class);
                intent.putExtra("subname","IS(401)");
                intent.putExtra("semname","SEM-IV");
                startActivity(intent);

            }
        });
        binding.subWd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem4.this, ViewNotes.class);
                intent.putExtra("subname","WD-II(405)");
                intent.putExtra("semname","SEM-IV");
                startActivity(intent);

            }
        });

        binding.subIot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem4.this,ViewNotes.class);
                intent.putExtra("subname","IOT(402)");
                intent.putExtra("semname","SEM-IV");
                startActivity(intent);

            }
        });
        binding.subJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem4.this,ViewNotes.class);
                intent.putExtra("subname","JAVA(403)");
                intent.putExtra("semname","SEM-IV");
                startActivity(intent);

            }
        });
        binding.subNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem4.this,ViewNotes.class);
                intent.putExtra("subname","NET(404)");
                intent.putExtra("semname","SEM-IV");
                startActivity(intent);

            }
        });
        binding.subMad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem4.this,ViewNotes.class);
                intent.putExtra("subname","MAD-II(405)");
                intent.putExtra("semname","SEM-IV");
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
        startActivity(new Intent(sem4.this, LernerMain.class));
    }
}