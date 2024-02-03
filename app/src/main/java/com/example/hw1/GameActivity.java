package com.example.hw1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int ROWS = 6;
    private static final int COLS = 3;
    private ImageView[] heart;
    private final ImageView[][] gameMatrix = new ImageView[ROWS][COLS];

    private final ImageView[][] rockMatrix = new ImageView[ROWS][COLS];
    private int lives = 3;

    private static int curScore = 0;
    private static TextView scoreTextView ;
    private int currentColumn = 1;
    private static final int DELAY = 1400;
    private static final int ROCK = R.drawable.rock;
    private static final int CAR_IMAGE = R.drawable.car;

    Random random = new Random();
    private Handler delayHandler = new Handler();


    @SuppressLint({"MissingInflatedId", "DiscouragedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setGameMatrix();


        heart = new ImageView[3];
        Button leftArrowButton = findViewById(R.id.leftArrowButton);
        Button rightArrowButton = findViewById(R.id.rightArrowButton);

        for (int i = 0; i < 3; i++) {
            heart[i] = findViewById(getResources().getIdentifier("heart" + (i + 1), "id", getPackageName()));
        }

        leftArrowButton.setOnClickListener(View -> moveCarLeft());
        rightArrowButton.setOnClickListener(View -> moveCarRight());
        scoreTextView = findViewById(R.id.score);
        delayHandler.postDelayed(runnable, DELAY);
        startScoreTimer();
    }

    private void spawnRocks() {
        delayHandler.postDelayed(this::randomRocks, DELAY);
    }

    private void randomRocks() {
        int i = random.nextInt(3);
        if (rockMatrix[0][i].getDrawable() == null) {
            rockMatrix[0][i].setImageResource(ROCK);

        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                moveRocks();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            delayHandler.postDelayed(this, DELAY);
        spawnRocks();

        }
    };

    private void moveRocks() throws InterruptedException {
        // Move rocks downward

        for (int i = ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                if (rockMatrix[i][j].getDrawable() != null) {
                    rockMatrix[i + 1][j].setImageDrawable(rockMatrix[i][j].getDrawable());
                    rockMatrix[i][j].setImageResource(0);

                    if (i + 1 == ROWS - 1 && (gameMatrix[ROWS - 1][j].getDrawable() != null) ) {
                        // Rock reached the bottom, remove it
                        rockMatrix[ROWS - 1][j].setImageDrawable(null);
                    }if(gameMatrix[ROWS - 1][currentColumn].getDrawable() == rockMatrix[i + 1][j].getDrawable()){
                        handleCollision();
                        gameMatrix[ROWS - 1][currentColumn].setImageResource(CAR_IMAGE);
                    }

                }
            }

        }
    }

    private void moveCarLeft() {
        if (currentColumn != 0) {

            moveCar(currentColumn - 1);

        }
    }

    private void moveCarRight() {
        // Move the car right only if it's not at the rightmost column
        if (currentColumn != COLS - 1) {

            moveCar(currentColumn + 1);
        }
    }

    private void moveCar(int newCol) {
        gameMatrix[ROWS - 1][currentColumn].setImageResource(0);

        gameMatrix[ROWS - 1][newCol].setImageResource(CAR_IMAGE);
        currentColumn = newCol;
    }

    private void setGameMatrix() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gameMatrix[i][j] = findViewById(R.id.row1_col1 + (i * COLS) + j);
                rockMatrix[i][j] = findViewById(R.id.row1_col1 + (i * COLS) + j);
            }
        }
    }


    private void handleCollision() {
        // Reduce lives
        lives--;
        // Show a toast message
        Toast.makeText(this, "Collision! Lives remaining: " + lives, Toast.LENGTH_SHORT).show();
        // Vibrate the device
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        // Update the UI with remaining lives
        updateLivesUI();
        // Check for game over
        if (lives == 0) {
            gameOver();
        }
    }

    private void updateLivesUI() {
        // Update UI to display remaining lives
        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                heart[i].setVisibility(View.VISIBLE);
            } else {
                heart[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void startScoreTimer() {
        Handler scoreHandler = new Handler();
        scoreHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Increment the score and update the TextView
                curScore++;
                scoreTextView.setText("Score: " + curScore);

                // Call this runnable again after a delay (e.g., 1000 milliseconds for 1 second)
                scoreHandler.postDelayed(this, 1000);
            }
        }, 1000); // Start the timer after 1 second
    }

    private void gameOver() {
        // Display game over message and handle game over actions
        Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,MainActivity.class);
        delayHandler.removeCallbacks(runnable);
        delayHandler.removeCallbacksAndMessages(null);
        runnable = null;
        delayHandler = null;
        startActivity(intent);


    }


}
