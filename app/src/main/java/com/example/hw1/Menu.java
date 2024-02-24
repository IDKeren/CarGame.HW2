package com.example.hw1;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    private Button fastButton;
    private Button slowButton;
    private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        findViews();
        setOnClickListners();

    }

    private void startGameActivity(int delay) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("DELAY_KEY",delay);
        startActivity(intent);
        finish();
    }

    private void findViews(){
        fastButton = findViewById(R.id.fastButton);
        slowButton = findViewById(R.id.slowButton);
        exitButton = findViewById(R.id.exitButton);
    }

    private void setOnClickListners(){
        // Set an OnClickListener for the "Slow" button
        slowButton.setOnClickListener(View -> startGameActivity(1800));

        // Set an OnClickListener for the "Fast" button
        fastButton.setOnClickListener(View -> startGameActivity(1000));

        // Set an OnClickListener for the "Exit" button
        exitButton.setOnClickListener(View -> finish());
    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}