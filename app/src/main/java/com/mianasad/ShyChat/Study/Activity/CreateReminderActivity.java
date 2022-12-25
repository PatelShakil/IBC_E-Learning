package com.mianasad.ShyChat.Study.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mianasad.ShyChat.databinding.ActivityCreateReminderBinding;
import com.mianasad.ShyChat.notification.ApiUtils;
import com.mianasad.ShyChat.notification.Cons;
import com.mianasad.ShyChat.notification.NotificationData;
import com.mianasad.ShyChat.notification.PushNotification;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateReminderActivity extends AppCompatActivity {
    ActivityCreateReminderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Create Reminder");
        binding.remStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.remTitle.getText().toString().trim().isEmpty() && !binding.body.getText().toString().trim().isEmpty()){
                    PushNotification notification = new PushNotification(new NotificationData(binding.remTitle.getText().toString().trim(),binding.body.getText().toString().trim()), Cons.TOPIC_STUDENT);
                    sendNotification(notification);
                    binding.remTitle.setText("");
                    binding.body.setText("");
                }else
                    Toast.makeText(CreateReminderActivity.this, "Please Create Proper Notification", Toast.LENGTH_SHORT).show();
            }
        });
        binding.remAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.remTitle.getText().toString().trim().isEmpty() && !binding.body.getText().toString().trim().isEmpty()){
                    PushNotification notification = new PushNotification(new NotificationData(binding.remTitle.getText().toString().trim(),binding.body.getText().toString().trim()), Cons.TOPIC_ALL);
                    sendNotification(notification);
                    binding.remTitle.setText("");
                    binding.body.setText("");
                }else
                    Toast.makeText(CreateReminderActivity.this, "Please Create Proper Notification", Toast.LENGTH_SHORT).show();
            }
        });
        binding.remStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.remTitle.getText().toString().trim().isEmpty() && !binding.body.getText().toString().trim().isEmpty()){
                    PushNotification notification = new PushNotification(new NotificationData(binding.remTitle.getText().toString().trim(),binding.body.getText().toString().trim()), Cons.TOPIC_STAFF);
                    sendNotification(notification);
                    binding.remTitle.setText("");
                    binding.body.setText("");
                }else
                    Toast.makeText(CreateReminderActivity.this, "Please Create Proper Notification", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void sendNotification(PushNotification notification) {
        ApiUtils.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful())
                    Toast.makeText(CreateReminderActivity.this, "Reminder Published Successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CreateReminderActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(CreateReminderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}