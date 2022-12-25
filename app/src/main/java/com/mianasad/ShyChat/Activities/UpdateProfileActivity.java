package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.solver.PriorityGoalRow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mianasad.ShyChat.Models.ProfileModel;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Study.Activity.LernerMain;
import com.mianasad.ShyChat.databinding.ActivitySetupProfileBinding;
import com.mianasad.ShyChat.databinding.ActivityUpdateProfileBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    boolean p_update = false;
    User1 user1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Glide.with(UpdateProfileActivity.this)
                                .load(mFirebaseRemoteConfig.getString("login"))
                                .into(binding.updateProfile);
                    }
                });
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("My Profile");
        binding.plusProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
                p_update = true;
            }
        });
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user1 = snapshot.getValue(User1.class);
                binding.nameBox.setText(user1.getName());
                binding.type.setText("Designation: "+user1.getType());
                binding.email.setText("Email: "+user1.getEmail());
                binding.bioBox.setText(user1.getBio());
                binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = binding.nameBox.getText().toString();

                        if(name.isEmpty()) {
                            binding.nameBox.setError("Please type a name");
                        }
                        dialog.show();
                        user1.setName(name);
                        user1.setBio(binding.bioBox.getText().toString());
                        dialog.show();
                        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                                .setValue(user1);
                        dialog.dismiss();
                        startActivity(new Intent(UpdateProfileActivity.this,LernerMain.class));

                    }
                });
                String pr = user1.getImageUrl();
                if (!isFinishing()){
                    Glide.with(UpdateProfileActivity.this).load(pr).placeholder(R.drawable.avatar).into(binding.profileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(data.getData() != null) {
                Uri uri = data.getData(); // filepath
                FirebaseStorage storage = FirebaseStorage.getInstance();
                long time = new Date().getTime();
                StorageReference reference = storage.getReference().child("Profiles").child(time+"");
                ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage("Updating Profile Image...");
                pd.setCancelable(false);
                pd.show();
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("imageUrl", filePath);
                                    database.getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    pd.dismiss();
                                                }
                                            });
                                }
                            });
                        }
                    }
                });

                Glide.with(this).load(uri).placeholder(R.drawable.avatar).into(binding.profileimage);
                selectedImage = data.getData();
            }
        }
    }
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