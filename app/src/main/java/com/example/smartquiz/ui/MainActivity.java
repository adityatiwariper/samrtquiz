package com.example.smartquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartquiz.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    RelativeLayout btnStart, btnHistory, btnPerformance;
    TextView txthistory,txtperfomance;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();
        setupClickListeners();
        setupBottomNavigation();


    }

    private void initializeViews() {
        btnStart = findViewById(R.id.btnStart);
        btnHistory = findViewById(R.id.btnHistory);
        btnPerformance = findViewById(R.id.btnPerformance);
        txtperfomance = findViewById(R.id.txtperfomance);
        txthistory = findViewById(R.id.txthistory);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set home as selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    private void setupClickListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
                startActivity(intent);
            }
        });

        txthistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnHistory.callOnClick();
            }
        });

        txtperfomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPerformance.callOnClick();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizHistoryActivity.class);
                startActivity(intent);
            }
        });

        btnPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PerformanceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    // Already on home, do nothing or refresh
                    return true;
                } else if (itemId == R.id.navigation_subjects) {
                    Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_history) {
                    Intent intent = new Intent(MainActivity.this, QuizHistoryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_performance) {
                    Intent intent = new Intent(MainActivity.this, PerformanceActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}

