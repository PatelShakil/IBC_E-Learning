package com.mianasad.ShyChat.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.Models.PdfModel;
import com.mianasad.ShyChat.Study.Activity.ReadNotes;
import com.mianasad.ShyChat.Study.Activity.ViewNotes;
import com.mianasad.ShyChat.databinding.DeleteDialogBinding;
import com.mianasad.ShyChat.databinding.DeleteNotesBinding;
import com.mianasad.ShyChat.databinding.EditNotesBinding;
import com.mianasad.ShyChat.databinding.SampleNotesBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {
    Context context;
    ArrayList<PdfModel> pdfModels;
    public PdfAdapter(Context context, ArrayList<PdfModel> pdfModels) {
        this.context = context;
        this.pdfModels = pdfModels;
    }
    User1 user;
    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_notes,parent   ,false);
        return new PdfViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        PdfModel pdfModel = pdfModels.get(position);
        holder.binding.notesname.setText(pdfModel.getFilename());
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    user = snapshot1.getValue(User1.class);
                    if (pdfModel.getUid().equals(snapshot1.getKey())){
                        holder.binding.author.setText(user.getName());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String filepath = pdfModel.getFilepath();
        String filename = pdfModel.getFilename();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma dd MMM-yy ");
        long time = pdfModel.getTimestamp();
        holder.binding.timestamp.setText(dateFormat.format(time));
        if (FirebaseAuth.getInstance().getUid().equals(pdfModel.uid)) {
            holder.binding.notesbtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    LayoutInflater inflater = (LayoutInflater)
                            holder.binding.viewCount.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    PopupWindow popupWindow = new PopupWindow(inflater.inflate(R.layout.delete_notes,null,false), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
                    popupWindow.showAtLocation(holder.binding.notesbtn, Gravity.CENTER,0,0);
                    TextView tv = popupWindow.getContentView().findViewById(R.id.sub_name);
                    tv.setText("Note name: "+pdfModel.getFilename());
                    popupWindow.getContentView().findViewById(R.id.edit)
                            .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            View view1 = LayoutInflater.from(context).inflate(R.layout.edit_notes, null);
                            EditNotesBinding binding = EditNotesBinding.bind(view1);
                            AlertDialog dialog1 = new AlertDialog.Builder(context)
                                    .setTitle("Edit Notes Name")
                                    .setView(binding.getRoot())
                                    .create();
                            dialog1.show();
                            binding.editFilename.setText(pdfModel.filename);
                            binding.updateFilename.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child(pdfModel.semname)
                                            .child("Notes")
                                            .child(pdfModel.subname)
                                            .child(pdfModel.notesId)
                                            .child("filename")
                                            .setValue(binding.editFilename.getText().toString());
                                    Toast.makeText(context, "Note name edited Successfully", Toast.LENGTH_SHORT).show();
                                    dialog1.dismiss();
                                }
                            });
                        }
                    });
                    popupWindow.getContentView().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child(pdfModel.semname)
                                    .child("Notes")
                                    .child(pdfModel.subname)
                                    .child(pdfModel.notesId).setValue(null);
                            Toast.makeText(context, "Note Deleted Successfully", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                        }
                    });

                    popupWindow.getContentView().findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    return true;
                }
            });
        }
        holder.binding.notesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.binding.notesbtn.getContext(), ReadNotes.class);
                intent.putExtra("fileuri",filepath);
                intent.putExtra("filename",filename);
                FirebaseDatabase.getInstance().getReference()
                        .child(pdfModel.getSemname())
                        .child("Notes")
                        .child(pdfModel.getSubname())
                        .child(pdfModel.getNotesId())
                        .child("views")
                                .child(FirebaseAuth.getInstance().getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (!snapshot.exists()){
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child(pdfModel.getSemname())
                                                            .child("Notes")
                                                            .child(pdfModel.getSubname())
                                                            .child(pdfModel.getNotesId())
                                                            .child("views")
                                                            .child(FirebaseAuth.getInstance().getUid())
                                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child(pdfModel.getSemname())
                                                                            .child("Notes")
                                                                            .child(pdfModel.getSubname())
                                                                            .child(pdfModel.getNotesId())
                                                                            .child("viewCount")
                                                                            .setValue(pdfModel.getViewCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                holder.binding.notesbtn.getContext().startActivity(intent);
            }
        });

        String i = String.valueOf(pdfModel.getViewCount());
        holder.binding.viewCount.setText("Views: "+i);
    }

    @Override
    public int getItemCount() {
        return pdfModels.size();
    }

    public class PdfViewHolder extends RecyclerView.ViewHolder{

        SampleNotesBinding binding;
        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleNotesBinding.bind(itemView);
        }
    }
}
