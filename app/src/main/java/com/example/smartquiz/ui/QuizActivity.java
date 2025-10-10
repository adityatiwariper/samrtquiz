package com.example.smartquiz.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.example.smartquiz.R;
import com.example.smartquiz.db.DBHelper;
import com.example.smartquiz.model.Question;
import com.example.smartquiz.model.Result;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    // Views
    private TextView subjectName, quizSetTitle, timerText, questionProgress, progressPercent, questionText;
    private ProgressBar progressBar;
    private RadioGroup optionsRadioGroup;
    private AppCompatButton previousButton, skipButton, nextButton;

    // Quiz data
    private String subjectId, setId;
    private Question[] questions;
    private int currentQuestion = 0;
    private Map<Integer, Integer> answers = new HashMap<>();
    private CountDownTimer countDownTimer;
    private long timeLeftMillis = 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);



        initializeViews();
        loadQuizData();
        setupClickListeners();
        startTimer();
        showQuestion();


    }


    private void animateOptionSelection(int checkedId) {
        RadioButton selected = findViewById(checkedId);
        if (selected != null) {
            selected.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(100)
                    .withEndAction(() -> selected.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start())
                    .start();
        }
    }




    private void initializeViews() {
        subjectName = findViewById(R.id.subjectName);
        quizSetTitle = findViewById(R.id.quizSetTitle);
        timerText = findViewById(R.id.timerText);
        questionProgress = findViewById(R.id.questionProgress);
        progressPercent = findViewById(R.id.progressPercent);
        questionText = findViewById(R.id.questionText);
        progressBar = findViewById(R.id.progressBar);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        previousButton = findViewById(R.id.previousButton);
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);
    }

    private void loadQuizData() {
        Intent intent = getIntent();
        subjectId = intent.getStringExtra("subjectId");
        setId = intent.getStringExtra("setId");

        // Mock questions data - replace with your actual data
        questions = new Question[]{
                new Question("What is the formula for force?",
                        new String[]{"F = ma", "F = mv", "F = mgh", "F = pV"}, 0),
                new Question("Which law states that every action has an equal and opposite reaction?",
                        new String[]{"Newton's First Law", "Newton's Second Law", "Newton's Third Law", "Law of Gravitation"}, 2),
                new Question("What is the unit of electric current?",
                        new String[]{"Volt", "Ampere", "Ohm", "Watt"}, 1),
                new Question("2 + 2 * 2 = ?",
                        new String[]{"6", "8", "4", "2",}, 1),
                new Question("Which language is used for Android native development (traditional)?",
                        new String[]{"Java", "Swift", "Kotlin", "C#"}, 1),
                new Question("Which gas do plants absorb?",
                        new String[]{"Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"}, 2),
                new Question("What is the largest ocean?",
                        new String[]{"Pacific","Atlantic", "Indian", "Arctic"}, 1)
        };

        // Set subject info
        subjectName.setText("Physics");
        quizSetTitle.setText("Set 3");
    }

    private void setupClickListeners() {
        previousButton.setOnClickListener(v -> handlePrevious());
        skipButton.setOnClickListener(v -> handleSkip());
        nextButton.setOnClickListener(v -> handleNext());

        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                int selectedOption = Integer.parseInt((String) group.findViewById(checkedId).getTag());
                answers.put(currentQuestion, selectedOption);
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                handleSubmit();
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftMillis / 1000) / 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void showQuestion() {
        if (currentQuestion >= questions.length) {
            handleSubmit();
            return;
        }

        Question question = questions[currentQuestion];

        // Update progress
        int progress = ((currentQuestion + 1) * 100) / questions.length;
        questionProgress.setText(String.format("Question %d of %d", currentQuestion + 1, questions.length));
        progressPercent.setText(progress + "%");
        progressBar.setProgress(progress);

        // Update question text
        questionText.setText(question.getText());

        // Update buttons
        previousButton.setEnabled(currentQuestion > 0);
        if (currentQuestion == questions.length - 1) {
            nextButton.setText("Submit");
            nextButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flag, 0);
        } else {
            nextButton.setText("Next");
            nextButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right, 0);
        }

        // Populate options
        populateOptions(question);
    }

    private void populateOptions(Question question) {
        optionsRadioGroup.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < question.getOptions().length; i++) {
            View optionView = inflater.inflate(R.layout.option_radio_item, optionsRadioGroup, false);

            RadioButton optionRadio = optionView.findViewById(R.id.optionRadio);
            TextView optionText = optionView.findViewById(R.id.optionText);

            optionRadio.setId(View.generateViewId());
            optionRadio.setTag(String.valueOf(i));
            optionText.setText(question.getOptions()[i]);

            // Set checked if previously answered
            if (answers.containsKey(currentQuestion) && answers.get(currentQuestion) == i) {
                optionRadio.setChecked(true);
            }

            // Set click listener for the whole option view
            optionView.setOnClickListener(v -> optionRadio.setChecked(true));

            optionsRadioGroup.addView(optionView);
        }
    }

    private void handlePrevious() {
        if (currentQuestion > 0) {
            currentQuestion--;
            showQuestion();
        }
    }

    private void handleNext() {
        if (currentQuestion == questions.length - 1) {
            handleSubmit();
        } else {
            currentQuestion++;
            showQuestion();
        }
    }

    private void handleSkip() {
        answers.put(currentQuestion, null);
        handleNext();
    }

    private void handleSubmit() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Calculate score
        int correct = 0;
        int attempted = 0;

        for (int i = 0; i < questions.length; i++) {
            Integer answer = answers.get(i);
            if (answer != null) {
                attempted++;
                if (answer == questions[i].getCorrectAnswer()) {
                    correct++;
                }
            }
        }

        int score = (correct * 100) / questions.length;

        // Navigate to result screen
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("setId", setId);
        intent.putExtra("totalQuestions", questions.length);
        intent.putExtra("attempted", attempted);
        intent.putExtra("correct", correct);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Data class for questions
    private static class Question {
        private String text;
        private String[] options;
        private int correctAnswer;

        public Question(String text, String[] options, int correctAnswer) {
            this.text = text;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getText() { return text; }
        public String[] getOptions() { return options; }
        public int getCorrectAnswer() { return correctAnswer; }
    }
}
