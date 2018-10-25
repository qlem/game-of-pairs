package com.example.qlem.pairsgame;

public class Card {
    public int resId;
    public int fromX;
    public int fromY;
    public int toX;
    public int toY;
    public CardState state;

    Card(int resId) {
        this.resId = resId;
        this.state = CardState.HIDDEN;
    }
}
