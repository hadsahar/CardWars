package com.example.cardwars;

import java.util.Random;

public class GameManager {
    private Player player1;
    private Player player2;
    private Card[] allCards;

    public GameManager(){
        setPlayer1(new Player());
        setPlayer2(new Player());
        allCards = initDeck();
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

    public void DrawCard(){
        player1.setCurrentCard(randomiseCard(allCards));
        player2.setCurrentCard(randomiseCard(allCards));
        checkCardWinner(player1.getCurrentCard(),player2.getCurrentCard());
    }

    // TO CHECK: the loop is not infinity loop
    private Card randomiseCard(Card[] allCards) {

        Random r = new Random();
        do {
            int i1 = r.nextInt(52);
            if (!allCards[i1].isDrawn())
                allCards[i1].setDrawn(true);
            return allCards[i1];
        }while(true);
    }

    private void checkCardWinner(Card card1, Card  card2) {
        if(card1.getValue() > card2.getValue()){
            player1.addPoint();// player 1 wins
        }else{
            if (card1.getValue() < card2.getValue()){
                player2.addPoint(); // player 2 wins
            }
        }
    }
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
