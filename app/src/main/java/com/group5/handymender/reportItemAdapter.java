package com.group5.handymender;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

    public class reportItemAdapter extends RecyclerView.Adapter<reportItemAdapter.MyViewHolder> {
        static Context context;
        static ArrayList<reportItem> reportItems;

        public reportItemAdapter(Context context, ArrayList<reportItem> reportItems) {
            this.context = context;
            this.reportItems = reportItems;
        }

        @NonNull
        @Override
        public reportItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_report,parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull reportItemAdapter.MyViewHolder holder, int position) {
            reportItem reportItem = reportItems.get(position);

            holder.fullName.setText(reportItem.name+" "+reportItem.LName);
            holder.location.setText("Location: " + reportItem.location);
            holder.category.setText("Category: " + reportItem.category);
            holder.description.setText("Description: " + reportItem.description);
            holder.date.setText(reportItem.date);
            Picasso.get().load("https://www.strasys.uk/wp-content/uploads/2022/02/Depositphotos_484354208_S.jpg").into(holder.profilePicture);

            switch (reportItem.status) {
                case "pending":
                    Picasso.get().load(R.drawable.pending).into(holder.statusPicture);
                    break;
                case "completed":
                    Picasso.get().load(R.drawable.completed).into(holder.statusPicture);
                    break;
                case "cancelled":
                    Picasso.get().load(R.drawable.cancelled).into(holder.statusPicture);
                    break;
            }
        }

        @Override
        public int getItemCount() {
        return reportItems.size();
    }

        public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ShapeableImageView profilePicture;
            ImageView  statusPicture;
            TextView fullName, location, category, description, date;
            Button commentBtn;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                profilePicture = itemView.findViewById(R.id.profilePicture);
                statusPicture = itemView.findViewById(R.id.status);
                fullName = itemView.findViewById(R.id.fullName);
                location = itemView.findViewById(R.id.location);
                category = itemView.findViewById(R.id.category);
                description = itemView.findViewById(R.id.description);
                date = itemView.findViewById(R.id.date);
                commentBtn = itemView.findViewById(R.id.commentBtn);
                commentBtn.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition(); // Get the position of the item clicked
                if (position != RecyclerView.NO_POSITION) {
                    // Retrieve the clicked item
                    reportItem clickedItem = reportItems.get(position);
                    // Retrieve the document ID of the residence building
                    String documentId = clickedItem.getDocumentId();

                    // Assuming you want to start an activity for the application form
                    // You can pass the document ID to the activity via Intent
                    Intent intent = new Intent(context, comment.class);
                    intent.putExtra("documentId", documentId);
                    context.startActivity(intent);
                }
            }
        }
    }
