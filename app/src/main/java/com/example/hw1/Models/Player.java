package com.example.hw1.Models;

public class Player {

    private String name = "";
    private int score = 0;
    private String location = "";

    public Player() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getScore() {

        return score;
    }



    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", location=" + location +
                '}';
    }
}
