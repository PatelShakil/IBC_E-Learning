package com.mianasad.ShyChat.Study.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.databinding.ActivityNoticeBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notice extends AppCompatActivity {
    ActivityNoticeBinding binding;
    String title,body,author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        title = getIntent().getStringExtra("title");
        body = getIntent().getStringExtra("body");
        author = getIntent().getStringExtra("author");
        String time = getIntent().getStringExtra("time");
        binding.title.setText("Subject : "+title);
        binding.content.setText(body);
        binding.author.setText("~"+author);
        binding.timestamp.setText(time);


    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }
    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        if (currentId != null){
            Date date = new Date();
            long d = date.getTime();
            SimpleDateFormat sp  = new SimpleDateFormat("hh:mma dd/MMM");
            database.getReference().child("presence").child(currentId).setValue(sp.format(d));
        }
    }
}