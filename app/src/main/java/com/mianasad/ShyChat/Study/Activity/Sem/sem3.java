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
import com.mianasad.ShyChat.databinding.ActivitySem3Binding;
import com.mianasad.ShyChat.databinding.ActivitySem6Binding;

public class sem3 extends AppCompatActivity {

    ActivitySem3Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySem3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("SEM-III");
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        binding.subSm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem3.this, ViewNotes.class);
                intent.putExtra("subname","SM(301)");
                intent.putExtra("semname","SEM-III");
                startActivity(intent);

            }
        });
        binding.subWd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem3.this, ViewNotes.class);
                intent.putExtra("subname","WD-I(305)");
                intent.putExtra("semname","SEM-III");
                startActivity(intent);

            }
        });
        binding.subSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem3.this,ViewNotes.class);
                intent.putExtra("subname","SE(302)");
                intent.putExtra("semname","SEM-III");
                startActivity(intent);

            }
        });
        binding.subDhup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem3.this,ViewNotes.class);
                intent.putExtra("subname","DHUP(303)");
                intent.putExtra("semname","SEM-III");
                startActivity(intent);

            }
        });
        binding.subOopds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem3.this,ViewNotes.class);
                intent.putExtra("subname","OOP DS(304)");
                intent.putExtra("semname","SEM-III");
                startActivity(intent);

            }
        });
        binding.subMadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem3.this,ViewNotes.class);
                intent.putExtra("subname","MAD-I(305)");
                intent.putExtra("semname","SEM-III");
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
        startActivity(new Intent(sem3.this, LernerMain.class));
    }
}