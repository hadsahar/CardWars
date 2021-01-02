package com.example.cardwars;

public class Player {
    private Card currentCard;
    private int points;

    public Player() {
        setCurrentCard(null);
        setPoints(0);
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public int getPoints() {
        return points;
    }

    private void setPoints(int points) {
        this.points = points;
    }

    public void addPoint(){
        this.setPoints(this.getPoints() + 1);
    }
}
