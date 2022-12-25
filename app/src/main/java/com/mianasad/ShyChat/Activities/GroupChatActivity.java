package com.mianasad.ShyChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mianasad.ShyChat.Adapters.GroupMessagesAdapter;
import com.mianasad.ShyChat.Adapters.UsersAdapter;
import com.mianasad.ShyChat.Models.Message;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    com.mianasad.ShyChat.databinding.ActivityGroupChatBinding binding;
    GroupMessagesAdapter adapter;
    ArrayList<Message> messages;

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;

    String senderUid;
    public String type;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.mianasad.ShyChat.databinding.ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().show();
        if (type.equals("public")){
            getSupportActionBar().setTitle("Group Chat");
        }
        else {
            getSupportActionBar().setTitle("Staff Talk");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("groupchatbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.groupchatbg);

        senderUid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child(type);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

        messages = new ArrayList<>();
        adapter = new GroupMessagesAdapter(this, messages);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.attachment.setVisibility(View.INVISIBLE);
        if (reference!=null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messages.clear();
                    if (snapshot.exists()){
                        messages = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message msg = snapshot1.getValue(Message.class);
                            msg.setMessageId(snapshot1.getKey());
                            messages.add(msg);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(messages,(Comparator.<Message> comparingLong(obj1 -> obj1.getTimestamp()).thenComparingLong(obj2 -> obj2.getTimestamp())));
                        }
                        GroupMessagesAdapter adapter1 = new GroupMessagesAdapter(GroupChatActivity.this,messages);
                        adapter1.notifyDataSetChanged();
                        binding.recyclerView.smoothScrollToPosition(messages.size() - 1);
                        binding.recyclerView.setAdapter(adapter1);
                    }
                    else
                        binding.datanotfound.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (!binding.searchBar.equals(null)){
            binding.searchBar.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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



        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.messageBox.getText().toString().isEmpty()&&!binding.messageBox.getText().toString().equals(" ")&&!binding.messageBox.getText().toString().equals("  ")){


                String messageTxt = binding.messageBox.getText().toString();

                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime());
                binding.messageBox.setText("");

                database.getReference().child(type)
                        .push()
                        .setValue(message);
                }
            }
        });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 25) {
            if(data != null) {
                if(data.getData() != null) {
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    dialog.show();
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();

                                        String messageTxt = binding.messageBox.getText().toString();

                                        Date date = new Date();
                                        Message message = new Message(messageTxt, senderUid, date.getTime());
                                        message.setMessage("photo");
                                        message.setImageUrl(filePath);
                                        binding.messageBox.setText("");

                                        database.getReference().child(type)
                                                .push()
                                                .setValue(message);
                                        //Toast.makeText(ChatActivity.this, filePath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }
    private void search(String newText) {
        ArrayList<Message> myMessage = new ArrayList<>();
        for(Message object : messages){
            if (object.getMessage().toLowerCase().contains(newText.toLowerCase())){
                myMessage.add(object);
                binding.datanotfound.setVisibility(View.GONE);
            }

        }
        if (myMessage.isEmpty())
            binding.datanotfound.setVisibility(View.VISIBLE);
        GroupMessagesAdapter adapter = new GroupMessagesAdapter(this,myMessage);
        adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchBar = (SearchView) item.getActionView();
        searchBar.setQueryHint("Find message");

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
            if (reference!=null){
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        if (snapshot.exists()){
                            messages = new ArrayList<>();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                Message msg = snapshot1.getValue(Message.class);
                                msg.setMessageId(snapshot1.getKey());
                                messages.add(0,msg);
                            }
                            GroupMessagesAdapter adapter1 = new GroupMessagesAdapter(searchBar.getContext(),messages);
                            adapter1.notifyDataSetChanged();
                            binding.recyclerView.setAdapter(adapter1);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        return super.onCreateOptionsMenu(menu);
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