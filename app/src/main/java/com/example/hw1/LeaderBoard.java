package com.example.hw1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.hw1.Models.Player;
import com.google.gson.Gson;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.Models.LeaderBoardList;
import com.example.hw1.Utilities.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class LeaderBoard extends AppCompatActivity {

    private Button backButton;
    private TextView leaderBoardTextView ;
    private SharedPreferencesManager sharedPreferencesManager;
    private LeaderBoardList leaderboardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_board);
        findViews();
        setOnClickListners();
        sharedPreferencesManager = SharedPreferencesManager.getInstance();

        leaderboardList = sharedPreferencesManager.getLeaderboardList("LEADERBOARDLIST");

        displayLeaderBoard();

    }

    private void displayLeaderBoard() {
        StringBuilder leaderboardText = new StringBuilder();
        if (leaderboardList != null) {
        ArrayList<Player> players = leaderboardList.getPLayersArrayList();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            leaderboardText.append(i + 1).append(". ").append(player.getName()).append(": ").append(player.getScore()).append(player.getDate()).append("\n");
        }
    }else {
        leaderboardText.append("Leaderboard is empty.");
    }
        leaderBoardTextView.setText(leaderboardText.toString());
    }



    private void findViews(){
        backButton = findViewById(R.id.backButton);
        leaderBoardTextView = findViewById(R.id.leader_board_text_list);

    }

    private void setOnClickListners(){
        // Set an OnClickListener for the "Back" button
        backButton.setOnClickListener(View -> goBack());

    }



    private void goBack(){
        {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
            finish();

        }
    }
}
