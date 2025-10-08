package com.example.smartquiz.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smartquiz.R;
import com.example.smartquiz.db.DBHelper;
import com.example.smartquiz.model.Question;
import com.example.smartquiz.model.Result;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    List<Question> questionList;
    int currentIndex = 0;
    int correct = 0, wrong = 0;

    CountDownTimer timer;
    TextView txtQuestion, txtTimer, txtProgress;
    RadioGroup optionsGroup;
    Button btnNext, btnPrev, btnSubmit;
    DBHelper db;
    ProgressBar progressBar;
    CardView cardQuestion;

    long timeLeftMillis;
    long selectedDuration = 5 * 60 * 1000; // default 5 mins
    HashMap<Integer, Integer> selectedAnswers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        db = new DBHelper(this);
        questionList = db.getAllQuestions();

        // Shuffle the questions randomly
        Collections.shuffle(questionList);

        txtQuestion = findViewById(R.id.txtQuestion);
        txtTimer = findViewById(R.id.txtTimer);
        txtProgress = findViewById(R.id.txtProgress);
        optionsGroup = findViewById(R.id.optionsGroup);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        cardQuestion = findViewById(R.id.cardQuestion);

        // Disable buttons until quiz starts
        btnNext.setEnabled(false);
        btnPrev.setEnabled(false);
        btnSubmit.setEnabled(false);

        // Show time selection dialog
        showTimeSelectionDialog();

        // Button listeners
        btnNext.setOnClickListener(v -> {
            animateButton(v);
            saveSelection();
            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                animateQuestionChange(this::loadQuestion);
            }
        });

        btnPrev.setOnClickListener(v -> {
            animateButton(v);
            saveSelection();
            if (currentIndex > 0) {
                currentIndex--;
                animateQuestionChange(this::loadQuestion);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            animateButton(v);
            saveSelection();
            finishAndShowResult();
        });

        // Animate option selection
        optionsGroup.setOnCheckedChangeListener((group, checkedId) -> animateOptionSelection(checkedId));
    }

    // --- TIME SELECTION DIALOG ---
    private void showTimeSelectionDialog() {
        String[] options = {"5 minutes", "10 minutes", "15 minutes"};
        int[] timesInMillis = {5 * 60 * 1000, 10 * 60 * 1000, 15 * 60 * 1000};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Select Test Duration")
                .setSingleChoiceItems(options, 0, (dialog, which) -> selectedDuration = timesInMillis[which])
                .setPositiveButton("Start", (dialog, which) -> startQuizWithDuration(selectedDuration))
                .setCancelable(false)
                .show();
    }

    private void startQuizWithDuration(long durationMillis) {
        loadQuestion();
        startTimer(durationMillis);

        btnNext.setEnabled(true);
        btnPrev.setEnabled(true);
        btnSubmit.setEnabled(true);
    }

    // --- ANIMATIONS ---
    private void animateQuestionChange(Runnable onAnimationEnd) {
        cardQuestion.animate()
                .alpha(0f)
                .translationX(-50f)
                .setDuration(200)
                .withEndAction(() -> {
                    onAnimationEnd.run();
                    cardQuestion.setAlpha(0f);
                    cardQuestion.setTranslationX(50f);
                    cardQuestion.animate()
                            .alpha(1f)
                            .translationX(0f)
                            .setDuration(200)
                            .start();
                }).start();
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

    private void animateButton(View btn) {
        btn.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(50)
                .withEndAction(() -> btn.animate().scaleX(1f).scaleY(1f).setDuration(50).start())
                .start();
    }

    // --- TIMER WITH SMOOTH PROGRESS ---
    private void startTimer(long duration) {
        timeLeftMillis = duration;
        progressBar.setMax((int) duration / 1000);

        timer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;

                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                txtTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));

                // Smooth progress bar animation
                int progress = (int) (millisUntilFinished / 1000);
                ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), progress)
                        .setDuration(500)
                        .start();
            }

            public void onFinish() {
                finishAndShowResult();
            }
        }.start();
    }

    // --- QUESTION LOADING ---
    private void loadQuestion() {
        Question q = questionList.get(currentIndex);
        txtQuestion.setText(q.getQuestion());
        ((RadioButton) findViewById(R.id.opt1)).setText(q.getOption1());
        ((RadioButton) findViewById(R.id.opt2)).setText(q.getOption2());
        ((RadioButton) findViewById(R.id.opt3)).setText(q.getOption3());
        ((RadioButton) findViewById(R.id.opt4)).setText(q.getOption4());
        optionsGroup.clearCheck();
        txtProgress.setText((currentIndex + 1) + "/" + questionList.size());

        Integer sel = selectedAnswers.get(currentIndex);
        if (sel != null) {
            int id = R.id.opt1;
            switch (sel) {
                case 1: id = R.id.opt1; break;
                case 2: id = R.id.opt2; break;
                case 3: id = R.id.opt3; break;
                case 4: id = R.id.opt4; break;
            }
            optionsGroup.check(id);
        }
    }

    private void saveSelection() {
        int selId = optionsGroup.getCheckedRadioButtonId();
        if (selId == -1) return;
        int val = 1;
        if (selId == R.id.opt2) val = 2;
        else if (selId == R.id.opt3) val = 3;
        else if (selId == R.id.opt4) val = 4;
        selectedAnswers.put(currentIndex, val);
    }

    // --- FINISH QUIZ AND SHOW RESULT ---
    private void finishAndShowResult() {
        if (timer != null) timer.cancel();
        correct = 0; wrong = 0;
        int total = questionList.size();
        for (int i = 0; i < total; i++) {
            Integer sel = selectedAnswers.get(i);
            if (sel == null) continue;
            String chosen = "";
            switch (sel) {
                case 1: chosen = questionList.get(i).getOption1(); break;
                case 2: chosen = questionList.get(i).getOption2(); break;
                case 3: chosen = questionList.get(i).getOption3(); break;
                case 4: chosen = questionList.get(i).getOption4(); break;
            }
            if (chosen.equals(questionList.get(i).getCorrect())) correct++;
            else wrong++;
        }
        int attempted = selectedAnswers.size();
        int skipped = total - attempted;
        int score = correct * 4 - wrong;
        int percentage = Math.round((correct * 100.0f) / total);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date());
        Result r = new Result(date, total, attempted, skipped, correct, wrong, score);
        db.insertResult(r);

        Intent i = new Intent(QuizActivity.this, ResultActivity.class);
        i.putExtra("total", total);
        i.putExtra("attempted", attempted);
        i.putExtra("skipped", skipped);
        i.putExtra("correct", correct);
        i.putExtra("wrong", wrong);
        i.putExtra("score", score);
        i.putExtra("percentage", percentage);
        startActivity(i);
        finish();
    }
}
