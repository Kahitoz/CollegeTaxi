package com.example.projecty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Button change;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        change = findViewById(R.id.change_page);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   startActivity(new Intent(getApplicationContext(), Login_Page.class));
                   finish();
            }
        });
    }
}