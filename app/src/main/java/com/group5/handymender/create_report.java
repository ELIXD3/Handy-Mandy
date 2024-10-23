package com.group5.handymender;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class create_report extends AppCompatActivity {

    Button submitBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    FirebaseUser user;
    ProgressBar progressBar;
    Spinner spinnerCategory;
    EditText editTextLocation, editTextDescription;
    String email,name, LName, date;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(create_report.this,splash.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        submitBtn = findViewById(R.id.submitBtn);
        spinnerCategory = findViewById(R.id.category);
        editTextLocation = findViewById(R.id.location);
        editTextDescription = findViewById(R.id.description);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        CollectionReference userInfo = db.collection("users");

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


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String category, location, description, status;
                int categoryNum;
                LocalDate unformattedDate;
                categoryNum = spinnerCategory.getSelectedItemPosition();
                location = String.valueOf(editTextLocation.getText());
                description = String.valueOf(editTextDescription.getText());
                category = "Other";

                if (categoryNum == 0){
                    category = "Road and Traffic Issues";
                }

                else if (categoryNum == 1){
                    category = "Public Infrastructure";
                }

                else if (categoryNum == 2){
                    category = "Health and Sanitation";
                }

                else if (categoryNum == 3){
                    category = "Housing and Urban Development";
                }

                else if (categoryNum == 4){
                    category = "Utilities and Energy";
                }

                else if (categoryNum == 5){
                    category = "Environmental Concerns";
                }

                else if (categoryNum == 6){
                    category = "Water and Sewage";
                }

                else if (categoryNum == 7){
                    category = "Public Safety";
                }

                else if (categoryNum == 8){
                    category = "Waste Management";
                }

                else if (categoryNum == 9){
                    category = "Other";
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    unformattedDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    date = unformattedDate.format(formatter);
                }

                status = "pending";

                if (TextUtils.isEmpty(email)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(create_report.this, "Email not found, try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(create_report.this, "Name not found, try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(LName)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(create_report.this, "Last name not found. try restarting the app", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(category)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(create_report.this, "Please choose a category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(location)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(create_report.this, "Enter the report location", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(description)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(create_report.this, "Please give a report description", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> report = new HashMap<>();
                report.put("name", name);
                report.put("LName", LName);
                report.put("email", email);
                report.put("category", category);
                report.put("location", location);
                report.put("description", description);
                report.put("date", date);
                report.put("status", status);

                db.collection("reports")
                        .add(report)
                        .addOnCompleteListener(documentReferenceTask -> {
                            if (documentReferenceTask.isSuccessful()) {
                                Toast.makeText(create_report.this, "Report successfully created.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(create_report.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(create_report.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}