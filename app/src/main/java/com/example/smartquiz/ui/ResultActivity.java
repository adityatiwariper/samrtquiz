package com.example.smartquiz.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.WindowCompat;

import com.example.smartquiz.R;

public class ResultActivity extends AppCompatActivity {
    private TextView scorePercent, subjectInfo, totalQuestions, correctAnswers, wrongAnswers, skippedQuestions;
    private ImageView scoreProgress;
    private AppCompatButton viewHistoryButton, goHomeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_result);
        initializeViews();
        loadResultData();
        setupClickListeners();
        animateScoreProgress();
    }

    private void initializeViews() {
        scorePercent = findViewById(R.id.scorePercent);
        subjectInfo = findViewById(R.id.subjectInfo);
        totalQuestions = findViewById(R.id.totalQuestions);
        correctAnswers = findViewById(R.id.correctAnswers);
        wrongAnswers = findViewById(R.id.wrongAnswers);
        skippedQuestions = findViewById(R.id.skippedQuestions);
        scoreProgress = findViewById(R.id.scoreProgress);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);
        goHomeButton = findViewById(R.id.goHomeButton);
    }

    private void loadResultData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("score")) {
            int totalQuestions = intent.getIntExtra("totalQuestions", 0);
            int attempted = intent.getIntExtra("attempted", 0);
            int correct = intent.getIntExtra("correct", 0);
            int score = intent.getIntExtra("score", 0);
            String subjectId = intent.getStringExtra("subjectId");
            String setId = intent.getStringExtra("setId");

            // Calculate derived values
            int wrong = attempted - correct;
            int skipped = totalQuestions - attempted;

            // Update UI
            this.totalQuestions.setText(String.valueOf(totalQuestions));
            correctAnswers.setText(String.valueOf(correct));
            wrongAnswers.setText(String.valueOf(wrong));
            skippedQuestions.setText(String.valueOf(skipped));

            // Set subject info
            subjectInfo.setText("Physics - Set 3");
        } else {
            setDefaultData();
        }
    }

    private void setDefaultData() {
        totalQuestions.setText("100");
        correctAnswers.setText("85");
        wrongAnswers.setText("10");
        skippedQuestions.setText("5");
        subjectInfo.setText("Physics - Set 3");
    }

    private void animateScoreProgress() {
        Intent intent = getIntent();
        int finalScore = intent != null && intent.hasExtra("score") ?
                intent.getIntExtra("score", 0) : 85;

        // Animate score percentage
        ValueAnimator scoreAnimator = ValueAnimator.ofInt(0, finalScore);
        scoreAnimator.setDuration(2000);
        scoreAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentScore = (int) animation.getAnimatedValue();
                scorePercent.setText(currentScore + "%");
            }
        });
        scoreAnimator.start();

        // Animate other stats with delay
        animateStatWithDelay(totalQuestions, 300);
        animateStatWithDelay(correctAnswers, 600);
        animateStatWithDelay(wrongAnswers, 900);
        animateStatWithDelay(skippedQuestions, 1200);
    }

    private void animateStatWithDelay(final TextView textView, long delay) {
        textView.setScaleX(0f);
        textView.setScaleY(0f);
        textView.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .setStartDelay(delay)
                .start();
    }

    private void setupClickListeners() {
        viewHistoryButton.setOnClickListener(v -> navigateToHistory());
        goHomeButton.setOnClickListener(v -> navigateToHome());
    }

    private void navigateToHistory() {
        Intent intent = new Intent(this, QuizHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
