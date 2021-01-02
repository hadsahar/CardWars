package com.example.cardwars;

import android.media.MediaPlayer;

import java.util.Random;

public class GameManager {
    private Player player1;
    private Player player2;
    private Card[] allCards;


    public GameManager(){
    }

    public GameManager(Player player1, Player player2) {
        setPlayer1(player1);
        setPlayer2(player2);
        allCards = initDeck();
    }

    private void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    private void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    // A card deck can have 2 (=2) to Ace (=14), and 4 kinds of signs.
    // A card deck can have only 1 of the same card and a card cannot be drawn twice
    public Card[] initDeck() {
        Card[] allCards = new Card[52];
        int counter = 0;
        for (int sign = 1; sign <= 4;sign++) {
            for(int value = 2; value <= 14; value++, counter++) {
                allCards[counter] = new Card(value, sign);
            }
        }
        return allCards;
    }

    // A function that draws a ned card and check who is the winner.
    public void DrawCard(){
        player1.setCurrentCard(randomiseCard(allCards));
        player2.setCurrentCard(randomiseCard(allCards));
        checkCardWinner(player1.getCurrentCard(),player2.getCurrentCard());
    }

    // A function that "draws" a random card and checks if it was drawn before. if so - "draw" again.
    private Card randomiseCard(Card[] allCards) {
        Random r = new Random();
        int i1;
        do {
            i1 = r.nextInt(52);
            if (!allCards[i1].isDrawn()){
                allCards[i1].setDrawn(true);
                return allCards[i1];
            }
        }while(true);
    }

    // A function that checks the current winner according to the cards drawn,
    // Adding a point to the winner.
    private void checkCardWinner(Card card1, Card  card2) {
        if(card1.getValue() > card2.getValue()){
            player1.addPoint();// player 1 wins
        }else{
            if (card1.getValue() < card2.getValue()){
                player2.addPoint(); // player 2 wins
            }
        }
    }

    // a function that returns the number of the player who has the advantage at the current
    // point of the game. returns 0 if the two players are tied.
    public int checkGameWinner(){
        if (player1.getPoints() > player2.getPoints()){
            return 1;
        }
        if (player1.getPoints() == player2.getPoints()){
            return 0;
        }
        return 2;
    }
}
