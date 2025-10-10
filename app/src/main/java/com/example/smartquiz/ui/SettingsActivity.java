package com.example.smartquiz.ui;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import com.example.smartquiz.R;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat darkModeSwitch;
    private AppCompatButton importCsvButton;
    private AppCompatButton deleteDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        importCsvButton = findViewById(R.id.importCsvButton);
        deleteDataButton = findViewById(R.id.deleteDataButton);
    }

    private void setupClickListeners() {
        // Dark Mode Switch
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onDarkModeChanged(isChecked);
            }
        });

        // Import CSV Button
        importCsvButton.setOnClickListener(v -> {
            onImportCsvClicked();
        });

        // Delete Data Button
        deleteDataButton.setOnClickListener(v -> {
            onDeleteDataClicked();
        });
    }

    private void onDarkModeChanged(boolean isEnabled) {
        // Implement dark mode logic here
        String message = isEnabled ? "Dark mode enabled" : "Dark mode disabled";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // You can implement actual dark mode switching here
        // For example, using AppCompatDelegate.setDefaultNightMode()
    }

    private void onImportCsvClicked() {
        Toast.makeText(this,
                "CSV Import\nThis feature allows admins to import question CSV files.",
                Toast.LENGTH_LONG).show();

        // Implement CSV import logic here
        // You can use Intent.ACTION_GET_CONTENT to let user select CSV file
    }

    private void onDeleteDataClicked() {
        // Show confirmation dialog
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("This will delete all quiz data. This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Perform delete operation
                    deleteAllData();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAllData() {
        // Implement data deletion logic here
        // Clear database, shared preferences, etc.

        Toast.makeText(this, "All quiz data has been deleted", Toast.LENGTH_SHORT).show();

        // You might want to restart the app or navigate to main screen
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
    }
}
