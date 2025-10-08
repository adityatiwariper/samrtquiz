package com.example.smartquiz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartquiz.model.Question;
import com.example.smartquiz.model.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smartquiz.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE questions(id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, option1 TEXT, option2 TEXT, option3 TEXT, option4 TEXT, correct_answer TEXT)");
        db.execSQL("CREATE TABLE results(id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, total INTEGER, attempted INTEGER, skipped INTEGER, correct INTEGER, wrong INTEGER, score INTEGER)");
        insertSampleQuestions(db);
    }

    private void insertSampleQuestions(SQLiteDatabase db) {
        insertQuestion(db, "What is the capital of India?", "Mumbai", "Chennai", "New Delhi", "Kolkata", "New Delhi");
        insertQuestion(db, "Which planet is known as the Red Planet?", "Earth", "Mars", "Jupiter", "Venus", "Mars");
        insertQuestion(db, "Who wrote 'Romeo and Juliet'?", "Mark Twain", "William Shakespeare", "Charles Dickens", "Leo Tolstoy", "William Shakespeare");
        insertQuestion(db, "What is H2O commonly known as?", "Salt", "Water", "Oxygen", "Hydrogen", "Water");
        insertQuestion(db, "2 + 2 * 2 = ?", "6", "8", "4", "2", "6");
        insertQuestion(db, "Which language is used for Android native development (traditional)?", "Java", "Swift", "Kotlin", "C#", "Java");
        insertQuestion(db, "Which gas do plants absorb?", "Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen", "Carbon Dioxide");
        insertQuestion(db, "What is the largest ocean?", "Atlantic", "Indian", "Arctic", "Pacific", "Pacific");
        insertQuestion(db, "Which instrument measures temperature?", "Barometer", "Thermometer", "Hygrometer", "Anemometer", "Thermometer");
        insertQuestion(db, "Who is known as the father of computers?", "Charles Babbage", "Alan Turing", "Tim Berners-Lee", "Bill Gates", "Charles Babbage");
    }

    private void insertQuestion(SQLiteDatabase db, String q, String a, String b, String c, String d, String correct) {
        ContentValues cv = new ContentValues();
        cv.put("question", q);
        cv.put("option1", a);
        cv.put("option2", b);
        cv.put("option3", c);
        cv.put("option4", d);
        cv.put("correct_answer", correct);
        db.insert("questions", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS results");
        onCreate(db);
    }

    public List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM questions", null);
        if (cursor.moveToFirst()) {
            do {
                Question q = new Question(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("question")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option1")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option2")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option3")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option4")),
                        cursor.getString(cursor.getColumnIndexOrThrow("correct_answer"))
                );
                list.add(q);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.shuffle(list);
        return list;
    }

    public void insertResult(Result r) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("date", r.getDate());
        cv.put("total", r.getTotal());
        cv.put("attempted", r.getAttempted());
        cv.put("skipped", r.getSkipped());
        cv.put("correct", r.getCorrect());
        cv.put("wrong", r.getWrong());
        cv.put("score", r.getScore());
        db.insert("results", null, cv);
    }


    public Cursor getAllResultsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Alias 'id' as '_id' so CursorAdapter can find it
        return db.rawQuery("SELECT id AS _id, date, total, attempted, skipped, correct, wrong, score FROM results ORDER BY id DESC", null);
    }

    public int getTotalTests() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM results", null);
        int total = 0;
        if (c.moveToFirst()) total = c.getInt(0);
        c.close();
        return total;
    }

    public int getAverageScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT AVG(score) FROM results", null);
        int avg = 0;
        if (c.moveToFirst()) avg = (int)Math.round(c.getDouble(0));
        c.close();
        return avg;
    }

    public int getBestScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT MAX(score) FROM results", null);
        int best = 0;
        if (c.moveToFirst()) best = c.getInt(0);
        c.close();
        return best;
    }
}
