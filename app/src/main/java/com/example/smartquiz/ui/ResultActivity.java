package com.example.smartquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartquiz.R;

public class ResultActivity extends AppCompatActivity {
    TextView tvSummary, tvPercent;
    Button btnHome,btnRestart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvSummary = findViewById(R.id.tvSummary);
        tvPercent = findViewById(R.id.tvPercent);
        btnHome = findViewById(R.id.btnHome);
        btnRestart = findViewById(R.id.btnRestart);

        int total = getIntent().getIntExtra("total", 0);
        int attempted = getIntent().getIntExtra("attempted", 0);
        int skipped = getIntent().getIntExtra("skipped", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        int wrong = getIntent().getIntExtra("wrong", 0);
        int score = getIntent().getIntExtra("score", 0);
        int percentage = getIntent().getIntExtra("percentage", 0);

        String s = "Total: " + total + "\nAttempted: " + attempted + "\nSkipped: " + skipped + "\nCorrect: " + correct + "\nWrong: " + wrong + "\nScore: " + score;
        tvSummary.setText(s);
        tvPercent.setText("Score: " + percentage + "%");
        btnHome.setOnClickListener(v->{
            finish();
        });
        btnRestart.setOnClickListener(v->{
            startActivity(new Intent(ResultActivity.this, QuizActivity.class));
            finish();
        });
    }
}
