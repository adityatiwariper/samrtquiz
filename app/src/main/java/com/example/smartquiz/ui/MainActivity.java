package com.example.smartquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartquiz.R;

public class MainActivity extends AppCompatActivity {
    Button btnStart, btnHistory, btnPerformance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = findViewById(R.id.btnStart);
        btnHistory = findViewById(R.id.btnHistory);
        btnPerformance = findViewById(R.id.btnPerformance);

        btnStart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, QuizActivity.class)));
        btnHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
        btnPerformance.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PerformanceActivity.class)));
    }
}

