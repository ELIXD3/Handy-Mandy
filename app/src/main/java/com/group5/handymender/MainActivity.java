package com.group5.handymender;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group5.handymender.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TextView toolbarTitleTv;
    FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbarTitleTv = findViewById(R.id.toolbarTitleTv);
        addBtn = findViewById(R.id.addBtn);

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId()==R.id.home){
                replaceFragment(new HomeFragment());
                toolbarTitleTv.setText("Home");
            } else if(item.getItemId()==R.id.reports){
                replaceFragment(new ReportsFragment());
                toolbarTitleTv.setText("Your Reports");
            } else if(item.getItemId()==R.id.notifications){
                replaceFragment(new NotificationsFragment());
                toolbarTitleTv.setText("Notifications");
            } else if (item.getItemId()==R.id.account) {
                replaceFragment(new AccountFragment());
                toolbarTitleTv.setText("Account");
            }
            return true;
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, create_report.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}