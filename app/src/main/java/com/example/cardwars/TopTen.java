package com.example.cardwars;

import java.util.ArrayList;

public class TopTen {

    private ArrayList<Player_Winner> highscores = new ArrayList<>();

    public TopTen() {

    }

    public TopTen(ArrayList<Player_Winner> highscores) {
        setHighscores(highscores);
    }

    public ArrayList<Player_Winner> getHighscores() {
        return highscores;
    }

    public void setHighscores(ArrayList<Player_Winner> highscores) {
        this.highscores = highscores;
    }

}
