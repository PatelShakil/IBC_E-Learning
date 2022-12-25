package com.mianasad.ShyChat.Study.Activity;

import static com.mianasad.ShyChat.notification.Cons.TOPIC_ALL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.databinding.ActivityUploadNotesBinding;
import com.mianasad.ShyChat.databinding.ActivityUploadNoticeBinding;
import com.mianasad.ShyChat.notification.ApiUtils;
import com.mianasad.ShyChat.notification.NotificationData;
import com.mianasad.ShyChat.notification.PushNotification;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Noticeupload extends AppCompatActivity {
    ActivityUploadNoticeBinding binding;
    String subname,semname,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadNoticeBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Make a Notice");
        username = getIntent().getStringExtra("username");
        binding.publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.title.getText().toString().isEmpty() & !binding.body.getText().toString().isEmpty()) {
                    Date date = new Date();
                    boolean pdf = false;
                    NoticeModel model = new NoticeModel(binding.title.getText().toString(), binding.body.getText().toString(), username,0,date.getTime(),"N/A",pdf);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference().child("Notice");
                    databaseReference.child(databaseReference.push().getKey()).setValue(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                PushNotification notification = new PushNotification(new NotificationData(username,"Notice was "+binding.title.getText().toString()),TOPIC_ALL);
                                                sendNotification(notification);
                                            }
                                        }
                                    });
                    startActivity(new Intent(Noticeupload.this, NoticeMain.class));

                }
                else{
                    Toast.makeText(Noticeupload.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(PushNotification notification) {
        ApiUtils.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
//                if (response.isSuccessful())
//                    Toast.makeText(Noticeupload.this, "Notice Published Successfully", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(Noticeupload.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(Noticeupload.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Noticeupload.this,NoticeMain.class);
        startActivity(intent);
    }
}