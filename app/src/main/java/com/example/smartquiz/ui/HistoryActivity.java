package com.example.smartquiz.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartquiz.R;
import com.example.smartquiz.db.DBHelper;

public class HistoryActivity extends AppCompatActivity {
    ListView listView;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = findViewById(R.id.listView);
        db = new DBHelper(this);
        Cursor c = db.getAllResultsCursor();
        String[] from = new String[] { "date", "score" };
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to, 0);
        listView.setAdapter(adapter);
    }
}
