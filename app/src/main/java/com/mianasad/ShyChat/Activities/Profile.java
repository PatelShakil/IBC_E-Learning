package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mianasad.ShyChat.Adapters.UsersAdapter;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.databinding.ActivityProfileBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Profile extends AppCompatActivity {
    ActivityProfileBinding binding;
    public String type,utype;
    User1 user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = remoteConfig.getString("semisterbg");
        Glide.with(this).load(bg).into(binding.lernerbg);
        String uname = getIntent().getStringExtra("name");
        String img = getIntent().getStringExtra("image");
        String uid = getIntent().getStringExtra("uid");
        String phone = getIntent().getStringExtra("phone");
        String bio = getIntent().getStringExtra("bio");
        String token = getIntent().getStringExtra("token");
        String userid = getIntent().getStringExtra("userid");
        if (uname.contains(" ")){
            getSupportActionBar().setTitle(uname.substring(0,uname.indexOf(" "))+"'s"+" profile");
        }
        else{
            getSupportActionBar().setTitle(uname+"'s"+" profile");
        }

        type = getIntent().getStringExtra("type");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        database.getReference().child("users").child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User1.class);
                        utype = user.getType();
                            if (utype.equals("Staff")){
                                binding.replyPrivate.setVisibility(View.VISIBLE);
                            }
                            if (utype.equals("Student") && type==null){
                                binding.replyPrivate.setVisibility(View.INVISIBLE);
                            }
                            else if(utype.equals("Student") && type.equals("Student")){
                                binding.replyPrivate.setVisibility(View.INVISIBLE);
                            }
                            else if (utype.equals("Student")&&type.equals("Staff")){
                                binding.replyPrivate.setVisibility(View.VISIBLE);
                            }
                            if (utype == null){
                                binding.replyPrivate.setVisibility(View.INVISIBLE);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        if (type == null){
            binding.profileType.setText("Designation: Student");
            binding.replyPrivate.setVisibility(View.INVISIBLE);
        }
        else {
            binding.profileType.setText("Designation: " + type);
        }
        binding.profileUsername.setText("Name: "+uname);
        if (bio == null){
            binding.profileBio.setText("Bio: Hey Buddies...");
        }
        else {
            binding.profileBio.setText("Bio: "+bio);
        }
        Glide.with(Profile.this).load(img)
                .placeholder(R.drawable.avatar)
                .into(binding.profilePic);
        binding.replyPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, ChatActivity.class);
                intent.putExtra("name",uname);
                intent.putExtra("image", img);
                intent.putExtra("uid", uid);
                intent.putExtra("token", token);
                intent.putExtra("phone", phone);
                intent.putExtra("bio" , bio);
                intent.putExtra("type", type);
                intent.putExtra("userid", userid);
//                Toast.makeText(Profile.this, userid + "el", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}