package com.example.OHassignment2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String fullName = "Omar Hussein";
    private final String studentId = "555222333";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvFullName = findViewById(R.id.tvFullName);
        TextView tvStudentId = findViewById(R.id.tvStudentId);
        tvFullName.setText("Full Name: " + fullName);
        tvStudentId.setText("Student ID: " + studentId);

        Button btnStartExplicit = findViewById(R.id.btnStartExplicit);
        Button btnStartImplicit = findViewById(R.id.btnStartImplicit);

        btnStartExplicit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        btnStartImplicit.setOnClickListener(v -> {
            Intent intent = new Intent("com.example.cs412androidapp.SHOW_CHALLENGES");
            intent.setPackage(getPackageName()); // keep it within this app for this exercise
            startActivity(intent);
        });
    }
}
