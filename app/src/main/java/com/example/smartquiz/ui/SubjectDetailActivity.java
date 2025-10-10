package com.example.smartquiz.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.smartquiz.R;
import com.google.android.material.button.MaterialButton;

public class SubjectDetailActivity extends AppCompatActivity {

    private TextView subjectName, subjectIcon, totalQuestions;
    private AppCompatButton backButton, generateSetButton;
    private LinearLayout quizSetsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        initializeViews();
        setupClickListeners();
        loadSubjectData();
        populateQuizSets();
    }

    private void initializeViews() {
        subjectName = findViewById(R.id.subjectName);
        subjectIcon = findViewById(R.id.subjectIcon);
        totalQuestions = findViewById(R.id.totalQuestions);
        backButton = findViewById(R.id.backButton);
        generateSetButton = findViewById(R.id.generateSetButton);
        quizSetsContainer = findViewById(R.id.quizSetsContainer);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        generateSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewSet();
            }
        });
    }

    private void loadSubjectData() {
        // Get subject data from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("subjectName")) {
            String name = intent.getStringExtra("subjectName");
            String icon = intent.getStringExtra("subjectIcon");
            int questionCount = intent.getIntExtra("totalQuestions", 0);

            subjectName.setText(name);
            subjectIcon.setText(icon);
            totalQuestions.setText(String.format("%,d Total Questions", questionCount));
        } else {
            // Default data if no intent
            subjectName.setText("Chemistry");
            subjectIcon.setText("🧪");
            totalQuestions.setText("1,250 Total Questions");
        }
    }

    private void populateQuizSets() {
        quizSetsContainer.removeAllViews();

        // Sample quiz sets data
        QuizSet[] quizSets = {
                new QuizSet("Set 1", 100, 100, 92, "Completed"),
                new QuizSet("Set 2", 100, 75, 85, "In Progress"),
                new QuizSet("Set 3", 100, 0, 0, "Not Started"),
                new QuizSet("Set 4", 100, 45, 78, "In Progress")
        };

        for (QuizSet set : quizSets) {
            View setCardView = createQuizSetCard(set);
            quizSetsContainer.addView(setCardView);
        }
    }

    private View createQuizSetCard(QuizSet set) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View cardView = inflater.inflate(R.layout.quiz_set_card, quizSetsContainer, false);

        TextView setName = cardView.findViewById(R.id.setName);
        TextView setStatus = cardView.findViewById(R.id.setStatus);
        TextView progressText = cardView.findViewById(R.id.progressText);
        TextView bestScore = cardView.findViewById(R.id.bestScore);
        ProgressBar progressBar = cardView.findViewById(R.id.progressBar);
        AppCompatButton startQuizButton = cardView.findViewById(R.id.startQuizButton);
        AppCompatButton viewDetailsButton = cardView.findViewById(R.id.viewDetailsButton);

        // Set data
        setName.setText(set.getName());
        progressText.setText(String.format("%d/%d questions", set.getCompleted(), set.getTotal()));
        progressBar.setMax(set.getTotal());
        progressBar.setProgress(set.getCompleted());

        // Set status
        if ("Completed".equals(set.getStatus())) {
            setStatus.setText("Completed");
            setStatus.setBackgroundResource(R.drawable.completed_status_background);
        } else if ("In Progress".equals(set.getStatus())) {
            setStatus.setText("In Progress");
            setStatus.setBackgroundResource(R.drawable.in_progress_status_background);
        } else {
            setStatus.setText("Not Started");
            setStatus.setBackgroundResource(R.drawable.not_started_status_background);
        }

        // Set best score
        if (set.getBestScore() > 0) {
            bestScore.setText(String.format("Best: %d%%", set.getBestScore()));
        } else {
            bestScore.setText("Not attempted");
            bestScore.setTextColor(getResources().getColor(R.color.secondary_text));
        }

        // Set button click listeners
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz(set);
            }
        });

        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSetDetails(set);
            }
        });

        return cardView;
    }

    private void generateNewSet() {
        Toast.makeText(this,
                "New quiz set generated!\nA new set of 100 questions has been created with <30% overlap.",
                Toast.LENGTH_LONG).show();

        // Add new set to the list
        QuizSet newSet = new QuizSet("Set " + (quizSetsContainer.getChildCount() + 1),
                100, 0, 0, "Not Started");
        View newCard = createQuizSetCard(newSet);
        quizSetsContainer.addView(newCard, 0); // Add at top
    }

    private void startQuiz(QuizSet set) {
        Intent intent = new Intent(SubjectDetailActivity.this, QuizActivity.class);
        startActivity(intent);
        // Navigate to quiz activity
    }

    private void viewSetDetails(QuizSet set) {
        Toast.makeText(this, "Viewing details: " + set.getName(), Toast.LENGTH_SHORT).show();
        // Navigate to set details activity
    }

    // Data class for quiz sets
    private static class QuizSet {
        private String name;
        private int total;
        private int completed;
        private int bestScore;
        private String status;

        public QuizSet(String name, int total, int completed, int bestScore, String status) {
            this.name = name;
            this.total = total;
            this.completed = completed;
            this.bestScore = bestScore;
            this.status = status;
        }

        public String getName() { return name; }
        public int getTotal() { return total; }
        public int getCompleted() { return completed; }
        public int getBestScore() { return bestScore; }
        public String getStatus() { return status; }
    }
}