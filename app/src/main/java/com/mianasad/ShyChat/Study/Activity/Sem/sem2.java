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
import com.mianasad.ShyChat.databinding.ActivitySem2Binding;
import com.mianasad.ShyChat.databinding.ActivitySem6Binding;

public class sem2 extends AppCompatActivity {

    ActivitySem2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySem2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("SEM-II");
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        binding.subIih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this, ViewNotes.class);
                intent.putExtra("subname","IIH(201)");
                intent.putExtra("semname","SEM-II");
                startActivity(intent);

            }
        });
        binding.subOsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this, ViewNotes.class);
                intent.putExtra("subname","OSB(201)");
                intent.putExtra("semname","SEM-II");
                startActivity(intent);

            }
        });
        binding.subCfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this, ViewNotes.class);
                intent.putExtra("subname","CFA(202)");
                intent.putExtra("semname","SEM-II");
                startActivity(intent);

            }
        });


        binding.subEtit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this,ViewNotes.class);
                intent.putExtra("subname","ET IT(202)");
                intent.putExtra("semname","SEM-II");
                startActivity(intent);

            }
        });
        binding.subOs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this,ViewNotes.class);
                intent.putExtra("subname","OS-I(203)");
                intent.putExtra("semname","SEM-II");
                startActivity(intent);

            }
        });
        binding.subPs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this,ViewNotes.class);
                intent.putExtra("subname","PS(204)");
                intent.putExtra("semname","SEM-II");
                startActivity(intent);

            }
        });
        binding.subRdbms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sem2.this,ViewNotes.class);
                intent.putExtra("subname","RDBMS(205)");
                intent.putExtra("semname","SEM-II");
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
        startActivity(new Intent(sem2.this, LernerMain.class));
    }
}