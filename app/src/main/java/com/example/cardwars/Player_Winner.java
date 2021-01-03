package com.example.cardwars;

public class Player_Winner {
    private String winner_name;
    private int max_score;
    private double lon;
    private double lat;

    public Player_Winner() {

    }
    public Player_Winner(String winner_name, int max_score, double lat, double lon) {
        setWinner_name(winner_name);
        setMax_score(max_score);
        setLat(lat);
        setLon(lon);
    }
    public String getWinner_name() {
        return winner_name;
    }

    public void setWinner_name(String winner_name) {
        this.winner_name = winner_name;
    }

    public int getMax_score() {
        return max_score;
    }

    public void setMax_score(int max_score) {
        this.max_score = max_score;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


}
