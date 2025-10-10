package com.example.smartquiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smartquiz.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizHistoryActivity extends AppCompatActivity {

    private LinearLayout historyListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_history);

        initializeViews();
        populateHistoryList();
    }

    private void initializeViews() {
        historyListContainer = findViewById(R.id.historyListContainer);
    }

    private void populateHistoryList() {
        historyListContainer.removeAllViews();

        List<QuizAttempt> historyData = getMockHistoryData();

        for (QuizAttempt attempt : historyData) {
            CardView historyCard = createHistoryCard(attempt);
            historyListContainer.addView(historyCard);
        }
    }

    private CardView createHistoryCard(QuizAttempt attempt) {
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView cardView = (CardView) inflater.inflate(R.layout.history_card, historyListContainer, false);

        TextView historyTitle = cardView.findViewById(R.id.historyTitle);
        TextView historyDate = cardView.findViewById(R.id.historyDate);
        TextView historyScore = cardView.findViewById(R.id.historyScore);

        // Set data
        historyTitle.setText(String.format("%s - %s", attempt.getSubjectName(), attempt.getSetTitle()));
        historyDate.setText(formatDate(attempt.getDate()));
        historyScore.setText(String.format("%d%%", attempt.getScore()));

        // Set click listener if needed
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click - show detailed results
                showAttemptDetails(attempt);
            }
        });

        return cardView;
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

    private void showAttemptDetails(QuizAttempt attempt) {
        // Show detailed results for this attempt
        // You can implement a dialog or navigate to a details activity
        // For now, just show a toast
        String message = String.format("%s - %s: %d%%",
                attempt.getSubjectName(), attempt.getSetTitle(), attempt.getScore());
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    private List<QuizAttempt> getMockHistoryData() {
        List<QuizAttempt> history = new ArrayList<>();
        history.add(new QuizAttempt("1", "Physics", "Set 3", "2025-10-06", 89));
        history.add(new QuizAttempt("2", "Chemistry", "Set 2", "2025-10-05", 92));
        history.add(new QuizAttempt("3", "Biology", "Set 1", "2025-10-04", 78));
        history.add(new QuizAttempt("4", "Mathematics", "Set 4", "2025-10-03", 85));
        return history;
    }

    // Data class for quiz attempts
    public static class QuizAttempt {
        private String id;
        private String subjectName;
        private String setTitle;
        private String date;
        private int score;

        public QuizAttempt(String id, String subjectName, String setTitle, String date, int score) {
            this.id = id;
            this.subjectName = subjectName;
            this.setTitle = setTitle;
            this.date = date;
            this.score = score;
        }

        public String getId() { return id; }
        public String getSubjectName() { return subjectName; }
        public String getSetTitle() { return setTitle; }
        public String getDate() { return date; }
        public int getScore() { return score; }
    }
}