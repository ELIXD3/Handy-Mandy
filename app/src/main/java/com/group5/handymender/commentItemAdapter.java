package com.group5.handymender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class commentItemAdapter extends RecyclerView.Adapter<commentItemAdapter.MyViewHolder> {
    static Context context;
    static ArrayList<commentItem> commentItems;

    public commentItemAdapter(Context context, ArrayList<commentItem> commentItems) {
        this.context = context;
        this.commentItems = commentItems;
    }

    @NonNull
    @Override
    public commentItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull commentItemAdapter.MyViewHolder holder, int position) {
        commentItem commentItem = commentItems.get(position);

        holder.commentDescription.setText(commentItem.commentDescription);
        holder.fullName.setText(commentItem.name+" "+commentItem.LName);
        holder.date.setText(commentItem.date);
        Picasso.get().load("https://www.strasys.uk/wp-content/uploads/2022/02/Depositphotos_484354208_S.jpg").into(holder.profilePicture);
    }

    @Override
    public int getItemCount() {
    return commentItems.size();
}

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ShapeableImageView profilePicture;
        TextView fullName, commentDescription, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commentDescription = itemView.findViewById(R.id.commentDescription);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            fullName = itemView.findViewById(R.id.fullName);
            date = itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
