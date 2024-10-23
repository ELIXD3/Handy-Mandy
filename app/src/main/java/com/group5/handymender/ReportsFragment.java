package com.group5.handymender;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReportsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<reportItem> reportItems;
    reportItemAdapter reportItemAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser user;
    FirebaseAuth auth;
    String email;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        progressDialog=new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            email = user.getEmail();
        }

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();
        reportItems = new ArrayList<reportItem>();
        reportItemAdapter = new reportItemAdapter(requireContext(), reportItems);

        recyclerView.setAdapter(reportItemAdapter);

        eventChangeListener();

        return view;
    }
    private void eventChangeListener() {
        db.collection("reports").whereEqualTo("email", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("FireStore Error", error.getMessage(), error);
                            Toast.makeText(requireContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    reportItem reportItem = documentChange.getDocument().toObject(reportItem.class);
                                    reportItem.setDocumentId(documentChange.getDocument().getId());
                                    reportItems.add(reportItem);
                                }
                            }
                            reportItemAdapter.notifyDataSetChanged();
                        }

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}