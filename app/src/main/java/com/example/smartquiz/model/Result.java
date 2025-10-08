package com.example.smartquiz.model;

public class Result {
    private String date;
    private int total, attempted, skipped, correct, wrong, score;

    public Result(String date, int total, int attempted, int skipped, int correct, int wrong, int score) {
        this.date = date;
        this.total = total;
        this.attempted = attempted;
        this.skipped = skipped;
        this.correct = correct;
        this.wrong = wrong;
        this.score = score;
    }

    public String getDate() { return date; }
    public int getTotal() { return total; }
    public int getAttempted() { return attempted; }
    public int getSkipped() { return skipped; }
    public int getCorrect() { return correct; }
    public int getWrong() { return wrong; }
    public int getScore() { return score; }
}
