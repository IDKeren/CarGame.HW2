package com.example.hw1.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderBoardList {
    private String name = "";
    private ArrayList<Player> playerArrayList = new ArrayList<>();

    public LeaderBoardList() {
    }

    public String getName() {

        return name;
    }

    public LeaderBoardList setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<Player> getPLayersArrayList() {

        return this.playerArrayList;
    }

    public LeaderBoardList setPlayersArrayList(ArrayList<Player> playerArrayList) {
        this.playerArrayList = playerArrayList;
        return this;
    }

    public LeaderBoardList addPlayer(Player player) {
        if (player != null) {
            playerArrayList.add(player);
            Collections.sort(playerArrayList, Comparator.comparingInt(Player::getScore).reversed());
            if (playerArrayList.size() > 10) {
                playerArrayList = (ArrayList<Player>) playerArrayList.subList(0, 10); // Keep only top 10 players
            }
        }else{
            Log.d("Player is null","Player is null" );
        }
        return this;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", Players List=" + playerArrayList +
                '}';
    }
}