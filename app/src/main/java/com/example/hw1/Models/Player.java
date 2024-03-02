package com.example.hw1.Models;

import java.time.LocalDate;
import java.util.Date;

public class Player {

    private String name = "";
    private int score = 0;
    private String location = "";

    private Date date = new Date();

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", location='" + location + '\'' +
                ", date=" + date +
                '}';
    }
}
