package com.mianasad.ShyChat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianasad.ShyChat.Activities.ChatActivity;
import com.mianasad.ShyChat.Activities.Profile;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.Models.User2;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Models.User;
import com.mianasad.ShyChat.databinding.RowConversationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    Context context;
    ArrayList<User1> users;

    public UsersAdapter(Context context, ArrayList<User1> users) {
        this.context = context;
        this.users = users;
    }
    public String type;

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User1 user = users.get(position);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        String senderId = FirebaseAuth.getInstance().getUid();

        String senderRoom = senderId + user.getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            holder.binding.msgTime.setText(dateFormat.format(new Date(time)));
                            holder.binding.lastMsg.setText(lastMsg);
                        } else {
                            holder.binding.lastMsg.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            holder.binding.username.setText(user.getName());
        Glide.with(context).load(user.getImageUrl())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profile);



        holder.binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("image", user.getImageUrl());
                intent.putExtra("uid", user.getUid());
                intent.putExtra("phone", user.getPhone());
                intent.putExtra("bio",user.getBio());
                intent.putExtra("token", user.getToken());
                intent.putExtra("type", user.getType());
                intent.putExtra("userid", user.getUserId());
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(user.getUserId())
                        .child("views")
                        .child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()){
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("users")
                                            .child(user.getUserId())
                                            .child("views")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("users")
                                                            .child(user.getUserId())
                                                            .child("viewCount")
                                                            .setValue(user.getViewCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("image", user.getImageUrl());
                intent.putExtra("uid", user.getUid());
                intent.putExtra("phone", user.getPhone());
                intent.putExtra("bio", user.getBio());
                intent.putExtra("type", user.getType());
                intent.putExtra("token", user.getToken());
                intent.putExtra("userid",user.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        RowConversationBinding binding;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }

}
