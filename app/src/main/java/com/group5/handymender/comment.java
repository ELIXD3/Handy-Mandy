package com.group5.handymender;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class comment extends AppCompatActivity {

    ShapeableImageView reportProfilePicture;
    ImageView statusPicture;
    TextView reportFullNameTv, reportLocationTv, reportCategoryTv, reportDescriptionTv, reportDateTv;
    String reportLocation, reportCategory, reportDate, reportDescription, reportStatus, reportName, reportLName;

    RecyclerView recyclerView;
    ArrayList<commentItem> commentItems;
    commentItemAdapter commentItemAdapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    String reportId, commentDescription, date, email, name, LName;
    TextInputEditText commentEditText;
    MaterialButton submitCommentBtn;

    public comment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); // Correctly close this block.

        // Initialize RecyclerView and other components
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(comment.this));
        reportId = getIntent().getStringExtra("documentId");
        commentEditText = findViewById(R.id.comment);
        submitCommentBtn = findViewById(R.id.submitCommentBtn);

        reportFullNameTv = findViewById(R.id.fullName);
        reportProfilePicture = findViewById(R.id.profilePicture);
        reportLocationTv = findViewById(R.id.location);
        reportCategoryTv = findViewById(R.id.category);
        reportDescriptionTv = findViewById(R.id.description);
        reportDateTv = findViewById(R.id.date);
        statusPicture = findViewById(R.id.status);


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        CollectionReference userInfo = db.collection("users");
        CollectionReference reportInfo = db.collection("reports");

        if (user != null) {
            email = user.getEmail();

            userInfo.whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            name = documentSnapshot.getString("name");
                            LName = documentSnapshot.getString("LName");
                        }
                    });
        }

        db.collection("reports").document(reportId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Extract the necessary fields from the document
                        reportLocation = documentSnapshot.getString("location");
                        reportCategory = documentSnapshot.getString("category");
                        reportDescription = documentSnapshot.getString("description");
                        reportDate = documentSnapshot.getString("date");
                        reportStatus = documentSnapshot.getString("status");
                        reportName = documentSnapshot.getString("name");
                        reportLName = documentSnapshot.getString("LName");

                        Picasso.get().load("https://www.strasys.uk/wp-content/uploads/2022/02/Depositphotos_484354208_S.jpg").into(reportProfilePicture);
                        reportFullNameTv.setText(reportName + " " + reportLName);
                        reportLocationTv.setText("Location: " + reportLocation);
                        reportCategoryTv.setText("Category: " + reportCategory);
                        reportDescriptionTv.setText("Description: " + reportDescription);
                        reportDateTv.setText(reportDate);

                        // Set status icon based on reportStatus
                        switch (reportStatus) {
                            case "pending":
                                Picasso.get().load(R.drawable.pending).into(statusPicture);
                                break;
                            case "completed":
                                Picasso.get().load(R.drawable.completed).into(statusPicture);
                                break;
                            case "cancelled":
                                Picasso.get().load(R.drawable.cancelled).into(statusPicture);
                                break;
                        }
                    } else {
                        // Handle case where document with reportId doesn't exist
                        Toast.makeText(this, "Report not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors here
                    Log.e("Firestore Error", e.getMessage(), e);
                    Toast.makeText(this, "Error retrieving report", Toast.LENGTH_SHORT).show();
                });




        commentItems = new ArrayList<>();
        commentItemAdapter = new commentItemAdapter(comment.this, commentItems);

        recyclerView.setAdapter(commentItemAdapter);

        // Call the event change listener
        eventChangeListener();

        submitCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentDescription, reportId;
                commentDescription = String.valueOf(commentEditText.getText());
                reportId = getIntent().getStringExtra("documentId");
                LocalDate unformattedDate;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    unformattedDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    date = unformattedDate.format(formatter);
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(comment.this, "Email not found, try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(comment.this, "Name not found, try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(LName)){
                    Toast.makeText(comment.this, "Last name not found. try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(commentDescription)){
                    Toast.makeText(comment.this, "Please enter your comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(date)){
                    Toast.makeText(comment.this, "Date not found. try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(reportId)){
                    Toast.makeText(comment.this, "Report not found. try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> comment = new HashMap<>();
                comment.put("name", name);
                comment.put("LName", LName);
                comment.put("email", email);
                comment.put("commentDescription", commentDescription);
                comment.put("date", date);
                comment.put("reportId", reportId);

                db.collection("comments")
                        .add(comment)
                        .addOnCompleteListener(documentReferenceTask -> {
                            if (documentReferenceTask.isSuccessful()) {
                                Toast.makeText(comment.this, "Comment successfully created.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(comment.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // Move the eventChangeListener outside onCreate
    private void eventChangeListener() {
        db.collection("comments").whereEqualTo("reportId", reportId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("FireStore Error", error.getMessage(), error);
                            Toast.makeText(comment.this, "Error getting data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    commentItem commentItem = documentChange.getDocument().toObject(commentItem.class);
                                    commentItem.setDocumentId(documentChange.getDocument().getId());
                                    commentItems.add(commentItem);
                                }
                            }
                            commentItemAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}