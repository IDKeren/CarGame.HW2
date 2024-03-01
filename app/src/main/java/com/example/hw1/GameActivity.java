package com.example.hw1;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Interface.StepCallback;
import com.example.hw1.Models.LeaderBoardList;
import com.example.hw1.Models.Player;
import com.example.hw1.Utilities.BackgroundSound;
import com.example.hw1.Utilities.SharedPreferencesManager;
import com.example.hw1.Utilities.SignalManager;
import com.example.hw1.Utilities.StepDetector;
import com.google.gson.Gson;

import java.util.Random;



public class GameActivity extends AppCompatActivity {

    private static final int ROWS = 9;
    private static final int COLS = 5;
    private final ImageView[] heart = new ImageView[3];
    private final ImageView[][] gameMatrix = new ImageView[ROWS][COLS];
    private final ImageView[][] drawableMatrix = new ImageView[ROWS][COLS];
    private final int[][] coinMatrix = new int[ROWS][COLS];
    private int lives = 3;
    private TextView scoreTextView ;
    private int currentColumn = 2;
    private int curScore = 0;
    private static int DELAY = 0;
    private static final int DEFAULT_DELAY_VALUE = 100;
    private static final int ROCK = R.drawable.rock;
    private static final int CAR_IMAGE = R.drawable.car;
    private static final int COIN = R.drawable.coin;
    Random random = new Random();
    private final Handler delayHandler = new Handler();
    private Button leftArrowButton;
    private Button rightArrowButton;
    private StepDetector stepDetector;
    private BackgroundSound backgroundSound;
    private Player newPlayer;
    private SharedPreferencesManager sharedPreferencesManager;
    private LeaderBoardList leaderboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setGameMatrix();
        startScoreTimer();
        newPlayer = new Player();
        sharedPreferencesManager = SharedPreferencesManager.getInstance();
        leaderboardList = sharedPreferencesManager.getLeaderboardList("LEADERBOARDLIST");
        backgroundSound = new BackgroundSound(this,R.raw.car_crash);

        leftArrowButton.setOnClickListener(View -> moveCarLeft());
        rightArrowButton.setOnClickListener(View -> moveCarRight());
        DELAY = getIntent().getIntExtra("DELAY_KEY",DEFAULT_DELAY_VALUE);

        delayHandler.postDelayed(runnable, DELAY);
    }

    @SuppressLint("DiscouragedApi")
    private void findViews(){
        leftArrowButton = findViewById(R.id.leftArrowButton);
        rightArrowButton = findViewById(R.id.rightArrowButton);
        scoreTextView = findViewById(R.id.score);


        for (int i = 0; i < 3; i++) {
            heart[i] = findViewById(getResources().getIdentifier("heart" + (i + 1), "id", getPackageName()));
        }
    }

    private void spawnRocks() {
        delayHandler.postDelayed(this::randomRocks, DELAY);
    }

    private void spawnCoins() {
        delayHandler.postDelayed(this::randomCoins, DELAY);
    }
    @SuppressLint("ResourceType")
    private void randomRocks() {
        // Generate random rocks
        int i = random.nextInt(5);
        if (drawableMatrix[0][i].getDrawable() == null) {
            drawableMatrix[0][i].setImageResource(ROCK);
            coinMatrix[0][i] = 2;
        }
    }

    @SuppressLint("ResourceType")
    private void randomCoins() {
        // Generate random rocks
        int i = random.nextInt(5);
        if (drawableMatrix[0][i].getDrawable() == null) {
            drawableMatrix[0][i].setImageResource(COIN);
            coinMatrix[0][i] = 1;
        }
    }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    moveRocksWithCoins();
                    delayHandler.postDelayed(this, DELAY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                spawnRocks();
                spawnCoins();

            }
        };



    @SuppressLint("ResourceType")
    private void moveRocksWithCoins() throws InterruptedException {
        // Move rocks downward

        for (int i = ROWS - 2; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                if (drawableMatrix[i][j].getDrawable() != null && coinMatrix[i][j] != 0 ) {
                    if (coinMatrix[i][j] == 2) {
                        coinMatrix[i + 1][j] = 2;
                    } else {
                        coinMatrix[i + 1][j] = 1;
                    }
                    drawableMatrix[i + 1][j].setImageDrawable(drawableMatrix[i][j].getDrawable());
                    drawableMatrix[i][j].setImageDrawable(null);
                    coinMatrix[i][j] = 0;

                    if (i + 1 == ROWS - 1 && (gameMatrix[ROWS - 1][j].getDrawable() != null) ) {
                        // Drawable reached the bottom, remove it
                        drawableMatrix[ROWS - 1][j].setImageDrawable(null);

                        gameMatrix[ROWS - 1][currentColumn].setImageResource(CAR_IMAGE);

                        if (coinMatrix[ROWS - 1][currentColumn] == 2  ) {
                            handleCollision();
                            gameMatrix[ROWS - 1][currentColumn].setImageResource(CAR_IMAGE);

                        }
                        if (coinMatrix[ROWS - 1][currentColumn] == 1 ) {

                            curScore = curScore + 10;
                            gameMatrix[ROWS - 1][currentColumn].setImageResource(CAR_IMAGE);
                        }
                        coinMatrix[ROWS - 1][j] = 0;

                    }
                }

            }
        }
    }

    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepX() {
                rightArrowButton.setText(String.valueOf(stepDetector.getStepCountX()));
            }

            @Override
            public void stepY() {
                leftArrowButton.setText(String.valueOf(stepDetector.getStepCountY()));
            }
        });
    }


    private void moveCarLeft() {
        // Move the car left only if it's not at the leftmost column
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
        gameMatrix[ROWS - 1][currentColumn].setImageDrawable(null);
        coinMatrix[ROWS - 1][currentColumn] = 0;
        gameMatrix[ROWS - 1][newCol].setImageResource(CAR_IMAGE);
        currentColumn = newCol;
    }

    private void setGameMatrix() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gameMatrix[i][j] = findViewById(R.id.row1_col1 + (i * COLS) + j);
                drawableMatrix[i][j] = findViewById(R.id.row1_col1 + (i * COLS) + j);

            }
        }
    }


    private void handleCollision() {
        backgroundSound.playSound();
        // Reduce lives
        lives--;
        // Show a toast message
        SignalManager.getInstance().toast("Collision! Lives remaining: " + lives);
        // Vibrate the device for 500 milliseconds
        SignalManager.getInstance().vibrate(500);
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

    private void startScoreTimer()  {
        Handler scoreHandler = new Handler();

        scoreHandler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
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




    @Override
    protected void onStop() {
        super.onStop();
        delayHandler.removeCallbacks(runnable);
        delayHandler.removeCallbacksAndMessages(null);

    }

    private Player setValuseToPlayer(Player newPlayer){
        newPlayer.setLocation("");
        newPlayer.setScore(curScore);
        newPlayer.setName(getIntent().getStringExtra("PLAYER_NAME"));
        return newPlayer;
    }

    private void gameOver() {
        // Display game over message and handle game over actions
        SignalManager.getInstance().toast("Game Over!");
        setValuseToPlayer(newPlayer);
        leaderboardList.addPlayer(newPlayer);
        sharedPreferencesManager.putLeaderboardList("LEADERBOARDLIST", leaderboardList);
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
        finish();

    }


}
