package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mianasad.ShyChat.Adapters.NoticeAdapter;
import com.mianasad.ShyChat.Adapters.TopStatusAdapter;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.Models.Status;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.Models.UserStatus;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.Adapters.UsersAdapter;
import com.mianasad.ShyChat.SplashActivity;
import com.mianasad.ShyChat.Study.Activity.LernerMain;
import com.mianasad.ShyChat.Study.Activity.SplashMain;
import com.mianasad.ShyChat.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<User1> users;
    UsersAdapter usersAdapter;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog;
    FirebaseAuth auth;
    User1 user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.statusList.setVisibility(View.INVISIBLE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding.study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LernerMain.class);
                startActivity(intent);
            }
        });

        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                String backgroundImage = mFirebaseRemoteConfig.getString("backgroundImage");
                Glide.with(getApplicationContext())
                        .load(backgroundImage)
                        .into(binding.backgroundImage);
                /* Toolbar Color */
                String toolbarColor = mFirebaseRemoteConfig.getString("toolbarColors");
                String toolBarImage = mFirebaseRemoteConfig.getString("toolbarImage");
                boolean isToolBarImageEnabled = mFirebaseRemoteConfig.getBoolean("toolBarImageEnabled");



                if(isToolBarImageEnabled) {
                    Glide.with(getApplicationContext())
                            .load(toolBarImage)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                                    getSupportActionBar()
                                            .setBackgroundDrawable(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                                }
                            });
                } else {
                    Objects.requireNonNull(getSupportActionBar())
                            .setBackgroundDrawable
                                    (new ColorDrawable(Color.parseColor("#51a3b8")));
                }

            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);


        users = new ArrayList<>();
        userStatuses = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, users);
        statusAdapter = new TopStatusAdapter(this, userStatuses);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statusList.setLayoutManager(layoutManager);
        binding.statusList.setAdapter(statusAdapter);

        binding.recyclerView.setAdapter(usersAdapter);
        binding.recyclerView.showShimmerAdapter();
        binding.statusList.showShimmerAdapter();


//        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    User1 user1 = snapshot1.getValue(User1.class);
//                        users.add(0,user1);
//                }
//                binding.recyclerView.hideShimmerAdapter();
//                usersAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User1.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userStatuses.clear();
                    for(DataSnapshot storySnapshot : snapshot.getChildren()) {
                        UserStatus status = new UserStatus();
                        status.setName(storySnapshot.child("name").getValue(String.class));
                        status.setImageUrl(storySnapshot.child("imageUrl").getValue(String.class));
                        status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));

                        ArrayList<Status> statuses = new ArrayList<>();

                        for(DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()) {
                            Status sampleStatus = statusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);
                        }

                        status.setStatuses(statuses);
                        userStatuses.add(0,status);
                    }
                    binding.statusList.hideShimmerAdapter();
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.status:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 75);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(data.getData() != null) {
                dialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");

                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getName());
                                    userStatus.setImageUrl(user.getImageUrl());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("imageUrl", userStatus.getImageUrl());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());

                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUpdated());

                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);

                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
            case R.id.group:
                Intent intent = new Intent(MainActivity.this,GroupChatActivity.class);
                intent.putExtra("type","public");
                startActivity(intent);
                break;
            case R.id.user_profile:
                startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                break;
            case R.id.login_newaccount:
                auth.signOut();
                startActivity(new Intent(MainActivity.this,PhoneNumberActivity.class));
                break;
            case R.id.stafftalk:
                Intent intent1 = new Intent(MainActivity.this,GroupChatActivity.class);
                intent1.putExtra("type","Stafftalk");
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void search(String newText) {
        ArrayList<User1> myUser = new ArrayList<>();
        for(User1 object : users){
            if (object.getName().toLowerCase().contains(newText.toLowerCase())){
                myUser.add(object);
                binding.datanotfound.setVisibility(View.GONE);
            }
        }
        if (myUser.isEmpty())
            binding.datanotfound.setVisibility(View.VISIBLE);
        UsersAdapter adapter = new UsersAdapter(this,myUser);
        adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchBar = (SearchView) item.getActionView();
        searchBar.setQueryHint("Search User ");
        if (reference!=null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    if (snapshot.exists()){
                        users = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            User1 user1 = snapshot1.getValue(User1.class);
                                if (!user1.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                                    user1.setUserId(snapshot1.getKey());
                                    try {
                                        user1.setToken(snapshot1.child("token").getValue().toString()+"");
                                    } catch (Exception e){                                    }
                                    users.add(0, user1);
                                }
                        }
                        UsersAdapter adapter1 = new UsersAdapter(searchBar.getContext(),users);
                        binding.recyclerView.hideShimmerAdapter();
                        usersAdapter.notifyDataSetChanged();
                        binding.recyclerView.setAdapter(adapter1);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (!searchBar.equals(null)){
            searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this,LernerMain.class));
    }
}