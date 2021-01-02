package com.example.cardwars;

public class Card {
    private int value;
    private int sign;
    private boolean isDrawn;

    public Card(){

    }

    public Card(int value,int sign) {
        setValue(value);
        setDrawn(false);
        setSign(sign);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean drawn) {
        isDrawn = drawn;
    }
}
