package com.mianasad.ShyChat.Study.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.mianasad.ShyChat.Adapters.PdfAdapter;
import com.mianasad.ShyChat.Models.PdfModel;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Study.Activity.Sem.sem1;
import com.mianasad.ShyChat.Study.Activity.Sem.sem2;
import com.mianasad.ShyChat.Study.Activity.Sem.sem3;
import com.mianasad.ShyChat.Study.Activity.Sem.sem4;
import com.mianasad.ShyChat.Study.Activity.Sem.sem5;
import com.mianasad.ShyChat.Study.Activity.Sem.sem6;
import com.mianasad.ShyChat.databinding.ActivityViewNotesBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewNotes extends AppCompatActivity {

    ActivityViewNotesBinding binding;
    FirebaseDatabase database;
    ArrayList<PdfModel> pdfModels;
    PdfAdapter pdfAdapter;
    String subname;
    String semname;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        subname = getIntent().getStringExtra("subname");
        semname = getIntent().getStringExtra("semname");
        getSupportActionBar().setTitle(subname);
        pd = new ProgressDialog(binding.recyclerViewNotes.getContext());
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(ViewNotes.this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Please Connect Internet").show();
        }else {
            pd.setTitle(R.string.app_name);
            pd.setMessage("Loading... Notes");
            pd.setCancelable(false);
            pd.setIcon(R.drawable.mslogo);
            pd.show();
        }
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String bg = firebaseRemoteConfig.getString("semisterbg");
        Glide.with(getApplicationContext())
                .load(bg)
                .into(binding.backgroundImage);
        database = FirebaseDatabase.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        pdfModels = new ArrayList<>();
        pdfAdapter = new PdfAdapter(this,pdfModels);
        binding.recyclerViewNotes.setAdapter(pdfAdapter);
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
    public void Sem1checker(){
        switch (subname){
            case "CS(101)":
                NotesViewer();
                break;
            case "MATHS(102)":
                NotesViewer();
                break;
            case "IC(103)":
                NotesViewer();
                break;
            case "DMA(105)":
                NotesViewer();
                break;
            case "CPPM(104)":
                NotesViewer();
                break;
        }
    }
    public void Sem2checker(){
        switch (subname){
            case "IIH(201)":
                NotesViewer();
                break;
            case "OSB(201)":
                NotesViewer();
                break;
            case "CFA(202)":
                NotesViewer();
                break;
            case "ET IT(202)":
                NotesViewer();
                break;
            case "OS-I(203)":
                NotesViewer();
                break;
            case "PS(204)":
                NotesViewer();
                break;
            case "RDBMS(205)":
                NotesViewer();
                break;
        }
    }
    public void Sem3checker(){
        switch (subname){
            case "SM(301)":
                NotesViewer();
                break;
            case "SE(302)":
                NotesViewer();
                break;
            case "DHUP(303)":
                NotesViewer();
                break;
            case "OOP DS(304)":
                NotesViewer();
                break;
            case "MAD-I(305)":
                NotesViewer();
                break;
            case "WD-I(305)":
                NotesViewer();
                break;
        }
    }
    public void Sem4checker(){
        switch (subname){
            case "IS(401)":
                NotesViewer();
                break;
            case "IOT(402)":
                NotesViewer();
                break;
            case "JAVA(403)":
                NotesViewer();
                break;
            case "NET(404)":
                NotesViewer();
                break;
            case "MAD-II(405)":
                NotesViewer();
                break;
            case "WD-II(405)":
                NotesViewer();
                break;
        }
    }
    public void Sem5checker(){
        switch (subname){
            case "AMC(501)":
                NotesViewer();
                break;
            case "AWD(501)":
                NotesViewer();
                break;
            case "UNIX(502)":
                NotesViewer();
                break;
            case "NT(503)":
                NotesViewer();
                break;
            case "WFS(504)":
                NotesViewer();
                break;
            case "ASP(505)":
                NotesViewer();
                break;
        }
    }
    public void Sem6checker(){
        switch (subname){
            case "CG(601)":
                NotesViewer();
                break;
            case "FCC(601)":
                NotesViewer();
                break;
            case "EC CS(602)":
                NotesViewer();
                break;
            case "PROJECT(603)":
                NotesViewer();
                break;
            case "SEMINAR(604)":
                NotesViewer();
                break;
        }
    }
    public void NotesViewer() {
        database.getReference().child(semname).child("Notes").child(subname).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfModels.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        PdfModel model = snapshot1.getValue(PdfModel.class);
                        model.setNotesId(snapshot1.getKey());
                        model.setSemname(semname);
                        model.setSubname(subname);
                        pdfModels.add(0, model);
                        pd.dismiss();

                    }
                    pdfAdapter.notifyDataSetChanged();
                }
                else{
                    pd.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewNotes.this);
                    builder.setTitle("Alert");
                    builder.setIcon(R.drawable.mslogo);
                    builder.setMessage("No notes available...");
                    builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (semname){
            case "SEM-I":
                startActivity(new Intent(ViewNotes.this, sem1.class));
                break;
            case "SEM-II":
                startActivity(new Intent(ViewNotes.this, sem2.class));
                break;
            case "SEM-III":
                startActivity(new Intent(ViewNotes.this, sem3.class));
                break;
            case "SEM-IV":
                startActivity(new Intent(ViewNotes.this, sem4.class));
                break;
            case "SEM-V":
                startActivity(new Intent(ViewNotes.this, sem5.class));
                break;
            case "SEM-VI":
                startActivity(new Intent(ViewNotes.this, sem6.class));
                break;
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
            SimpleDateFormat sp  = new SimpleDateFormat("hh:mma dd/MMM");
            database.getReference().child("presence").child(currentId).setValue(sp.format(d));
        }
    }
}