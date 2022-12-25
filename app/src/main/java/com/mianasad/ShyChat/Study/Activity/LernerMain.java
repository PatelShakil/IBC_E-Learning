package com.mianasad.ShyChat.Study.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.mianasad.ShyChat.Activities.GroupChatActivity;
import com.mianasad.ShyChat.Activities.MainActivity;
import com.mianasad.ShyChat.Activities.PhoneNumberActivity;
import com.mianasad.ShyChat.Activities.Profile;
import com.mianasad.ShyChat.Activities.SetupProfileActivity;
import com.mianasad.ShyChat.Activities.UpdateProfileActivity;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.SplashActivity;
import com.mianasad.ShyChat.Study.Activity.Sem.sem1;
import com.mianasad.ShyChat.Study.Activity.Sem.sem2;
import com.mianasad.ShyChat.Study.Activity.Sem.sem3;
import com.mianasad.ShyChat.Study.Activity.Sem.sem4;
import com.mianasad.ShyChat.Study.Activity.Sem.sem5;
import com.mianasad.ShyChat.Study.Activity.Sem.sem6;
import com.mianasad.ShyChat.databinding.ActivityLernerMainBinding;
import com.mianasad.ShyChat.notification.Cons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class LernerMain extends AppCompatActivity {

    ActivityLernerMainBinding binding;
    User1 user = new User1();
    User user1;
    String bg;
    String VersionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLernerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Welcome to IBC E-Learning");
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
                                .setValue(s)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("LernerMain","success");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LernerMain.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
//        Objects.requireNonNull(getSupportActionBar())
//                .setBackgroundDrawable
//                        (new ColorDrawable(Color.parseColor("#51a3b8")));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding.fabchat.setVisibility(View.GONE);
        binding.upload.setVisibility(View.GONE);
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(LernerMain.this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.mslogo)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Please Connect Internet").show();
        }
        VersionCode = String.valueOf(getCurrentVersionCode());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic(Cons.TOPIC_ALL);
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User1 user1 = snapshot.getValue(User1.class);
                        if (user1.getType().equals("Staff")){
                            FirebaseMessaging.getInstance().subscribeToTopic(Cons.TOPIC_STAFF);
                            binding.fabchat.setVisibility(View.VISIBLE);
                            binding.upload.setVisibility(View.VISIBLE);
                        }
                        else{
                            FirebaseMessaging.getInstance().subscribeToTopic(Cons.TOPIC_STUDENT);
                            binding.fabchat.setVisibility(View.GONE);
                            binding.upload.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        bg = mFirebaseRemoteConfig.getString("lernerbg");
                        Glide.with(getApplicationContext())
                                .load(bg)
                                .into(binding.lernerbg);
                        if (!VersionCode.equals(mFirebaseRemoteConfig.getString("versioncode"))){
                            AlertDialog.Builder builder = new AlertDialog.Builder(LernerMain.this);
                            builder.setTitle("Alert");
                            builder.setIcon(R.drawable.mslogo);
                            builder.setMessage("Update application otherwise you don't able to use application");
                            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            });
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mFirebaseRemoteConfig.getString("UpdateLink"))));
                                }
                            });
                            builder.setCancelable(false);
                            builder.show();
                        }
                    }
                });
        binding.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) binding.notes.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                PopupWindow popupWindow = new PopupWindow(layoutInflater.inflate(R.layout.bca_layout,null,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAtLocation(binding.notes, Gravity.CENTER,0,0);
                binding.notes.setVisibility(View.GONE);
                binding.notice.setVisibility(View.GONE);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        binding.notes.setVisibility(View.VISIBLE);
                        binding.notice.setVisibility(View.VISIBLE);
                    }
                });
                popupWindow.getContentView().findViewById(R.id.sem1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LernerMain.this, sem1.class));

                    }
                });
                popupWindow.getContentView().findViewById(R.id.sem2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LernerMain.this, sem2.class));

                    }
                });
                popupWindow.getContentView().findViewById(R.id.sem3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LernerMain.this, sem3.class));

                    }
                });
                popupWindow.getContentView().findViewById(R.id.sem4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LernerMain.this, sem4.class));

                    }
                });
                popupWindow.getContentView().findViewById(R.id.sem5).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LernerMain.this, sem5.class));

                    }
                });
                popupWindow.getContentView().findViewById(R.id.sem6).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(LernerMain.this, sem6.class));

                    }
                });
            }
        });
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LernerMain.this,FacCheck.class));
            }
        });

        binding.notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LernerMain.this,NoticeMain.class));
            }
        });
        binding.fabchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LernerMain.this, MainActivity.class));
            }
        });
        binding.groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(LernerMain.this, GroupChatActivity.class));
                intent.putExtra("type","public");
                startActivity(intent);
            }
        });
    }

    public int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return packageInfo.versionCode;
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()){
                            String s = task.getResult().toString();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("token",s);
                            database.getReference().child("users")
                                    .child(FirebaseAuth.getInstance().getUid().toString())
                                    .child("token")
                                    .setValue(s)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("LernerMain","success");
                                        }
                                    });
                        }
                        else{
                            Toast.makeText(LernerMain.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_profile:
                startActivity(new Intent(LernerMain.this, UpdateProfileActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(LernerMain.this, SplashActivity.class));
                break;
            case R.id.login_newaccount:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(LernerMain.this, PhoneNumberActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lerner_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}