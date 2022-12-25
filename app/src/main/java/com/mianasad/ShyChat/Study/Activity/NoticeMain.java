package com.mianasad.ShyChat.Study.Activity;

import static com.mianasad.ShyChat.notification.Cons.TOPIC_ALL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mianasad.ShyChat.Activities.GroupChatActivity;
import com.mianasad.ShyChat.Adapters.NoticeAdapter;
import com.mianasad.ShyChat.Adapters.UsersAdapter;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.databinding.ActivityNoticeMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class NoticeMain extends AppCompatActivity {
    ActivityNoticeMainBinding binding;
    ArrayList<NoticeModel> noticeModels;
    NoticeAdapter noticeAdapter;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Notices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        noticeModels = new ArrayList<>();
        noticeAdapter = new NoticeAdapter(this, noticeModels);
        reference = FirebaseDatabase.getInstance().getReference().child("Notice");
        binding.groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(NoticeMain.this, GroupChatActivity.class));
                intent.putExtra("type","public");
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

    private void search(String newText) {
        ArrayList<NoticeModel> myNotice = new ArrayList<>();
        for (NoticeModel object : noticeModels){
            if (object.getTitle().toLowerCase().contains(newText.toLowerCase())){
                myNotice.add(object);
                binding.datanotfound.setVisibility(View.GONE);
            }

        }
        if (myNotice.isEmpty())
            binding.datanotfound.setVisibility(View.VISIBLE);
        NoticeAdapter adapter = new NoticeAdapter(this,myNotice);
        adapter.notifyDataSetChanged();
        binding.noticeRecyclerview.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchBar = (SearchView) item.getActionView();
        searchBar.setQueryHint("Search Notices");
        if (reference!=null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        noticeModels.clear();
                        noticeModels = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            NoticeModel notice = snapshot1.getValue(NoticeModel.class);
                            notice.setNoticeid(snapshot1.getKey());
                            noticeModels.add(0,notice);
                        }
                        NoticeAdapter noticeAdapter = new NoticeAdapter(NoticeMain.this,noticeModels);
                        noticeAdapter.notifyDataSetChanged();
                        binding.noticeRecyclerview.setAdapter(noticeAdapter);

                    }
                    else
                        Toast.makeText(NoticeMain.this, "No notice found", Toast.LENGTH_SHORT).show();

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
        if (searchBar.equals(null)){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        noticeModels = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            noticeModels.add(snapshot1.getValue(NoticeModel.class));
                        }
                        noticeAdapter.notifyDataSetChanged();
                        binding.noticeRecyclerview.setAdapter(noticeAdapter);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }
}