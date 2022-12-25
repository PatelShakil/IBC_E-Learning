package com.mianasad.ShyChat.Study.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianasad.ShyChat.Models.AdminModel;
import com.mianasad.ShyChat.Models.FacCheckModel;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.databinding.ActivityFacCheckBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FacCheck extends AppCompatActivity {

    String subname,u,semname,uname,key,storageuri;
    ActivityFacCheckBinding binding;
    FirebaseDatabase database;
    AdminModel model;
    String admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacCheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Upload Section");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] sem1 = getResources().getStringArray(R.array.sem1);
        String[] sem2 = getResources().getStringArray(R.array.sem2);
        String[] sem3 = getResources().getStringArray(R.array.sem3);
        String[] sem4 = getResources().getStringArray(R.array.sem4);
        String[] sem5 = getResources().getStringArray(R.array.sem5);
        String[] sem6 = getResources().getStringArray(R.array.sem6);
        binding.sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (binding.sem.getSelectedItem().toString()){
                    case "SEM-I":
                        semname = "SEM-I";
                        ArrayAdapter<String> adaptersem1 = new ArrayAdapter<String>(binding.subject.getContext(),R.layout.support_simple_spinner_dropdown_item,sem1);
                        binding.subject.setAdapter(adaptersem1);
                        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (binding.subject.getSelectedItem().toString()){
                                    case "CS(101)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "MATHS(102)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "IC(103)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "DMA(105)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "CPPM(104)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case "SEM-II":
                        semname = "SEM-II";
                        ArrayAdapter<String> adaptersem2 = new ArrayAdapter<String>(binding.subject.getContext(),R.layout.support_simple_spinner_dropdown_item,sem2);
                        binding.subject.setAdapter(adaptersem2);
                        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (binding.subject.getSelectedItem().toString()){
                                    case "OSB(201)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "IIH(201)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "CFA(202)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "ET IT(202)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "OS-I(203)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "PS(204)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "RDBMS(205)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case "SEM-III":
                        semname = "SEM-III";
                        ArrayAdapter<String> adaptersem3 = new ArrayAdapter<String>(binding.subject.getContext(),R.layout.support_simple_spinner_dropdown_item,sem3);
                        binding.subject.setAdapter(adaptersem3);
                        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (binding.subject.getSelectedItem().toString()){
                                    case "SM(301)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "SE(302)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "DHUP(303)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "OOP DS(304)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "WD-I(305)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "MAD-I(305)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case "SEM-IV":
                        semname = "SEM-IV";
                        ArrayAdapter<String> adaptersem4 = new ArrayAdapter<String>(binding.subject.getContext(),R.layout.support_simple_spinner_dropdown_item,sem4);
                        binding.subject.setAdapter(adaptersem4);
                        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (binding.subject.getSelectedItem().toString()){
                                    case "IS(401)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "IOT(402)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "JAVA(403)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "NET(404)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "WD-II(405)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "MAD-II(405)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;
                    case "SEM-V":
                        semname = "SEM-V";
                        ArrayAdapter<String> adaptersem5 = new ArrayAdapter<String>(binding.subject.getContext(),R.layout.support_simple_spinner_dropdown_item,sem5);
                        binding.subject.setAdapter(adaptersem5);
                        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (binding.subject.getSelectedItem().toString()){
                                    case "AWD(501)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "AMC(501)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "UNIX(502)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "NT(503)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "WFS(504)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "ASP(505)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                    case "SEM-VI":
                        semname = "SEM-VI";
                        ArrayAdapter<String> adaptersem6 = new ArrayAdapter<String>(binding.subject.getContext(),R.layout.support_simple_spinner_dropdown_item,sem6);
                        binding.subject.setAdapter(adaptersem6);
                        binding.subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                switch (binding.subject.getSelectedItem().toString()){
                                    case "CG(601)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "FCC(601)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "EC CS(602)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "PROJECT(603)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                    case "SEMINAR(604)":
                                        subname = binding.subject.getSelectedItem().toString();
                                        break;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        database = FirebaseDatabase.getInstance();
        database.getReference().child("ukey")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FacCheckModel model = snapshot.getValue(FacCheckModel.class);
                        assert model != null;
                        key = model.getUkey();
                        storageuri = model.getStorageuri();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(FacCheck.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        binding.uploadpdfnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = binding.etusername.getText().toString();
                if (u.equals(key)) {
                    Intent intent = new Intent(FacCheck.this, UploadNotes.class);
                    intent.putExtra("pdf", "true");
                    intent.putExtra("storageuri",storageuri);
                    intent.putExtra("username", uname);
                    intent.putExtra("subname", subname);
                    intent.putExtra("semname", semname);
                    startActivity(intent);
                } else {
                    Toast.makeText(FacCheck.this, "Enter Valid Security Key", Toast.LENGTH_SHORT).show();
                    binding.getRoot().setBackgroundColor(Color.parseColor("#F30707"));
                    binding.warning.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = binding.etusername.getText().toString();
                if (u.equals(key)){
                    gotoUploadNotes();
                }
                else {
                    Toast.makeText(FacCheck.this, "Enter Valid Security Key", Toast.LENGTH_SHORT).show();
                    binding.getRoot().setBackgroundColor(Color.parseColor("#F30707"));
                    binding.warning.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.uploadnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = binding.etusername.getText().toString();
                if (u.equals(key)){
                    Intent intent = new Intent(FacCheck.this, Noticeupload.class);
                    intent.putExtra("username",uname);
                    intent.putExtra("subname",subname);
                    intent.putExtra("semname",semname);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(FacCheck.this, "Enter Valid Security Key", Toast.LENGTH_SHORT).show();
                    binding.getRoot().setBackgroundColor(Color.parseColor("#F30707"));
                    binding.warning.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.createReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etusername.getText().toString().equals(key)){
                    startActivity(new Intent(FacCheck.this,CreateReminderActivity.class));
                }else{
                    Toast.makeText(FacCheck.this, "Enter Valid Security Key", Toast.LENGTH_SHORT).show();
                    binding.getRoot().setBackgroundColor(Color.parseColor("#F30707"));
                    binding.warning.setVisibility(View.VISIBLE);
                }
            }
        });
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User1 user1 = snapshot.getValue(User1.class);
                        uname = user1.getName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FacCheck.this,LernerMain.class));
    }

    public void gotoUploadNotes() {
        Intent intent = new Intent(FacCheck.this,UploadNotes.class);
        intent.putExtra("username",uname);
        intent.putExtra("subname",subname);
        intent.putExtra("semname",semname);
        intent.putExtra("pdf","false");
        intent.putExtra("storageuri",storageuri);
        startActivity(intent);
    }
}