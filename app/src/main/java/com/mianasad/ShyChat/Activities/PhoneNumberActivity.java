package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mianasad.ShyChat.Activities.MainActivity;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Study.Activity.LernerMain;
import com.mianasad.ShyChat.databinding.ActivityPhoneNumberBinding;
import com.mianasad.ShyChat.databinding.ActivityPhoneNumberBinding;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.databinding.ActivityPhoneNumberBinding;
import com.mianasad.ShyChat.databinding.ActivityOTPBinding;

public class PhoneNumberActivity extends AppCompatActivity {
    ActivityPhoneNumberBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(PhoneNumberActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");


        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Glide.with(PhoneNumberActivity.this)
                                .load(mFirebaseRemoteConfig.getString("login"))
                                .into(binding.login);
                    }
                });
        //Google sign in

        binding.note.setVisibility(View.GONE);
        binding.forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etEmail.getText().toString().isEmpty()){
                    binding.etEmail.setError("Please Enter an Email");
                }
                else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(binding.etEmail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(PhoneNumberActivity.this, "Password reset link sent to  "+binding.etEmail.getText().toString(), Toast.LENGTH_LONG).show();
                                                binding.note.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                Toast.makeText(PhoneNumberActivity.this, "Please enter registered email address", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                }
            }
        });
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etEmail.getText().toString().isEmpty()){
                    binding.etEmail.setError("Please Enter an Email");
                }
                if (binding.etPassword.getText().toString().isEmpty()){
                    binding.etPassword.setError("Please Enter a Password");
                }else {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(PhoneNumberActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PhoneNumberActivity.this, LernerMain.class);
                                startActivity(intent);
                            } else {
                                String er = "There is no user record corresponding to this identifier. The user may have been deleted.";
                                if (task.getException().getMessage().equals(er)){
                                    Toast.makeText(PhoneNumberActivity.this, "Please Sign up first.", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(PhoneNumberActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
            }
            }
        });
        if (auth.getCurrentUser()!=null){
            Intent intent = new Intent(PhoneNumberActivity.this,LernerMain.class);
            startActivity(intent);
        }
        binding.tvClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneNumberActivity.this,SetupProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
