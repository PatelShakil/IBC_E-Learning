package com.mianasad.ShyChat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.Models.ViewsModel;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Study.Activity.Notice;
import com.mianasad.ShyChat.Study.Activity.NoticeMain;
import com.mianasad.ShyChat.Study.Activity.ReadNotes;
import com.mianasad.ShyChat.databinding.SampleNoticeBinding;

import java.io.PushbackInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {
    Context context;
    ArrayList<NoticeModel> noticeModels;

    public NoticeAdapter(Context context, ArrayList<NoticeModel> noticeModels) {
        this.context = context;
        this.noticeModels = noticeModels;
    }
    boolean v;
    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_notice,parent,false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        NoticeModel model = noticeModels.get(position);
        holder.binding.title.setText("SUBJECT : "+model.getTitle().toUpperCase());
        holder.binding.author.setText("~"+model.getAuthor());
        long time = model.getTimestamp();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma dd MMM-yy ");
            holder.binding.timestamp.setText(dateFormat.format(time));
        boolean pdf = model.isPdf();
        if (!pdf){
        holder.binding.noticeCard.setOnClickListener(new View.OnClickListener() {
            boolean flag = true;
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Notice.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("body",model.getBody());
                intent.putExtra("author",model.getAuthor());
                intent.putExtra("time",dateFormat.format(time));
                FirebaseDatabase.getInstance().getReference()
                        .child("Notice")
                        .child(model.getNoticeid())
                        .child("views")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                            if (snapshot1.getKey().equals(FirebaseAuth.getInstance().getUid())){
                                                flag = false;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                            Date date = new Date();
                                            ViewsModel viewsModel1 = new ViewsModel(FirebaseAuth.getInstance().getUid(), date.getTime());
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("Notice")
                                                    .child(model.getNoticeid())
                                                    .child("views")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .setValue(viewsModel1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("Notice")
                                                                    .child(model.getNoticeid())
                                                                    .child("viewCount")
                                                                    .setValue(model.getViewCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        String i = String.valueOf(model.getViewCount());
        holder.binding.viewCount.setText("Views: "+i);
        }
        if (pdf){
            holder.binding.noticeCard.setOnClickListener(new View.OnClickListener() {
                boolean flag = true;
                @Override
                public void onClick(View view) {
                    String filepath = model.getPdfuri();
                    Intent intent = new Intent(context, ReadNotes.class);
                    intent.putExtra("author",model.getAuthor());
                    intent.putExtra("time",dateFormat.format(time));
                    intent.putExtra("fileuri",filepath);
                    intent.putExtra("filename",model.getTitle());
                    FirebaseDatabase.getInstance().getReference()
                            .child("Notice")
                            .child(model.getNoticeid())
                            .child("views")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                        if (snapshot1.getKey().equals(FirebaseAuth.getInstance().getUid())){
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if (flag){
                                        Date date = new Date();
                                        ViewsModel viewsModel1 = new ViewsModel(FirebaseAuth.getInstance().getUid(), date.getTime());
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Notice")
                                                .child(model.getNoticeid())
                                                .child("views")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .setValue(viewsModel1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseDatabase.getInstance().getReference()
                                                                .child("Notice")
                                                                .child(model.getNoticeid())
                                                                .child("viewCount")
                                                                .setValue(model.getViewCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            String i = String.valueOf(model.getViewCount());
            holder.binding.viewCount.setText("Views: "+i);
        }
        LayoutInflater inflater = (LayoutInflater)
                holder.binding.viewCount.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.viewslayout,null,false), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User1 user1 = snapshot.getValue(User1.class);
                        if (user1.getType().equals("Staff"))
                            holder.binding.viewCount.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popupWindow.showAtLocation(holder.binding.viewCount, Gravity.CENTER,0,0);
                                    RecyclerView recyclerView = (RecyclerView) popupWindow.getContentView().findViewById(R.id.viewsrecyclerView);
                                    TextView tv = (TextView) popupWindow.getContentView().findViewById(R.id.notice_name);
                                    TextView viewc = (TextView) popupWindow.getContentView().findViewById(R.id.viewc);
                                    tv.setText(model.getTitle());
                                    viewc.setText("Seen by " + model.getViewCount() + " people");
                                    ArrayList<ViewsModel> viewsModels = new ArrayList<>();
                                    ViewsAdapter viewsAdapter = new ViewsAdapter(holder.binding.viewCount.getContext(),viewsModels);
                                    try {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notice").child(model.getNoticeid()).child("views");
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ViewsModel viewsModel = new ViewsModel();
                                                viewsModels.clear();
                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                    viewsModel = snapshot1.getValue(ViewsModel.class);
                                                    viewsModels.add(0,viewsModel);
                                                }
                                                viewsAdapter.notifyDataSetChanged();
                                                ViewsAdapter viewsAdapter = new ViewsAdapter(context, viewsModels);
                                                recyclerView.setAdapter(viewsAdapter);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    catch (Exception e){
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return noticeModels.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder {
        SampleNoticeBinding binding;
        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleNoticeBinding.bind(itemView);

        }
    }
}
