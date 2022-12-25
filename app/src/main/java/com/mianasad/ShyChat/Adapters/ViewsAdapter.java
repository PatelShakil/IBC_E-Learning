package com.mianasad.ShyChat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mianasad.ShyChat.Models.NoticeModel;
import com.mianasad.ShyChat.Models.User1;
import com.mianasad.ShyChat.Models.ViewsModel;
import com.mianasad.ShyChat.R;
import com.mianasad.ShyChat.databinding.SampleViewsBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewsAdapter extends RecyclerView.Adapter<ViewsAdapter.ViewsHolder>{
    Context context;
    ArrayList<ViewsModel> viewsModels ;

    public ViewsAdapter(Context context, ArrayList<ViewsModel> viewsModels) {
        this.context = context;
        this.viewsModels = viewsModels;
    }
    User1 user1;
    String name;
    @NonNull
    @Override
    public ViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_views,parent,false);
        return new ViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewsHolder holder, int position) {
        ViewsModel model = viewsModels.get(position);
        try{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(model.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user1 = snapshot.getValue(User1.class);
                    holder.binding.viewsUsername.setText(user1.getName());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage() + model.getUid(), Toast.LENGTH_LONG).show();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma dd/MMM");
        long time = model.getTimestamp();
        holder.binding.viewtime.setText(dateFormat.format(time));
    }

    @Override
    public int getItemCount() {
        return viewsModels.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder {
        SampleViewsBinding binding;
        public ViewsHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleViewsBinding.bind(itemView);
        }
    }
}
