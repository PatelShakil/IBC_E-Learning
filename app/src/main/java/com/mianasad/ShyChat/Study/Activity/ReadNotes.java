package com.mianasad.ShyChat.Study.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.StatusBarManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.EventLogTags;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.encoders.config.Configurator;
import com.mianasad.ShyChat.Activities.GroupChatActivity;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.databinding.ActivityReadNotesBinding;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class ReadNotes extends AppCompatActivity {

    ActivityReadNotesBinding binding;
    String fileurl,filename;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        binding.pdfview.getSettings().setJavaScriptEnabled(true);
        filename = getIntent().getStringExtra("filename");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();
        binding.groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(ReadNotes.this, GroupChatActivity.class));
                intent.putExtra("type","public");
                startActivity(intent);
            }
        });
        binding.filename.setText(filename);
        fileurl = getIntent().getStringExtra("fileuri");
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(ReadNotes.this.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Please Connect Internet").show();
        }else{
            pd = new ProgressDialog(this);
            pd.setTitle(R.string.app_name);
            pd.setMessage("Wait a moment...");
            pd.setCancelable(false);
            pd.setIcon(R.drawable.mslogo);
            pd.setButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    }
            );
            pd.show();
            new RetrivePDFfromUrl().execute(fileurl);
        }
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

    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(fileurl);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            binding.pdfView.fromStream(inputStream).load();
            binding.pdfView.setNightMode(false);
            pd.dismiss();
        }
    }
}