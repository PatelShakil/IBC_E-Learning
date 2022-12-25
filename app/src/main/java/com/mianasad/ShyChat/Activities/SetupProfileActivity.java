package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import com.google.firebase.auth.AuthResult;
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
import com.mianasad.ShyChat.Models.FacCheckModel;
import com.mianasad.ShyChat.Models.ProfileModel;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Study.Activity.LernerMain;
import com.mianasad.ShyChat.databinding.ActivitySetupProfileBinding;

import java.util.Date;
import java.util.HashMap;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;
    String str;
    boolean allow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account....");
        dialog.setCancelable(false);

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Glide.with(SetupProfileActivity.this)
                                .load(mFirebaseRemoteConfig.getString("login"))
                                .into(binding.signup);
                    }
                });

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        binding.ukey.setVisibility(View.GONE);
        binding.submit.setVisibility(View.GONE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (binding.spinner2.getSelectedItem().toString()) {
                    case "Student":
                        str = "Student";
                        binding.continueBtn.setVisibility(View.VISIBLE);
                        binding.ukey.setVisibility(View.GONE);
                        binding.submit.setVisibility(View.GONE);
                        break;
                    case "Staff":
                        str = "Staff";
                        binding.continueBtn.setVisibility(View.GONE);
                        binding.ukey.setVisibility(View.VISIBLE);
                        binding.submit.setVisibility(View.VISIBLE);
                        database.getReference().child("ukey")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        FacCheckModel model = snapshot.getValue(FacCheckModel.class);
                                        assert model != null;
                                        String ukey = model.getUkey();
                                        binding.submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (binding.ukey.getText().toString().equals(ukey)) {
                                                    binding.continueBtn.setVisibility(View.VISIBLE);
                                                    allow = true;
                                                } else {
                                                    Toast.makeText(SetupProfileActivity.this, "Provide a Valid Security key", Toast.LENGTH_LONG).show();
                                                    binding.continueBtn.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    str = "Student";
            }
        });
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupProfileActivity.this,PhoneNumberActivity.class);
                startActivity(intent);
            }
        });
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameBox.getText().toString();
                String phone = "N/A";
                String email = binding.etEmail.getText().toString();
                String pass = "";
                if (binding.etPassword.getText().toString().equals(binding.confirmPassword.getText().toString()) && !binding.etPassword.getText().toString().isEmpty()) {
                    pass = binding.etPassword.getText().toString();
                    if (!email.isEmpty()) {
                        if (name.isEmpty()) {
                            binding.nameBox.setError("Please type a name");
                        } else {
                            dialog.show();
                            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task){
                                    if (task.isSuccessful()){
                                    if (selectedImage != null) {
                                        StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
                                        reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String imageUrl = uri.toString();

                                                            String uid = auth.getUid();
                                                            String name = binding.nameBox.getText().toString();
                                                            String bio = binding.bioBox.getText().toString();

                                                            if (str.equals("Staff")) {
                                                                if (!allow) {
                                                                    Toast.makeText(SetupProfileActivity.this, "Please Enter Security key", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    User1 user = new User1(uid, name, email, imageUrl, phone, bio, str);

                                                                    database.getReference()
                                                                            .child("users")
                                                                            .child(uid)
                                                                            .setValue(user)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    dialog.dismiss();
                                                                                    Intent intent = new Intent(SetupProfileActivity.this, LernerMain.class);
                                                                                    startActivity(intent);
                                                                                    finish();
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                            if (str.equals("Student")) {
                                                                User1 user = new User1(uid, name, email, imageUrl, phone, bio, str);

                                                                database.getReference()
                                                                        .child("users")
                                                                        .child(uid)
                                                                        .setValue(user)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                dialog.dismiss();
                                                                                Intent intent = new Intent(SetupProfileActivity.this, LernerMain.class);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        });
                                                            }

                                                        }
                                                    });
                                                }
                                            else{
                                                Toast.makeText(SetupProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                            }
                                        });
                                    } else {
                                        String uid = auth.getUid();
                                        String bio = binding.bioBox.getText().toString();
                                        if (str.equals("Staff")) {
                                            if (!allow) {
                                                Toast.makeText(SetupProfileActivity.this, "Please Enter Security key", Toast.LENGTH_SHORT).show();
                                            } else {
                                                User1 user = new User1(uid, name, email, "No Image", phone, bio, str);

                                                database.getReference()
                                                        .child("users")
                                                        .child(uid)
                                                        .setValue(user)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                dialog.dismiss();
                                                                Intent intent = new Intent(SetupProfileActivity.this, LernerMain.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                        if (str.equals("Student")) {
                                            User1 user = new User1(uid, name, email, "No Image", phone, bio, str);

                                            database.getReference()
                                                    .child("users")
                                                    .child(uid)
                                                    .setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            dialog.dismiss();
                                                            Intent intent = new Intent(SetupProfileActivity.this, LernerMain.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }
                                }
                                    else{
                                        Toast.makeText(SetupProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });

                        }

                    }
                    else{
                        binding.etEmail.setError("Please enter email");
                    }
                }else {
                    binding.etPassword.setError("Please enter correct password");
                }
            }
        });
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
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("image", filePath);
                                    database.getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                }
                            });
                        }
                    }
                });


                binding.imageView.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
}