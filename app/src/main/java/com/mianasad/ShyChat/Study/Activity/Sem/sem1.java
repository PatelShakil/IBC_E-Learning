package com.mianasad.ShyChat.Study.Activity.Sem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mianasad.ShyChat.Activities.MainActivity;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Study.Activity.LernerMain;
import com.mianasad.ShyChat.Study.Activity.ViewNotes;
import com.mianasad.ShyChat.databinding.ActivitySem1Binding;
import com.mianasad.ShyChat.databinding.ActivitySem6Binding;

public class sem1 extends AppCompatActivity {
    ActivitySem1Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySem1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("SEM-I");
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        binding.subCs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem1.this,ViewNotes.class);
                intent.putExtra("subname","CS(101)");
                intent.putExtra("semname","SEM-I");
                startActivity(intent);

            }
        });
        binding.subMaths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem1.this,ViewNotes.class);
                intent.putExtra("subname","MATHS(102)");
                intent.putExtra("semname","SEM-I");
                startActivity(intent);

            }
        });
        binding.subIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem1.this,ViewNotes.class);
                intent.putExtra("subname","IC(103)");
                intent.putExtra("semname","SEM-I");
                startActivity(intent);

            }
        });
        binding.subDma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem1.this,ViewNotes.class);
                intent.putExtra("subname","DMA(105)");
                intent.putExtra("semname","SEM-I");
                startActivity(intent);

            }
        });
        binding.subCppm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem1.this,ViewNotes.class);
                intent.putExtra("subname","CPPM(104)");
                intent.putExtra("semname","SEM-I");
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
        Intent intent = new Intent(sem1.this, LernerMain.class);
        startActivity(intent);
    }
}