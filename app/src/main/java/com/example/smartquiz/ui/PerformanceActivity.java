package com.example.smartquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartquiz.R;
import com.example.smartquiz.db.DBHelper;

public class PerformanceActivity extends AppCompatActivity {
    TextView tvTotal, tvAvg, tvBest;
    Button btnBackHome;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        tvTotal = findViewById(R.id.tvTotal);
        btnBackHome = findViewById(R.id.btnBackHome);
        tvAvg = findViewById(R.id.tvAvg);
        tvBest = findViewById(R.id.tvBest);
        db = new DBHelper(this);

        tvTotal.setText("Total tests: " + db.getTotalTests());
        tvAvg.setText("Average score: " + db.getAverageScore());
        tvBest.setText("Best score: " + db.getBestScore());

        btnBackHome.setOnClickListener(v -> {
            finish();
        });
    }
}
