package com.example.smartquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import com.example.smartquiz.R;
import com.example.smartquiz.model.Subject;

public class SubjectsActivity extends AppCompatActivity {

    private GridLayout subjectsGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        initializeViews();
        populateSubjectsGrid();
    }

    private void initializeViews() {
        subjectsGrid = findViewById(R.id.subjectsGrid);
    }

    private void populateSubjectsGrid() {
        subjectsGrid.removeAllViews();

        // Sample subjects data
        Subject[] subjects = {
                new Subject("physics", "Physics", "⚛️", 1250),
                new Subject("chemistry", "Chemistry", "🧪", 980),
                new Subject("biology", "Biology", "🧬", 1100),
                new Subject("mathematics", "Mathematics", "📐", 1500),
                new Subject("history", "History", "📜", 800),
                new Subject("geography", "Geography", "🌍", 750)
        };

        for (Subject subject : subjects) {
            CardView subjectCard = createSubjectCard(subject);
            subjectsGrid.addView(subjectCard);
        }
    }

    private CardView createSubjectCard(Subject subject) {
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView cardView = (CardView) inflater.inflate(R.layout.subject_card, subjectsGrid, false);

        TextView subjectIcon = cardView.findViewById(R.id.subjectIcon);
        TextView subjectName = cardView.findViewById(R.id.subjectName);
        TextView questionCount = cardView.findViewById(R.id.questionCount);

        // Set subject data
        subjectIcon.setText(subject.getIcon());
        subjectName.setText(subject.getName());
        questionCount.setText(String.format("%,d questions", subject.getQuestionCount()));

        // Set click listener
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSubjectDetail(subject);
            }
        });

        return cardView;
    }

    private void openSubjectDetail(Subject subject) {
        Intent intent = new Intent(SubjectsActivity.this, SubjectDetailActivity.class);
        intent.putExtra("subjectId", subject.getId());
        intent.putExtra("subjectName", subject.getName());
        intent.putExtra("subjectIcon", subject.getIcon());
        intent.putExtra("totalQuestions", subject.getQuestionCount());
        startActivity(intent);
    }

    // Data class for subjects

}
