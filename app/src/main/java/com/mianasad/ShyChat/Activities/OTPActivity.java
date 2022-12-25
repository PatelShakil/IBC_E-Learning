package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Models.User2;
import com.mianasad.ShyChat.databinding.ActivityOTPBinding;

import java.util.HashMap;

public class OTPActivity extends AppCompatActivity {
    ActivityOTPBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(OTPActivity.this);
        progressDialog.setTitle("Creating Account...");
        progressDialog.setMessage("We're creating your account");


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etEmail.getText().toString().isEmpty()){
                    Toast.makeText(OTPActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                }
                if (binding.etPassword.getText().toString().isEmpty()){
                    Toast.makeText(OTPActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword
                            (binding.etEmail.getText().toString(), binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                User2 user = new User2(binding.etEmail.getText().toString(), binding.etPassword.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                user.setUid(id);
                                database.getReference().child("users").child(id).setValue(user);
                                FirebaseMessaging.getInstance()
                                        .getToken()
                                        .addOnSuccessListener(new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                HashMap<String,Object> map = new HashMap<>();
                                                map.put("token",s);
                                                database.getReference().child("users")
                                                        .child(FirebaseAuth.getInstance().getUid().toString())
                                                        .child("token")
                                                        .setValue(s);
                                            }
                                        });

                                Toast.makeText(OTPActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OTPActivity.this, SetupProfileActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPActivity.this,PhoneNumberActivity.class);
                startActivity(intent);
            }
        });




    }
}