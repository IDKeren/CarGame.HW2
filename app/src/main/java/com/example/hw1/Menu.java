package com.example.hw1;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Models.Player;
import com.example.hw1.Utilities.BackgroundSound;

public class Menu extends AppCompatActivity {
    private Button fastButton;
    private Button slowButton;
    private Button exitButton;
    private Button leaderBoardButton;
    private BackgroundSound backgroundSound;
    private EditText playerName;
    private EditText chooseGameMODEText;
    private Switch modeSwitch;
    private boolean mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        findViews();
        setOnClickListners();


    }



    private void startGameActivity(int delay) {
        String name = playerName.getText().toString();
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("PLAYER_NAME",name);
        intent.putExtra("DELAY_KEY",delay);
        intent.putExtra("MODE",mode);
        startActivity(intent);
        finish();
    }

    private void startLeaderBoardActivity() {
        Intent intent = new Intent(this, LeaderBoard.class);
        startActivity(intent);
        finish();
    }



    private void findViews(){
        fastButton = findViewById(R.id.fastButton);
        slowButton = findViewById(R.id.slowButton);
        exitButton = findViewById(R.id.exitButton);
        leaderBoardButton = findViewById(R.id.leaderBoardButton);
        modeSwitch = findViewById(R.id.switch1);
        chooseGameMODEText = findViewById(R.id.chooseGameMODEText);
        playerName = findViewById(R.id.nameEditText);
    }

    private void setOnClickListners(){
        // Set an OnClickListener for the "Slow" button
        slowButton.setOnClickListener(View -> startGameActivity(1800));

        // Set an OnClickListener for the "Fast" button
        fastButton.setOnClickListener(View -> startGameActivity(1000));

        // Set an OnClickListener for the "Leader Board" button
        leaderBoardButton.setOnClickListener(View -> startLeaderBoardActivity());

        modeSwitch.setOnClickListener(View -> mode = true);

        // Set an OnClickListener for the "Player name to clear the initial text"
        playerName.setOnClickListener(View -> clearText(playerName));

        playerName.getText().toString();

        // Set an OnClickListener for the "Exit" button
        exitButton.setOnClickListener(View -> finish());
    }

    private void clearText(EditText playerName){
        playerName.setText("");
    }

    private void changeGameMode(boolean mode){

    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundSound = new BackgroundSound(this, R.raw.lifelike);
        backgroundSound.playSound();

    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundSound.stopSound();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}
