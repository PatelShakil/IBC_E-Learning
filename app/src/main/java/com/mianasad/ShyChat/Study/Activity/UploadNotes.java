package com.mianasad.ShyChat.Study.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.Models.PdfModel;
import com.mianasad.ShyChat.databinding.ActivityUploadNotesBinding;
import com.mianasad.ShyChat.notification.ApiUtils;
import com.mianasad.ShyChat.notification.Cons;
import com.mianasad.ShyChat.notification.NotificationData;
import com.mianasad.ShyChat.notification.PushNotification;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadNotes extends AppCompatActivity {
    ActivityUploadNotesBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri filepath;
    String u,subname,semname,pdf,storageuri;
    String facultyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        pdf = getIntent().getStringExtra("pdf");
        if (pdf.equals("false")){
        u = getIntent().getStringExtra("username");
        subname = getIntent().getStringExtra("subname");
        semname = getIntent().getStringExtra("semname");
        getSupportActionBar().setTitle(semname+" : "+subname);
        database = FirebaseDatabase.getInstance().getReference().getDatabase();
        storage = FirebaseStorage.getInstance().getReference().getStorage();

        binding.filelogo.setVisibility(View.INVISIBLE);
        binding.filecancel.setVisibility(View.INVISIBLE);
        database.getReference().child("users")
                        .child(FirebaseAuth.getInstance().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            facultyName = snapshot.child("name").getValue().toString();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
        binding.filecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.browse.setVisibility(View.VISIBLE);
                binding.filecancel.setVisibility(View.INVISIBLE);
                binding.filelogo.setVisibility(View.INVISIBLE);
            }
        });

        binding.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (semname){
                    case "SEM-I":
                        Sem1checker();
                        break;
                        case "SEM-II":
                        Sem2checker();
                        break;
                    case "SEM-III":
                        Sem3checker();
                        break;
                    case "SEM-IV":
                        Sem4checker();
                        break;
                    case "SEM-V":
                        Sem5checker();
                        break;
                    case "SEM-VI":
                        Sem6checker();
                        break;
                }
                }

        });
        binding.browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,101);
            }
        });
        }
        if (pdf.equals("true")){
            binding.etBookname.setHint("Enter Subject of Notice");
            u = getIntent().getStringExtra("username");
            binding.up.setText("Publish");
            subname = getIntent().getStringExtra("subname");
            semname = getIntent().getStringExtra("semname");
            getSupportActionBar().setTitle("PDF Notice");
            database = FirebaseDatabase.getInstance().getReference().getDatabase();
            storage = FirebaseStorage.getInstance().getReference().getStorage();

            binding.filelogo.setVisibility(View.INVISIBLE);
            binding.filecancel.setVisibility(View.INVISIBLE);
            binding.filecancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.browse.setVisibility(View.VISIBLE);
                    binding.filecancel.setVisibility(View.INVISIBLE);
                    binding.filelogo.setVisibility(View.INVISIBLE);
                }
            });
            binding.browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent,101);
                    binding.up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UploadingNotice();
                        }
                    });
                }
            });

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
            SimpleDateFormat sp  = new SimpleDateFormat("hh:mma dd:MM");
            database.getReference().child("presence").child(currentId).setValue(sp.format(d));
        }
    }
    public void Sem1checker(){
        switch (subname){
            case "CS(101)":
                UploadingNotes();
                break;
            case "MATHS(102)":
                UploadingNotes();
                break;
            case "IC(103)":
                UploadingNotes();
                break;
            case "DMA(105)":
                UploadingNotes();
                break;
            case "CPPM(104)":
                UploadingNotes();
                break;
        }
    }
    public void Sem2checker(){
        switch (subname){
            case "IIH(201)":
                UploadingNotes();
                break;
            case "OSB(201)":
                UploadingNotes();
                break;
            case "CFA(202)":
                UploadingNotes();
                break;
            case "ET IT(202)":
                UploadingNotes();
                break;
            case "OS-I(203)":
                UploadingNotes();
                break;
            case "PS(204)":
                UploadingNotes();
                break;
            case "RDBMS(205)":
                UploadingNotes();
                break;
        }
    }
    public void Sem3checker(){
        switch (subname){
            case "SM(301)":
                UploadingNotes();
                break;
            case "SE(302)":
                UploadingNotes();
                break;
            case "DHUP(303)":
                UploadingNotes();
                break;
            case "OOP DS(304)":
                UploadingNotes();
                break;
            case "MAD-I(305)":
                UploadingNotes();
                break;
            case "WD-I(305)":
                UploadingNotes();
                break;
        }
    }
    public void Sem4checker(){
        switch (subname){
            case "IS(401)":
                UploadingNotes();
                break;
            case "IOT(402)":
                UploadingNotes();
                break;
            case "JAVA(403)":
                UploadingNotes();
                break;
            case "NET(404)":
                UploadingNotes();
                break;
            case "MAD-II(405)":
                UploadingNotes();
                break;
            case "WD-II(405)":
                UploadingNotes();
                break;
        }
    }
    public void Sem5checker(){
        switch (subname){
            case "AWD(501)":
                UploadingNotes();
                break;
            case "AMC(501)":
                UploadingNotes();
                break;
            case "UNIX(502)":
                UploadingNotes();
                break;
            case "NT(503)":
                UploadingNotes();
                break;
            case "WFS(504)":
                UploadingNotes();
                break;
            case "ASP(505)":
                UploadingNotes();
                break;
        }
    }
    public void Sem6checker(){
        switch (subname){
            case "CG(601)":
                UploadingNotes();
                break;
            case "FCC(601)":
                UploadingNotes();
                break;
            case "EC CS(602)":
                UploadingNotes();
                break;
            case "PROJECT(603)":
                UploadingNotes();
                break;
            case "SEMINAR(604)":
                UploadingNotes();
                break;
    }}
    public void UploadingNotice() {
        if (!binding.etBookname.getText().toString().isEmpty()) {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("File is Uploading");
            pd.show();
            storageuri = getIntent().getStringExtra("storageuri");
            StorageReference reference = FirebaseStorage.getInstance(storageuri).getReference().child("notice/" + u + System.currentTimeMillis() + ".pdf");
            DatabaseReference databaseReference = database.getReference().child("Notice");
            reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    Toast.makeText(UploadNotes.this, "Notice Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    Date date = new Date();
                                    boolean pdf = true;
                                    NoticeModel model = new NoticeModel(binding.etBookname.getText().toString(),"N/A",u,0,date.getTime(),uri.toString(),pdf);
                                    databaseReference.child(databaseReference.push().getKey()).setValue(model)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            PushNotification notification = new PushNotification(new NotificationData(facultyName,"Notice was " + binding.etBookname.getText().toString()), Cons.TOPIC_ALL);
                                                            sendNotification(notification);
                                                        }
                                                    });
                                    binding.etBookname.setText("");
                                    Intent intent = new Intent(UploadNotes.this, NoticeMain.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Uploaded : " + (int) percent + "%");
                        }
                    });
        }
        else {
            Toast.makeText(this, "Please give filename", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(UploadNotes.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void UploadingNotes() {
        if (!binding.etBookname.getText().toString().isEmpty()) {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("File is Uploading");
            pd.setCancelable(false);
            pd.show();
            storageuri = getIntent().getStringExtra("storageuri");
            StorageReference reference = storage.getInstance(storageuri).getReference().child("notes/" + u +binding.etBookname.getText().toString() + System.currentTimeMillis() + ".pdf");
            DatabaseReference databaseReference = database.getReference().child(semname).child("Notes").child(subname);
            reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    Toast.makeText(UploadNotes.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    Date date = new Date();
                                    PdfModel model = new PdfModel(FirebaseAuth.getInstance().getUid(), binding.etBookname.getText().toString(), uri.toString(),date.getTime());
                                    databaseReference.child(databaseReference.push().getKey()).setValue(model)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            PushNotification notification = new PushNotification(new NotificationData(facultyName,"Uploaded notes in "+ subname+ " for "+semname+" students."),Cons.TOPIC_STUDENT);
                                                            sendNotification(notification);
                                                        }
                                                    });
                                    binding.etBookname.setText("");
                                    Intent intent = new Intent(UploadNotes.this,FacCheck.class);
                                    intent.putExtra("subname", subname);
                                    intent.putExtra("semname", semname);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            float percent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Uploaded : " + (int) percent + "%");
                        }
                    });
        }
        else {
            Toast.makeText(this, "Please give filename", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK)
        {
            filepath = data.getData();
            binding.browse.setVisibility(View.INVISIBLE);
            binding.filecancel.setVisibility(View.VISIBLE);
            binding.filelogo.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}